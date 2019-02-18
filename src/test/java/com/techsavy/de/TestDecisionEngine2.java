package com.techsavy.de;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.techsavy.de.domain.ProcessorResponse;
import com.techsavy.de.domain.RuleEngineResponse;
import com.techsavy.de.processor.sample2.domain.RuleEngineRequest2;

public class TestDecisionEngine2 {
  private static final Logger log = LogManager.getLogger();
  private static final Logger auditLog = LogManager.getLogger("auditlog");
  private static void printResults(List<ProcessorResponse> results) {
    if(results != null) {
      log.debug("**** Results Start ***");
      for(ProcessorResponse result : results) {
        log.debug("Processor:"+result.getProcessor()+", Decision:"+result.getDecision()+", Score:"+result.getScore());
      }
      log.debug("**** Results End ***");
    }
  }
  
  public static void main(String[] args) throws Exception {
    System.out.println("Started...");
    DecisionEngine de = new DecisionEngine();
    RuleEngineRequest2 ruleEngineRequest = new RuleEngineRequest2();
    RuleEngineResponse ruleEngineResponse = de.process(ruleEngineRequest);
    List<ProcessorResponse> results = ruleEngineResponse.getProcessorResponses();
    auditLog.info("Milti Threading time(millis):"+ruleEngineResponse.getAudit().getTimespan());
    printResults(results);
    ruleEngineResponse = de.processSequentially(ruleEngineRequest);
    List<ProcessorResponse> resultsSeq = ruleEngineResponse.getProcessorResponses();
    auditLog.info("Single Threading time(millis):"+ruleEngineResponse.getAudit().getTimespan());
    printResults(resultsSeq);
    System.out.println("Done...");
  }
}
