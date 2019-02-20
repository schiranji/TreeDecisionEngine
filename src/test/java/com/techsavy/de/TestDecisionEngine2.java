package com.techsavy.de;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.techsavy.de.domain.ProcessorResponse;
import com.techsavy.de.domain.DecisionEngineResponse;
import com.techsavy.de.processor.sample2.domain.ProcessorResponse2;
import com.techsavy.de.processor.sample2.domain.RuleEngineRequest2;
import com.techsavy.de.util.LogUtil;

public class TestDecisionEngine2 {
  private static final Logger log = LogManager.getLogger();
  private static final Logger auditLog = LogManager.getLogger("auditlog");



  public static void main(String[] args) throws Exception {
    System.out.println("Started...");
    RuleEngineRequest2 ruleEngineRequest = new RuleEngineRequest2();
    ProcessorResponse2 processorResponse2 = ProcessorResponse2.getInstance();
    DecisionEngine de = new DecisionEngine(ruleEngineRequest, processorResponse2);
    DecisionEngineResponse ruleEngineResponse = de.process(ruleEngineRequest, processorResponse2);
    List<ProcessorResponse> results = ruleEngineResponse.getProcessorResponses();
    auditLog.info("Milti Threading time(millis):" + ruleEngineResponse.getAudit().getTimespan());
    LogUtil.logObject(log, results);
    ruleEngineResponse = de.processSequentially(ruleEngineRequest, processorResponse2);
    List<ProcessorResponse> resultsSeq = ruleEngineResponse.getProcessorResponses();
    auditLog.info("Single Threading time(millis):" + ruleEngineResponse.getAudit().getTimespan());
    LogUtil.logObject(log, resultsSeq);
    System.out.println("Done...");
  }
}
