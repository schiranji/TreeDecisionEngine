package com.techsavy.de.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.techsavy.de.domain.RuleResponse;

public class BaseProcessor extends AbstractProcessor {
  private static final Logger log = LogManager.getLogger();
  private static final String PROCESSOR_VERSION = "1.0.0";

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
  @Override
  protected String getProcessorVersion() {
    return PROCESSOR_VERSION;
  }
}
