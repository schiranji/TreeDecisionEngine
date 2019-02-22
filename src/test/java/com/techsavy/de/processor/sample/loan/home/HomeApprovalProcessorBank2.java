package com.techsavy.de.processor.sample.loan.home;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.techsavy.de.domain.PrerequisiteResponse;
import com.techsavy.de.domain.RuleResponse;
import com.techsavy.de.processor.BaseProcessor;

public class HomeApprovalProcessorBank2 extends BaseProcessor {
  private static final Logger log = LogManager.getLogger();
  
  @Override
  protected void buildPrerequistes() {
    prerequisites.add((decisionEngineRequest) -> {
      PrerequisiteResponse prerequisiteResponse = PrerequisiteResponse.getInstance("HomeApprovalProcessorBank2:Prerequiste1");
      log.debug("Processing HomeApprovalProcessorBank2:Prerequiste1: Score: "+" depth:"+depth );
      prerequisiteResponse.setPassed(true);
      return prerequisiteResponse;
   });
  }
  
  @Override
  protected void buildRules() {
    rules.add((decisionEngineRequest, processorResponse) -> { 
      RuleResponse ruleResponse = RuleResponse.getInstance("HomeApprovalProcessorBank2:Rule1");
      log.debug("Processing HomeApprovalProcessorBank2:Rule1: Score: "+processorResponse.getScore() +" depth:"+depth); 
      processorResponse.setScore(processorResponse.getScore()+1);
      return ruleResponse;
    });
  }
}
