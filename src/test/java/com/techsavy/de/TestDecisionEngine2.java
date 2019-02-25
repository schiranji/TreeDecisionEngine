package com.techsavy.de;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.techsavy.de.domain.DecisionEngineResponse;
import com.techsavy.de.processor.sample2.domain.DecisionEngineRequest2;
import com.techsavy.de.processor.sample2.domain.ProcessorResponse2;
import com.techsavy.de.util.LogUtil;

public class TestDecisionEngine2 {
  private static final Logger log = LogManager.getLogger();
  private static final Logger auditLog = LogManager.getLogger("auditlog");



  public static void main(String[] args) throws Exception {
    System.out.println("Started...");
    DecisionEngineRequest2 decisionEngineRequest = new DecisionEngineRequest2();
    ProcessorResponse2 processorResponse2 = ProcessorResponse2.getInstance();
    DecisionEngine de = new DecisionEngine(decisionEngineRequest, processorResponse2);
    DecisionEngineResponse decisionEngineResponse = de.process();
    auditLog.info("Milti Threading time(millis):" + decisionEngineResponse.getAudit().getTimespan());
    LogUtil.logObject(log, decisionEngineResponse);
    decisionEngineResponse = de.processSequentially();
    auditLog.info("Single Threading time(millis):" + decisionEngineResponse.getAudit().getTimespan());
    LogUtil.logObject(log, decisionEngineResponse);
    System.out.println("Done...");
  }
}
