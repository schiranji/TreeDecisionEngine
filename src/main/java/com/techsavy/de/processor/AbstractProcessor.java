package com.techsavy.de.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.techsavy.de.common.ResponseCode;
import com.techsavy.de.domain.Audit;
import com.techsavy.de.domain.DecisionEngineRequest;
import com.techsavy.de.domain.PostrequisiteResponse;
import com.techsavy.de.domain.PrerequisiteResponse;
import com.techsavy.de.domain.ProcessorResponse;
import com.techsavy.de.domain.RuleResponse;
import com.techsavy.de.util.LogUtil;
import com.techsavy.de.util.ObjectUtil;

public abstract class AbstractProcessor implements ProcessorInt {

  private static final Logger log = LogManager.getLogger();
  
  private static int CHILD_PROCESSOR_MAX_WAIT_TIME = 3;
  private static final String AUDIT_TYPE_PROCESSOR = "Processor";
  public DecisionEngineRequest decisionEngineRequest;
  public ProcessorResponse processorResponse;
  public Map<String, Object> childProcessorMap;
  public int depth = 0;
  protected List<Rule> rules = new ArrayList<Rule>();
  protected List<Prerequisite> prerequisites = new ArrayList<Prerequisite>();
  protected List<Postrequisite> postActions = new ArrayList<Postrequisite>();
  
	public AbstractProcessor() {
		buildPrerequistes();
		buildRules();
		buildActions();
		/*if(prerequisites == null || prerequisites.size() <= 0) {
			buildPrerequistes();			
		}
		if(rules == null || rules.size() <= 0) {
			buildRules();			
		}
		if(postActions == null || postActions.size() <= 0) {
			buildActions();			
		}*/
	}
 
  public void setProcessorData(DecisionEngineRequest decisionEngineRequest, ProcessorInt processor, ProcessorResponse argProcessorResponse,
      Map<String, Object> argProcessorMap, int depth) {
    processor.setDecisionEngineRequest(decisionEngineRequest);
    processor.setProcessorResponse(argProcessorResponse);
    processorResponse.setAudit(Audit.getInstance(AUDIT_TYPE_PROCESSOR,processor.getClass().getName()));
    processor.setChildProcessorMap(argProcessorMap);
    processor.setDepth(depth);
  }
  
  @SuppressWarnings("unchecked")
  public List<ProcessorResponse> process(DecisionEngineRequest decisionEngineRequest, ExecutorService executor, ProcessorResponse processorResponse, List<ProcessorResponse> argProcessorResponses,
      Map<String, Object> argProcessorMap, int depth, int maxWaitTimeSeconds) {
    long startTime = System.currentTimeMillis();
    if(isLeafNode(argProcessorMap) || stopCondition() || errorCondition(processorResponse)) {
      return argProcessorResponses;
    }
    Map<ProcessorInt, Future<List<ProcessorResponse>>> processors = new HashMap<ProcessorInt, Future<List<ProcessorResponse>>>();
    for (String key : argProcessorMap.keySet()) {
      ProcessorInt processor = (ProcessorInt) ObjectUtil.getInstanceByName(key);
      Map<String, Object> childProcessors = null;
      if(isHttpProcessor(processor)) {
        ((HttpProcessor)processor).setParams((String)argProcessorMap.get(key));
      } else {
        childProcessors = (Map<String, Object>)argProcessorMap.get(key);        
      }
      processor.setProcessorData(decisionEngineRequest, processor, clone(processorResponse), childProcessors, depth);
      Future<List<ProcessorResponse>> processorFuture = executor.submit(processor);
      processors.put(processor, processorFuture);  
    }
    for(ProcessorInt processor:processors.keySet()) {
      try {
        Future<List<ProcessorResponse>> processorFuture = processors.get(processor);
        List<ProcessorResponse> iterProcessorResponses = processorFuture.get(maxWaitTimeSeconds, TimeUnit.HOURS);
        if(iterProcessorResponses != null && !iterProcessorResponses.isEmpty()) {
          if(isLeafNode(processor.getChildProcessorMap()) || stopCondition()) { //Add only leaf node responses
            iterProcessorResponses.get(0).setProcessor(processor.getClass().getName());
            argProcessorResponses.addAll(iterProcessorResponses); 
          } 
          process(decisionEngineRequest, executor, iterProcessorResponses.get(0), argProcessorResponses,
              processor.getChildProcessorMap(), (depth+1), CHILD_PROCESSOR_MAX_WAIT_TIME);          
        }
      } catch (TimeoutException e) {
        processorResponse.setResponseCode(ResponseCode.PR_408);
        log.error("Error while retrieving processor responses. "+processor.getClass().getName(), e);
      } catch (InterruptedException | ExecutionException e) {
        processorResponse.setResponseCode(ResponseCode.PR_500);
        log.error("Error while retrieving processor responses. "+processor.getClass().getName(), e);
      } finally {
        LogUtil.logAuditTimeMillis(processor.getClass().getName()+" Processor Execution time(millis):", startTime);
        processorResponse.setAuditTime();
      }
    }
    return argProcessorResponses;
  }

  private boolean isHttpProcessor(Object obj) {
    return obj instanceof HttpProcessor;
  }
  
  public static ProcessorResponse clone(ProcessorResponse processorResponse) {
    return (ProcessorResponse) SerializationUtils.clone(processorResponse);
  }

  private boolean errorCondition(ProcessorResponse processorResponse) {
    return !processorResponse.getResponseCode().equals(ResponseCode.PR_200);
  }
  
