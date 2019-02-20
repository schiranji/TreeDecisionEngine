package com.techsavy.de.processor.sample.loan.bankA;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.techsavy.de.domain.PrerequisiteResponse;
import com.techsavy.de.domain.RuleResponse;
import com.techsavy.de.processor.BaseProcessor;

public class MasterApprovalProcecssorBankA extends BaseProcessor {
  private static final Logger log = LogManager.getLogger();
  
  @Override
  protected void buildPrerequistes() {
    prerequisites.add((ruleEngineRequest) -> {
      PrerequisiteResponse prerequisiteResponse = PrerequisiteResponse.getInstance("MasterApprovalProcecssorBankA:Prerequiste1");
      log.debug("Processing MasterApprovalProcecssorBankA:Prerequiste1: Score: "+" depth:"+depth );
      prerequisiteResponse.setPassed(true);
      return prerequisiteResponse;
    });
  }

  @Override
  protected void buildRules() {
    rules.add((ruleEngineRequest, ruleEngineResponse) -> {
      RuleResponse ruleResponse = RuleResponse.getInstance("MasterApprovalProcecssorBankA:Rule1");
      log.debug("Processing MasterApprovalProcecssorBankA:Rule1: Score: "+ruleEngineResponse.getScore()+" depth:"+depth); 
      ruleEngineResponse.setScore(ruleEngineResponse.getScore()+1);
      return ruleResponse;
    });
  }
}
