package com.techsavy.de.processor;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.techsavy.de.domain.ProcessorResponse;
import com.techsavy.de.domain.RuleResponse;

public class BaseProcessor extends BaseAbstractProcessor {
  private static final Logger log = LogManager.getLogger();
  public BaseProcessor() {
    buildPrerequistes();
    buildRules();
  }
  public BaseProcessor(ProcessorResponse processorResponse, List<ProcessorResponse> results, Map<String, Object> processorMap, int depth, int maxWaitTime) {
    setProcessorData(this, processorResponse, processorMap, depth);
  }

  @Override
  protected void buildPrerequistes() {
  }
  
  @Override
  protected void buildRules() {
    rules.add((argDecisionEngineRequest, processorResponse) -> {
      RuleResponse ruleResponse = RuleResponse.getInstance("BaseProcessor:Rule1");
      log.debug("Processing BaseProcessor:Rule1: Score: "+processorResponse.getScore() +" depth:"+depth); 
      processorResponse.setScore(processorResponse.getScore()+1);
      return ruleResponse;
    });
  }
}
