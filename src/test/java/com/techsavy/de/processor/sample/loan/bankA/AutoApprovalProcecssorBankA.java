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
    prerequisites.add((decisionEngineRequest) -> {
      PrerequisiteResponse prerequisiteResponse = PrerequisiteResponse.getInstance("AutoApprovalProcecssorBankA:Prerequiste1");
      log.debug("Processing AutoApprovalProcecssorBankA:Prerequiste1: Score: "+" depth:"+depth);
      prerequisiteResponse.setPassed(true);
      return prerequisiteResponse;
    });
    prerequisites.add((decisionEngineRequest) -> {
      PrerequisiteResponse prerequisiteResponse = PrerequisiteResponse.getInstance("AutoApprovalProcecssorBank1:Prerequiste1");
      log.debug("Processing AutoApprovalProcecssorBankA:Prerequiste2: Score: "+" depth:"+depth);
      prerequisiteResponse.setPassed(true);
      return prerequisiteResponse;
    });
  }
  
  @Override
  protected void buildRules() {
    rules.add((decisionEngineRequest, processorResponse) -> {
      RuleResponse ruleResponse = RuleResponse.getInstance("AutoApprovalProcecssorBankA:Rule1");
      decisionEngineRequest.setTestCounter(decisionEngineRequest.getTestCounter()+1);
      log.debug("Processing AutoApprovalProcecssorBankA:Rule1: Score: "+processorResponse.getScore() +" depth:"+depth+", Test Counter1:"+decisionEngineRequest.getTestCounter()); 
      processorResponse.setScore(processorResponse.getScore()+1);
      return ruleResponse;
    });
    rules.add((decisionEngineRequest, processorResponse) -> {
      decisionEngineRequest.setTestCounter(decisionEngineRequest.getTestCounter()+1);
      RuleResponse ruleResponse = RuleResponse.getInstance("AutoApprovalProcecssorBankA:Rule2");
      log.debug("Processing AutoApprovalProcecssorBankA:Rule2: Score: "+processorResponse.getScore() +" depth:"+depth+", Test Counter2:"+decisionEngineRequest.getTestCounter()); 
      processorResponse.setScore(processorResponse.getScore()+1);
      return ruleResponse;
    });
    rules.add((decisionEngineRequest, processorResponse) -> {
      decisionEngineRequest.setTestCounter(decisionEngineRequest.getTestCounter()+1);
      RuleResponse ruleResponse = RuleResponse.getInstance("AutoApprovalProcecssorBankA:Rule3");
      log.debug("Processing AutoApprovalProcecssorBankA:Rule3: Score: "+processorResponse.getScore() +" depth:"+depth+", Test Counter3:"+decisionEngineRequest.getTestCounter()); 
      processorResponse.setScore(processorResponse.getScore()+1);
      return ruleResponse;
    });
    rules.add((decisionEngineRequest, processorResponse) -> { 
      RuleResponse ruleResponse = RuleResponse.getInstance("AutoApprovalProcecssorBankA:Rule4");
      log.debug("Processing AutoApprovalProcecssorBankA:Rule4: Score: "+processorResponse.getScore() +" depth:"+depth); 
      processorResponse.setScore(processorResponse.getScore()+1);
      return ruleResponse;
    });}

}
