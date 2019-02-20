package com.techsavy.de.processor.sample.loan.home;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.techsavy.de.domain.PrerequisiteResponse;
import com.techsavy.de.domain.RuleResponse;
import com.techsavy.de.processor.BaseProcessor;

public class MasterHomeApprovalProcessor extends BaseProcessor {
  private static final Logger log = LogManager.getLogger();
  
  @Override
  protected void buildPrerequistes() {
    prerequisites.add((ruleEngineRequest) -> {
      PrerequisiteResponse prerequisiteResponse = PrerequisiteResponse.getInstance("MasterHomeApprovalProcessor:Prerequiste1");
      log.debug("Processing MasterHomeApprovalProcessor:Prerequiste1: Score: "+" depth:"+depth );
      prerequisiteResponse.setPassed(true);
      return prerequisiteResponse;
    });
  }
  
  @Override
  protected void buildRules() {
    rules.add((ruleEngineRequest, ruleEngineResponse) -> {
      RuleResponse ruleResponse = RuleResponse.getInstance("MasterHomeApprovalProcessor:Rule1");
      log.debug("Processing MasterHomeApprovalProcessor:Rule1: Score: "+ruleEngineResponse.getScore() +" depth:"+depth); 
      ruleEngineResponse.setScore(ruleEngineResponse.getScore()+1);
      return ruleResponse;
    });
  }
}
