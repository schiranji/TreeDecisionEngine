package com.techsavy.de.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.techsavy.de.domain.PrerequisiteResponse;
import com.techsavy.de.domain.RuleResponse;

public class BaseProcessor extends BaseAbstractProcessor {
  private static final Logger log = LogManager.getLogger();
  public BaseProcessor() {
    buildPrerequistes();
    buildRules();
  }

  @Override
  protected void buildPrerequistes() {
    prerequisites.add((argDecisionEngineRequest) -> {
      PrerequisiteResponse prerequisiteResponse = PrerequisiteResponse.getInstance("BaseProcessor:Prerequiste");
      log.debug("Processing BaseProcessor:Prerequiste: Score: "+" depth:"+depth);
      prerequisiteResponse.setPassed(true);
      return prerequisiteResponse;
   });
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