  @Override
  public List<ProcessorResponse> call() throws Exception {
    return processRules();
  }

  @Override
  public List<ProcessorResponse> processRules() {
    DecisionEngineRequest request = getDERequest();
    processorResponse.setAudit(Audit.getInstance(AUDIT_TYPE_PROCESSOR, this.getClass().getName()));
    if(!processPreRequisite()) {
      log.info("Prerequisite failed, returning" + this.getClass().getName());
      return null;
    }
    List<ProcessorResponse> processorResponses = new ArrayList<ProcessorResponse>();
    for (Rule rule : rules) {
      long ruleStartTime = System.nanoTime();
      RuleResponse ruleResponse = rule.process(request, processorResponse);
      ruleResponse.setAuditTime();
      processorResponse.addRuleResponse(ruleResponse);
      LogUtil.logAuditTimeMicros("Rule: "+ ruleResponse.getRuleName(), ruleStartTime);
    }
    if(isHttpProcessor(this)) {
      processorResponses.addAll(((HttpProcessor)this).processorResponses);
    } else {
      processPostActions();
      processorResponses.add(processorResponse);
      processorResponse.setAuditTime();
    }
    return processorResponses;
  }
  
  private DecisionEngineRequest getDERequest() {
    ObjectUtil.assertNotNull(decisionEngineRequest);
    return decisionEngineRequest;
    //return DecisionEngineScope.getDecisionEngineRequest();
  }
  /*private ExecutorService getExecutor() {
    return DecisionEngineScope.getExecutorService();
  }*/
  protected boolean processPreRequisite() {
    if(prerequisites != null) {
      final DecisionEngineRequest request = getDERequest();
      //TODO ?? start storing request and results
      //TODO ?? Check if Processor version is same as previously stored request then return prev results instead of processing again.
      for(Prerequisite prerequisite: prerequisites) {
        PrerequisiteResponse prerequisiteResponse = prerequisite.process(request);
        prerequisiteResponse.setAuditTime();
        processorResponse.addPrerequisiteResponse(prerequisiteResponse);
        if(!prerequisiteResponse.isPassed()) {
          return false;
        }
      }
    }
    return true;
  }
  
  protected void processPostActions() {
    if(postActions != null) {
      final DecisionEngineRequest request = getDERequest();
      //TODO ?? start storing request and results
      //TODO ?? Check if Processor version is same as previously stored request then return prev results instead of processing again.
      for(Postrequisite postrequisite: postActions) {
        PostrequisiteResponse postrequisiteResponses = postrequisite.process(request, processorResponse);
        postrequisiteResponses.setAuditTime();
        processorResponse.addPostrequisiteResponses(postrequisiteResponses);
      }
    }
  }

  private boolean isLeafNode(Map<String, Object> childProcessors) {
    return childProcessors == null;
  }

  private boolean stopCondition() {
    return "DECLINED".equals(processorResponse.getDecision());
  }

  @SuppressWarnings("unchecked")
  public List<ProcessorResponse> processSequentially(DecisionEngineRequest decisionEngineRequest, ProcessorResponse processorResponse, List<ProcessorResponse> argProcessorResponses,
      Map<String, Object> argProcessorMap, int depth) {
    if(isLeafNode(argProcessorMap)) {
      return argProcessorResponses;
    }
    for (String key : argProcessorMap.keySet()) {
      Map<String, Object> childProcessors = null;
      AbstractProcessor processor = (AbstractProcessor) ObjectUtil.getInstanceByName(key);
      if(isHttpProcessor(processor)) {
        ((HttpProcessor)processor).setParams((String)argProcessorMap.get(key));
      } else {
        childProcessors = (Map<String, Object>)argProcessorMap.get(key);        
      }
      ProcessorResponse clonedProcessorResponse = clone(processorResponse);
      processor.setProcessorData(decisionEngineRequest, processor, clonedProcessorResponse, childProcessors, depth);
      List<ProcessorResponse> iterProcessorResponses = processor.processRules();
      if(iterProcessorResponses != null && iterProcessorResponses.size() > 0) {
        processor.processSequentially(decisionEngineRequest, clonedProcessorResponse, argProcessorResponses, childProcessors, (depth+1));
        if(isLeafNode(childProcessors) || stopCondition()) {
          iterProcessorResponses.get(0).setProcessor(processor.getClass().getName());
          argProcessorResponses.addAll(iterProcessorResponses);
        }
      }
    }
    return argProcessorResponses;
  }
  
  	public DecisionEngineRequest getDecisionEngineRequest() {
		return decisionEngineRequest;
	}
	
	public void setDecisionEngineRequest(DecisionEngineRequest decisionEngineRequest) {
		this.decisionEngineRequest = decisionEngineRequest;
	}
	
	public ProcessorResponse getProcessorResponse() {
		return processorResponse;
	}
	
	public void setProcessorResponse(ProcessorResponse processorResponse) {
		this.processorResponse = processorResponse;
	}
	
	public Map<String, Object> getChildProcessorMap() {
		return childProcessorMap;
	}
	
	public void setChildProcessorMap(Map<String, Object> childProcessorMap) {
		this.childProcessorMap = childProcessorMap;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public void setDepth(int depth) {
		this.depth = depth;
	}
  protected abstract void buildPrerequistes();
  protected abstract void buildRules();
  protected abstract void buildActions();
  protected abstract String getProcessorVersion();
}
