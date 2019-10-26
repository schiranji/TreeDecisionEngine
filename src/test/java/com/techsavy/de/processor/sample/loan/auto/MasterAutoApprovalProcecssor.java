package com.techsavy.de.processor.sample.loan.auto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.techsavy.de.domain.PrerequisiteResponse;
import com.techsavy.de.domain.RuleResponse;
import com.techsavy.de.processor.AbstractProcessor;

public class MasterAutoApprovalProcecssor extends AbstractProcessor {
  private static final Logger log = LogManager.getLogger();
  private static final String PROCESSOR_VERSION = "1.0.0";
  @Override
  protected void buildPrerequistes() {
    prerequisites.add((decisionEngineRequest) -> {
      PrerequisiteResponse prerequisiteResponse = PrerequisiteResponse.getInstance("MasterAutoApprovalProcecssor:Prerequiste1");
      log.debug("Processing MasterAutoApprovalProcecssor:Prerequiste1: Score: "+processorResponse.getScore()+" depth:"+depth );
      prerequisiteResponse.setPassed(true);
      return prerequisiteResponse;
    });
  }

  @Override
  protected void buildRules() {
    rules.add((decisionEngineRequest, processorResponse) -> {
      RuleResponse ruleResponse = RuleResponse.getInstance("MasterAutoApprovalProcecssor:Rule1");
      log.debug("Processing MasterAutoApprovalProcecssor:Rule1: Score: "+processorResponse.getScore()+" depth:"+depth); 
      processorResponse.setScore(processorResponse.getScore()+1);
      return ruleResponse;
    });
  }
  
  @Override
  protected String getProcessorVersion() {
    return PROCESSOR_VERSION;
  }
}
