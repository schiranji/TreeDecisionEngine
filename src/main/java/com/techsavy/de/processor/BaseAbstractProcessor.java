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

import com.techsavy.de.domain.ProcessorResponse;
import com.techsavy.de.domain.RuleEngineRequest;
import com.techsavy.de.util.ObjectUtil;

public abstract class BaseAbstractProcessor implements Callable<List<ProcessorResponse>> {
  private static final Logger auditLog = LogManager.getLogger("auditlog");
  
  private static int CHILD_PROCESSOR_MAX_WAIT_TIME = 3;
  public RuleEngineRequest ruleEngineData;
  public ProcessorResponse result;
  public Map<String, Object> childProcessorMap;
  public int depth = 0;
  protected List<Rule> rules = new ArrayList<Rule>();
  protected List<Prerequisite> prerequisites = new ArrayList<Prerequisite>();
 
  public void setProcessorData(BaseAbstractProcessor processor, RuleEngineRequest argRuleEngineData, ProcessorResponse argResult,
      Map<String, Object> argProcessorMap, int depth) {
    processor.ruleEngineData = argRuleEngineData;
    processor.result = argResult;
    processor.childProcessorMap = argProcessorMap;
    processor.depth = depth;
  }
  
  @SuppressWarnings("unchecked")
  public List<ProcessorResponse> process(ExecutorService executor, RuleEngineRequest ruleEngineData, ProcessorResponse result, List<ProcessorResponse> results,
      Map<String, Object> argProcessorMap, int depth, int maxWaitTimeSeconds) {
    if(isLeafNode(argProcessorMap) || stopCondition()) {
      return results;
    }
    Map<BaseAbstractProcessor, Future<List<ProcessorResponse>>> processors = new HashMap<BaseAbstractProcessor, Future<List<ProcessorResponse>>>();
    for (String key : argProcessorMap.keySet()) {
      Map<String, Object> childProcessors = (Map<String, Object>)argProcessorMap.get(key);
      BaseAbstractProcessor processor = (BaseAbstractProcessor) ObjectUtil.getInstanceByName(key);
      ProcessorResponse clonedResult = (ProcessorResponse) SerializationUtils.clone(result);
      processor.setProcessorData(processor, ruleEngineData, clonedResult, childProcessors, depth);
      Future<List<ProcessorResponse>> processorFuture = executor.submit(processor);
      processors.put(processor, processorFuture);  
    }
    for(BaseAbstractProcessor processor:processors.keySet()) {
      try {
        Future<List<ProcessorResponse>> processorFuture = processors.get(processor);
        List<ProcessorResponse> iterResults = processorFuture.get(maxWaitTimeSeconds, TimeUnit.SECONDS);
        if(iterResults != null && !iterResults.isEmpty()) {
          if(isLeafNode(processor.childProcessorMap) || stopCondition()) { //Add only leaf node results
            iterResults.get(0).setProcessor(processor.getClass().getName());
            results.addAll(iterResults); 
          } 
          process(executor, ruleEngineData, iterResults.get(0), results,
              processor.childProcessorMap, (depth+1), CHILD_PROCESSOR_MAX_WAIT_TIME);          
        }
      } catch (ExecutionException | TimeoutException | InterruptedException e) {
        throw new RuntimeException("Error while retrieving processor results. "+processor.getClass().getName(), e);
      }  
    }
    return results;
  }
  
  @Override
  public List<ProcessorResponse> call() throws Exception {
    return processRules();
  }

  private List<ProcessorResponse> processRules() {
    long startTime = System.currentTimeMillis();
    if(!processPreRequisite(ruleEngineData)) {
      auditLog.info("Proccessor:"+this.getClass().getName()+", Timespan(millis):"+(System.currentTimeMillis()-startTime));
      return null;
    }
    List<ProcessorResponse> results = new ArrayList<ProcessorResponse>();
    for (Rule rule : rules) {
      long ruleStartTime = System.nanoTime();
      rule.process(ruleEngineData, result);
      auditLog.info("Rule: Timespan(micro):"+(System.nanoTime()-ruleStartTime)/1000);
    }
    result.setAuditTime();
    auditLog.info("Proccessor:"+this.getClass().getName()+", Timespan from root(millis):"+result.getAudit().getTimespan());
    results.add(result);
    auditLog.info("Proccessor:"+this.getClass().getName()+", Timespan(millis):"+(System.currentTimeMillis()-startTime));
    return results;
  }
  
  protected boolean processPreRequisite(RuleEngineRequest argRuleEngineData) {
    if(prerequisites != null) {
      for(Prerequisite prerequisite: prerequisites) {
        if(!prerequisite.process(ruleEngineData)) {
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
    return "DECLINED".equals(result.getDecision());
  }

  @SuppressWarnings("unchecked")
  public List<ProcessorResponse> processSequentially(RuleEngineRequest ruleEngineData, ProcessorResponse result, List<ProcessorResponse> results,
      Map<String, Object> argProcessorMap, int depth) {
    if(isLeafNode(argProcessorMap)) {
      return results;
    }
    for (String key : argProcessorMap.keySet()) {
      Map<String, Object> childProcessors = (Map<String, Object>)argProcessorMap.get(key);
      BaseAbstractProcessor processor = (BaseAbstractProcessor) ObjectUtil.getInstanceByName(key);
      ProcessorResponse clonedResult = (ProcessorResponse) SerializationUtils.clone(result);
      processor.setProcessorData(processor, ruleEngineData, clonedResult, childProcessors, depth);
      List<ProcessorResponse> iterResults = processor.processRules();
      if(iterResults != null && iterResults.size() > 0) {
        processor.processSequentially(ruleEngineData, clonedResult, results, childProcessors, (depth+1));
        if(isLeafNode(childProcessors) || stopCondition()) {
          iterResults.get(0).setProcessor(processor.getClass().getName());
          results.addAll(iterResults);
        }
      }
    }
    return results;
  }
  
  /*private void invokeProcessorNewThread(ExecutorService executor, BaseAbstractProcessor processor, Map<String, Object> childProcessors, List<RuleEngineResult> results, int maxWaitTimeSeconds) {
	  Future<List<RuleEngineResult>> future = executor.submit(processor);      
      try {
        List<RuleEngineResult> iterResults = future.get(maxWaitTimeSeconds, TimeUnit.SECONDS);
        if(iterResults != null && iterResults.size() > 0) {
          if(childProcessors == null) { results.addAll(iterResults); } //Add only leaf node results
            process(executor, ruleEngineData, iterResults.get(0), results,
              childProcessors, (depth+1), CHILD_PROCESSOR_MAX_WAIT_TIME);          
        }
      } catch (ExecutionException | TimeoutException | InterruptedException e) {
        throw new RuntimeException("Error while processing processor "+processor.getClass().getName(), e);
      }
  }

  private void invokeProcessorSameThread(ExecutorService executor, BaseAbstractProcessor processor, Map<String, Object> childProcessors, List<RuleEngineResult> results, int maxWaitTimeSeconds) {
    List<RuleEngineResult> iterResults = processor.processRules();
    if(iterResults != null && iterResults.size() > 0) {
      processor.processSequentially(ruleEngineData, processor.result, results, childProcessors, (depth+1));
      if(isLeafNode(childProcessors) || stopCondition()) { 
        results.addAll(iterResults);
      }
    }
  }*/
  
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
