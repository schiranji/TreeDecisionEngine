package com.techsavy.de.processor.sample.loan.auto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.techsavy.de.domain.PrerequisiteResponse;
import com.techsavy.de.domain.RuleResponse;
import com.techsavy.de.processor.BaseProcessor;

public class MasterAutoApprovalProcecssor extends BaseProcessor {
  private static final Logger log = LogManager.getLogger();
  
  @Override
  protected void buildPrerequistes() {
    prerequisites.add((ruleEngineRequest) -> {
      PrerequisiteResponse prerequisiteResponse = PrerequisiteResponse.getInstance("MasterAutoApprovalProcecssor:Prerequiste1");
      log.debug("Processing MasterAutoApprovalProcecssor:Prerequiste1: Score: "+" depth:"+depth );
      prerequisiteResponse.setPassed(true);
      return prerequisiteResponse;
    });
  }

  @Override
  protected void buildRules() {
    rules.add((ruleEngineRequest, processorResponse) -> {
      RuleResponse ruleResponse = RuleResponse.getInstance("MasterAutoApprovalProcecssor:Rule1");
      log.debug("Processing MasterAutoApprovalProcecssor:Rule1: Score: "+processorResponse.getScore()+" depth:"+depth); 
      processorResponse.setScore(processorResponse.getScore()+1);
      return ruleResponse;
    });
  }
}
