package com.techsavy.de.processor.sample.loan.bankA;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.techsavy.de.domain.PrerequisiteResponse;
import com.techsavy.de.domain.RuleResponse;
import com.techsavy.de.processor.BaseProcessor;

public class LoanApprovalProcessorBankA extends BaseProcessor {
  private static final Logger log = LogManager.getLogger();
  
  @Override
  protected void buildPrerequistes() {
    prerequisites.add((ruleEngineRequest) -> {
      PrerequisiteResponse prerequisiteResponse = PrerequisiteResponse.getInstance("LoanApprovalProcessorBankA:Prerequiste1");
      log.debug("Processing LoanApprovalProcessorBankA:Prerequiste1: Score: "+" depth:"+depth );
      prerequisiteResponse.setPassed(true);
      return prerequisiteResponse;
    });
  }
  
  @Override
  protected void buildRules() {
    rules.add((ruleEngineRequest, ruleEngineResponse) -> { 
      RuleResponse ruleResponse = RuleResponse.getInstance("LoanApprovalProcessorBankA: Score");
      log.debug("Processing LoanApprovalProcessorBankA: Score: "+ruleEngineResponse.getScore() +" depth:"+depth); 
      ruleEngineResponse.setScore(ruleEngineResponse.getScore()+1);
      return ruleResponse;
    });
  }
}
