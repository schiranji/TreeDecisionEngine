package com.techsavy.de.processor.sample.loan.bankA;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.techsavy.de.domain.PrerequisiteResponse;
import com.techsavy.de.domain.RuleResponse;
import com.techsavy.de.processor.BaseProcessor;

public class AutoApprovalProcecssorBankA extends BaseProcessor {
  private static final Logger log = LogManager.getLogger();
  
  @Override
  protected void buildPrerequistes() {
    prerequisites.add((ruleEngineRequest) -> {
      PrerequisiteResponse prerequisiteResponse = PrerequisiteResponse.getInstance("AutoApprovalProcecssorBankA:Prerequiste1");
      log.debug("Processing AutoApprovalProcecssorBankA:Prerequiste1: Score: "+" depth:"+depth);
      prerequisiteResponse.setPassed(true);
      return prerequisiteResponse;
    });
    prerequisites.add((ruleEngineRequest) -> {
      PrerequisiteResponse prerequisiteResponse = PrerequisiteResponse.getInstance("AutoApprovalProcecssorBank1:Prerequiste1");
      log.debug("Processing AutoApprovalProcecssorBankA:Prerequiste2: Score: "+" depth:"+depth);
      prerequisiteResponse.setPassed(true);
      return prerequisiteResponse;
    });
  }
  
  @Override
  protected void buildRules() {
    rules.add((ruleEngineRequest, ruleEngineResponse) -> {
      RuleResponse ruleResponse = RuleResponse.getInstance("AutoApprovalProcecssorBankA:Rule1");
      log.debug("Processing AutoApprovalProcecssorBankA:Rule1: Score: "+ruleEngineResponse.getScore() +" depth:"+depth); 
      ruleEngineResponse.setScore(ruleEngineResponse.getScore()+1);
      return ruleResponse;
    });
    rules.add((ruleEngineRequest, ruleEngineResponse) -> {
      RuleResponse ruleResponse = RuleResponse.getInstance("AutoApprovalProcecssorBankA:Rule2");
      log.debug("Processing AutoApprovalProcecssorBankA:Rule2: Score: "+ruleEngineResponse.getScore() +" depth:"+depth); 
      ruleEngineResponse.setScore(ruleEngineResponse.getScore()+1);
      return ruleResponse;
    });
    rules.add((ruleEngineRequest, ruleEngineResponse) -> {
      RuleResponse ruleResponse = RuleResponse.getInstance("AutoApprovalProcecssorBankA:Rule3");
      log.debug("Processing AutoApprovalProcecssorBankA:Rule3: Score: "+ruleEngineResponse.getScore() +" depth:"+depth); 
      ruleEngineResponse.setScore(ruleEngineResponse.getScore()+1);
      return ruleResponse;
    });
    rules.add((ruleEngineRequest, ruleEngineResponse) -> { 
      RuleResponse ruleResponse = RuleResponse.getInstance("AutoApprovalProcecssorBankA:Rule4");
      log.debug("Processing AutoApprovalProcecssorBankA:Rule4: Score: "+ruleEngineResponse.getScore() +" depth:"+depth); 
      ruleEngineResponse.setScore(ruleEngineResponse.getScore()+1);
      return ruleResponse;
    });}

}
