package com.techsavy.de.processor.bankA;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.techsavy.de.processor.BaseProcessor;

public class AutoApprovalProcecssorBankA extends BaseProcessor {
  private static final Logger log = LogManager.getLogger();
  
  @Override
  protected void buildPrerequistes() {
    prerequisites.add((ruleEngineRequest) -> {
      log.debug("Processing AutoApprovalProcecssorBankA:Prerequiste1: Score: "+" depth:"+depth);
      return true;
    });
    prerequisites.add((ruleEngineRequest) -> {
      log.debug("Processing AutoApprovalProcecssorBankA:Prerequiste2: Score: "+" depth:"+depth);
      return true;
    });
  }
  
  @Override
  protected void buildRules() {
    rules.add((ruleEngineRequest, ruleEngineResponse) -> { 
      log.debug("Processing AutoApprovalProcecssorBankA:Rule1: Score: "+ruleEngineResponse.getScore() +" depth:"+depth); 
      ruleEngineResponse.setScore(ruleEngineResponse.getScore()+1);
    });
    rules.add((ruleEngineRequest, ruleEngineResponse) -> { 
      log.debug("Processing AutoApprovalProcecssorBankA:Rule2: Score: "+ruleEngineResponse.getScore() +" depth:"+depth); 
      ruleEngineResponse.setScore(ruleEngineResponse.getScore()+1);
    });
    rules.add((ruleEngineRequest, ruleEngineResponse) -> { 
      log.debug("Processing AutoApprovalProcecssorBankA:Rule3: Score: "+ruleEngineResponse.getScore() +" depth:"+depth); 
      ruleEngineResponse.setScore(ruleEngineResponse.getScore()+1);
    });
    rules.add((ruleEngineRequest, ruleEngineResponse) -> { 
      log.debug("Processing AutoApprovalProcecssorBankA:Rule4: Score: "+ruleEngineResponse.getScore() +" depth:"+depth); 
      ruleEngineResponse.setScore(ruleEngineResponse.getScore()+1);
    });}

}
