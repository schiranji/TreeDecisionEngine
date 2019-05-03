package com.techsavy.de.processor.sample2.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.techsavy.de.domain.PrerequisiteResponse;
import com.techsavy.de.domain.RuleResponse;
import com.techsavy.de.processor.BaseAbstractProcessor;
import com.techsavy.de.processor.sample2.domain.DecisionEngineRequest2;
import com.techsavy.de.processor.sample2.domain.ProcessorResponse2;

public class ExternalRequestProcessor extends BaseAbstractProcessor {

  private static final Logger log = LogManager.getLogger();
  private static final String PROCESSOR_VERSION = "1.0.0";
  @Override
  protected void buildPrerequistes() {
    prerequisites.add((decisionEngineRequest) -> {
      PrerequisiteResponse prerequisiteResponse = PrerequisiteResponse.getInstance("ExternalRequestProcessor:Prerequiste1");
      log.debug("Processing ExternalRequestProcessor:Prerequiste1: Score: "+" depth:"+depth+", returning:"+ (decisionEngineRequest instanceof DecisionEngineRequest2));
      prerequisiteResponse.setPassed(decisionEngineRequest instanceof DecisionEngineRequest2);
      return prerequisiteResponse;
    });
  }
  
  @Override
  protected void buildRules() {
    rules.add((decisionEngineRequest, processorResponse) -> {
      RuleResponse ruleResponse = RuleResponse.getInstance("ExternalRequestProcessor:Rule1");
      processorResponse.setScore(processorResponse.getScore() + ((DecisionEngineRequest2)decisionEngineRequest).delinquencies);
      log.debug("Processing ExternalRequestProcessor:Rule1: Score: "+processorResponse.getScore() +", depth:"+depth);
      ((ProcessorResponse2)processorResponse).setApprovedAmount(((DecisionEngineRequest2)decisionEngineRequest).getDelinquencies());
      if (((DecisionEngineRequest2) decisionEngineRequest).getDelinquencies() > 3) {
        processorResponse.setDecision("DECLINED");
      } else {
        processorResponse.setDecision("APPROVED");
      }
      return ruleResponse;
    });
  }
  
  
  @Override
  protected String getProcessorVersion() {
    return PROCESSOR_VERSION;
  }
}
