package com.techsavy.de.processor;

import java.util.ArrayList;
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

import com.techsavy.de.domain.RuleEngineData;
import com.techsavy.de.domain.ProcessorResult;
import com.techsavy.de.util.ObjectUtil;

public abstract class BaseAbstractProcessor implements Callable<List<ProcessorResult>> {
  private static int CHILD_PROCESSOR_MAX_WAIT_TIME = 3;
  public RuleEngineData ruleEngineData;
  public ProcessorResult result;
  public Map<String, Object> childProcessorMap;
  public int depth = 0;
  protected List<Rule> rules = new ArrayList<Rule>();
  protected List<Prerequisite> prerequisites = new ArrayList<Prerequisite>();
  protected Future<List<ProcessorResult>> processorFuture;
 
  public void setProcessorData(BaseAbstractProcessor processor, RuleEngineData argRuleEngineData, ProcessorResult argResult,
      Map<String, Object> argProcessorMap, int depth) {
    processor.ruleEngineData = argRuleEngineData;
    processor.result = argResult;
    processor.childProcessorMap = argProcessorMap;
    processor.depth = depth;
  }
  
  @SuppressWarnings("unchecked")
  public List<ProcessorResult> process(ExecutorService executor, RuleEngineData ruleEngineData, ProcessorResult result, List<ProcessorResult> results,
      Map<String, Object> argProcessorMap, int depth, int maxWaitTimeSeconds) {
    if(isLeafNode(argProcessorMap) || stopCondition()) {
      return results;
    }
    List<BaseAbstractProcessor> processors = new ArrayList<BaseAbstractProcessor>();
    for (String key : argProcessorMap.keySet()) {
      Map<String, Object> childProcessors = (Map<String, Object>)argProcessorMap.get(key);
      BaseAbstractProcessor processor = (BaseAbstractProcessor) ObjectUtil.getInstanceByName(key);
      ProcessorResult clonedResult = (ProcessorResult) SerializationUtils.clone(result);
      processor.setProcessorData(processor, ruleEngineData, clonedResult, childProcessors, depth);
      processorFuture = executor.submit(processor);
      processors.add(processor);
      try {
        List<ProcessorResult> iterResults = processorFuture.get(maxWaitTimeSeconds, TimeUnit.SECONDS);
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
  public List<ProcessorResult> call() throws Exception {
    return processRules();
  }

  private List<ProcessorResult> processRules() {
    if(!processPreRequisite(ruleEngineData)) {
      return null;
    }
    List<ProcessorResult> results = new ArrayList<ProcessorResult>();
    for (Rule rule : rules) {
      rule.process(ruleEngineData, result);
      results.add(result);
    }
    return results;
  }
  
  protected boolean processPreRequisite(RuleEngineData argRuleEngineData) {
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
  public List<ProcessorResult> processSequentially(RuleEngineData ruleEngineData, ProcessorResult result, List<ProcessorResult> results,
      Map<String, Object> argProcessorMap, int depth) {
    if(isLeafNode(argProcessorMap)) {
      return results;
    }
    for (String key : argProcessorMap.keySet()) {
      Map<String, Object> childProcessors = (Map<String, Object>)argProcessorMap.get(key);
      BaseAbstractProcessor processor = (BaseAbstractProcessor) ObjectUtil.getInstanceByName(key);
      ProcessorResult clonedResult = (ProcessorResult) SerializationUtils.clone(result);
      processor.setProcessorData(processor, ruleEngineData, clonedResult, childProcessors, depth);
      List<ProcessorResult> iterResults = processor.processRules();
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
