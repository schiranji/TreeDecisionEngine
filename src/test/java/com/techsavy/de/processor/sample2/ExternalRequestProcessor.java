package com.techsavy.de.processor.sample2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.techsavy.de.domain.PrerequisiteResponse;
import com.techsavy.de.domain.RuleResponse;
import com.techsavy.de.processor.BaseProcessor;
import com.techsavy.de.processor.sample2.domain.ProcessorResponse2;
import com.techsavy.de.processor.sample2.domain.RuleEngineRequest2;

public class ExternalRequestProcessor extends BaseProcessor {

  private static final Logger log = LogManager.getLogger();
  
  @Override
  protected void buildPrerequistes() {
    prerequisites.add((ruleEngineRequest) -> {
      PrerequisiteResponse prerequisiteResponse = PrerequisiteResponse.getInstance("ExternalRequestProcessor:Prerequiste1");
      log.debug("Processing ExternalRequestProcessor:Prerequiste1: Score: "+" depth:"+depth+", returning:"+ (ruleEngineRequest instanceof RuleEngineRequest2));
      prerequisiteResponse.setPassed(ruleEngineRequest instanceof RuleEngineRequest2);
      return prerequisiteResponse;
    });
  }
  
  @Override
  protected void buildRules() {
    rules.add((ruleEngineRequest, ruleEngineResponse) -> {
      RuleResponse ruleResponse = RuleResponse.getInstance("ExternalRequestProcessor:Rule1");
      ruleEngineResponse.setScore(ruleEngineResponse.getScore() + ((RuleEngineRequest2)ruleEngineRequest).delinquencies);
      log.debug("Processing ExternalRequestProcessor:Rule1: Score: "+ruleEngineResponse.getScore() +", depth:"+depth);
      ((ProcessorResponse2)ruleEngineResponse).setApprovedAmount(2000);
      ruleEngineResponse.setDecision("DECLINED");
      return ruleResponse;
    });
  }
  
}
