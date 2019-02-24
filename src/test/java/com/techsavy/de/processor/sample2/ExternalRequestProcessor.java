package com.techsavy.de.processor.sample2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.techsavy.de.domain.PrerequisiteResponse;
import com.techsavy.de.domain.RuleResponse;
import com.techsavy.de.processor.BaseProcessor;
import com.techsavy.de.processor.sample2.domain.DecisionEngineRequest2;
import com.techsavy.de.processor.sample2.domain.ProcessorResponse2;

public class ExternalRequestProcessor extends BaseProcessor {

  private static final Logger log = LogManager.getLogger();
  
  @Override
  protected void buildPrerequistes() {
    prerequisites.add((decisionEngineRequest) -> {
      PrerequisiteResponse prerequisiteResponse = PrerequisiteResponse.getInstance("ExternalRequestProcessor:Prerequiste1");
      try { Thread.sleep(1000);} catch (InterruptedException e) { e.printStackTrace();}
      log.debug("Processing ExternalRequestProcessor:Prerequiste1: Score: "+" depth:"+depth+", returning:"+ (decisionEngineRequest instanceof DecisionEngineRequest2));
      prerequisiteResponse.setPassed(decisionEngineRequest instanceof DecisionEngineRequest2);
      return prerequisiteResponse;
    });
  }
  
  @Override
  protected void buildRules() {
    rules.add((decisionEngineRequest, processorResponse) -> {
      RuleResponse ruleResponse = RuleResponse.getInstance("ExternalRequestProcessor:Rule1");
      try { Thread.sleep(1000);} catch (InterruptedException e) { e.printStackTrace();}
      processorResponse.setScore(processorResponse.getScore() + ((DecisionEngineRequest2)decisionEngineRequest).delinquencies);
      log.debug("Processing ExternalRequestProcessor:Rule1: Score: "+processorResponse.getScore() +", depth:"+depth);
      ((ProcessorResponse2)processorResponse).setApprovedAmount(2000);
      processorResponse.setDecision("DECLINED");
      return ruleResponse;
    });
  }
  
}
