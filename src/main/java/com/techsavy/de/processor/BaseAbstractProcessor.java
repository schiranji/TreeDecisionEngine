package com.techsavy.de.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.techsavy.de.common.Constants;
import com.techsavy.de.common.ResponseCode;
import com.techsavy.de.domain.PrerequisiteResponse;
import com.techsavy.de.domain.ProcessorResponse;
import com.techsavy.de.domain.DecisionEngineRequest;
import com.techsavy.de.domain.RuleResponse;
import com.techsavy.de.util.ObjectUtil;

public abstract class BaseAbstractProcessor implements Callable<List<ProcessorResponse>>, Constants {
  private static final Logger log = LogManager.getLogger();
  private static final Logger auditLog = LogManager.getLogger("auditlog");
  
  private static int CHILD_PROCESSOR_MAX_WAIT_TIME = 3;
  public DecisionEngineRequest decisionEngineRequest;
  public ProcessorResponse processorResponse;
  public Map<String, Object> childProcessorMap;
  public int depth = 0;
  protected List<Rule> rules = new ArrayList<Rule>();
  protected List<Prerequisite> prerequisites = new ArrayList<Prerequisite>();
 
  public void setProcessorData(BaseAbstractProcessor processor, DecisionEngineRequest argDecisionEngineRequest, ProcessorResponse argProcessorResponse,
      Map<String, Object> argProcessorMap, int depth) {
    processor.decisionEngineRequest = argDecisionEngineRequest;
    processor.processorResponse = argProcessorResponse;
    processor.childProcessorMap = argProcessorMap;
    processor.depth = depth;
  }
  
  @SuppressWarnings("unchecked")
  public List<ProcessorResponse> process(ExecutorService executor, DecisionEngineRequest argDecisionEngineRequest, ProcessorResponse processorResponse, List<ProcessorResponse> argProcessorResponses,
      Map<String, Object> argProcessorMap, int depth, int maxWaitTimeSeconds) {
    if(isLeafNode(argProcessorMap) || stopCondition() || errorCondition(processorResponse)) {
      return argProcessorResponses;
    }
    Map<BaseAbstractProcessor, Future<List<ProcessorResponse>>> processors = new HashMap<BaseAbstractProcessor, Future<List<ProcessorResponse>>>();
    for (String key : argProcessorMap.keySet()) {
      Map<String, Object> childProcessors = (Map<String, Object>)argProcessorMap.get(key);
      BaseAbstractProcessor processor = (BaseAbstractProcessor) ObjectUtil.getInstanceByName(key);
      ProcessorResponse clonedProcessorResponse = (ProcessorResponse) SerializationUtils.clone(processorResponse);
      processor.setProcessorData(processor, argDecisionEngineRequest, clonedProcessorResponse, childProcessors, depth);
      Future<List<ProcessorResponse>> processorFuture = executor.submit(processor);
      processors.put(processor, processorFuture);  
    }
    for(BaseAbstractProcessor processor:processors.keySet()) {
      try {
        Future<List<ProcessorResponse>> processorFuture = processors.get(processor);
        List<ProcessorResponse> iterProcessorResponses = processorFuture.get(maxWaitTimeSeconds, TimeUnit.SECONDS);
        if(iterProcessorResponses != null && !iterProcessorResponses.isEmpty()) {
          if(isLeafNode(processor.childProcessorMap) || stopCondition()) { //Add only leaf node responses
            iterProcessorResponses.get(0).setProcessor(processor.getClass().getName());
            argProcessorResponses.addAll(iterProcessorResponses); 
          } 
          process(executor, argDecisionEngineRequest, iterProcessorResponses.get(0), argProcessorResponses,
              processor.childProcessorMap, (depth+1), CHILD_PROCESSOR_MAX_WAIT_TIME);          
        }
      } catch (TimeoutException e) {
        processorResponse.setResponseCode(ResponseCode.PR_408);
        log.error("Error while retrieving processor responses. "+processor.getClass().getName(), e);
      } catch (InterruptedException | ExecutionException e) {
        processorResponse.setResponseCode(ResponseCode.PR_500);
        log.error("Error while retrieving processor responses. "+processor.getClass().getName(), e);
      } finally {
        processorResponse.setAuditTime();
      }
    }
    return argProcessorResponses;
  }

