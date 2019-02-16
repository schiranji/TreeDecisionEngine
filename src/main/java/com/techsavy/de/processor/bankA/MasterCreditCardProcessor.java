package com.techsavy.de.processor.bankA;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.techsavy.de.processor.BaseAbstractProcessor;

public class MasterCreditCardProcessor extends BaseAbstractProcessor {
  private static final Logger log = LogManager.getLogger();
  
  @Override
  protected void buildPrerequistes() {
    prerequisites.add((ruleEngineRequest) -> {
      log.debug("Processing MasterCreditCardProcessor:Prerequiste1: Score: "+" depth:"+depth );
      return true;
    });
  }

  @Override
  protected void buildRules() {
    rules.add((ruleEngineRequest, ruleEngineResponse) -> 
    { 
      log.debug("Processing MasterCreditCardProcessor:Rule1: Score: "+ruleEngineResponse.getScore() +" depth:"+depth); 
      ruleEngineResponse.setScore(ruleEngineResponse.getScore()+1);
      ruleEngineResponse.setDecision("DECLINED");
    });
  }
}
