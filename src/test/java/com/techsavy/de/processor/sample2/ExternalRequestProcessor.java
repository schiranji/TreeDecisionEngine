package com.techsavy.de.processor.sample2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.techsavy.de.processor.BaseProcessor;
import com.techsavy.de.processor.sample2.domain.RuleEngineRequest2;

public class ExternalRequestProcessor extends BaseProcessor {

  private static final Logger log = LogManager.getLogger();
  
  @Override
  protected void buildPrerequistes() {
    prerequisites.add((ruleEngineRequest) -> {
      log.debug("Processing ExternalRequestProcessor:Prerequiste1: Score: "+" depth:"+depth+", returning:"+ (ruleEngineRequest instanceof RuleEngineRequest2));
      return (ruleEngineRequest instanceof RuleEngineRequest2);
    });
  }
  
  @Override
  protected void buildRules() {
    rules.add((ruleEngineRequest, ruleEngineResponse) -> 
    { 
      ruleEngineResponse.setScore(ruleEngineResponse.getScore() + ((RuleEngineRequest2)ruleEngineRequest).delinquencies);
      log.debug("Processing ExternalRequestProcessor:Rule1: Score: "+ruleEngineResponse.getScore() +", depth:"+depth); 
      ruleEngineResponse.setDecision("DECLINED");
    });
  }
  
}