  private boolean errorCondition(ProcessorResponse processorResponse) {
    return !processorResponse.getResponseCode().equals(ResponseCode.PR_200);
  }
  
  @Override
  public List<ProcessorResponse> call() throws Exception {
    return processRules();
  }

  private List<ProcessorResponse> processRules() {
    long startTime = System.currentTimeMillis();
    if(!processPreRequisite(decisionEngineRequest)) {
      auditLog.info(AUDIT_MARKER, "Proccessor:"+this.getClass().getName()+", Timespan(millis):"+(System.currentTimeMillis()-startTime));
      return null;
    }
    List<ProcessorResponse> processorResponses = new ArrayList<ProcessorResponse>();
    for (Rule rule : rules) {
      long ruleStartTime = System.nanoTime();
      RuleResponse ruleResponse = rule.process(decisionEngineRequest, processorResponse);
      ruleResponse.setAuditTime();
      processorResponse.addRuleResponse(ruleResponse);
      auditLog.info(AUDIT_MARKER, "Rule: Timespan(micro):"+(System.nanoTime()-ruleStartTime)/1000);
    }
    processorResponse.setAuditTime();
    auditLog.info(AUDIT_MARKER, "Proccessor:"+this.getClass().getName()+", Timespan from root(millis):"+processorResponse.getAudit().getTimespan());
    processorResponses.add(processorResponse);
    auditLog.info(AUDIT_MARKER, "Proccessor:"+this.getClass().getName()+", Timespan(millis):"+(System.currentTimeMillis()-startTime));
    return processorResponses;
  }
  
  protected boolean processPreRequisite(DecisionEngineRequest argRuleEngineData) {
    if(prerequisites != null) {
      for(Prerequisite prerequisite: prerequisites) {
        PrerequisiteResponse prerequisiteResponse = prerequisite.process(decisionEngineRequest);
        prerequisiteResponse.setAuditTime();
        processorResponse.addPrerequisiteResponse(prerequisiteResponse);
        if(!prerequisiteResponse.isPassed()) {
          return false;
        }
      }
    }
    return true;
  }

  private boolean isLeafNode(Map<String, Object> childProcessors) {
    return childProcessors == null;
  }

  private boolean stopCondition() {
    return "DECLINED".equals(processorResponse.getDecision());
  }

  @SuppressWarnings("unchecked")
  public List<ProcessorResponse> processSequentially(DecisionEngineRequest ruleEngineData, ProcessorResponse processorResponse, List<ProcessorResponse> argProcessorResponses,
      Map<String, Object> argProcessorMap, int depth) {
    if(isLeafNode(argProcessorMap)) {
      return argProcessorResponses;
    }
    for (String key : argProcessorMap.keySet()) {
      Map<String, Object> childProcessors = (Map<String, Object>)argProcessorMap.get(key);
      BaseAbstractProcessor processor = (BaseAbstractProcessor) ObjectUtil.getInstanceByName(key);
      ProcessorResponse clonedProcessorResponse = (ProcessorResponse) SerializationUtils.clone(processorResponse);
      processor.setProcessorData(processor, ruleEngineData, clonedProcessorResponse, childProcessors, depth);
      List<ProcessorResponse> iterProcessorResponses = processor.processRules();
      if(iterProcessorResponses != null && iterProcessorResponses.size() > 0) {
        processor.processSequentially(ruleEngineData, clonedProcessorResponse, argProcessorResponses, childProcessors, (depth+1));
        if(isLeafNode(childProcessors) || stopCondition()) {
          iterProcessorResponses.get(0).setProcessor(processor.getClass().getName());
          argProcessorResponses.addAll(iterProcessorResponses);
        }
      }
    }
    return argProcessorResponses;
  }
  
  protected void sleepRandom() {
    try {
      Thread.sleep((new Random()).nextInt(1000));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  protected abstract void buildPrerequistes();
  protected abstract void buildRules();
}
