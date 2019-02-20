package com.techsavy.de;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.techsavy.de.domain.ProcessorResponse;
import com.techsavy.de.domain.DecisionEngineRequest;
import com.techsavy.de.domain.DecisionEngineResponse;
import com.techsavy.de.processor.sample2.domain.ProcessorResponse2;
import com.techsavy.de.util.LogUtil;

public class TestDecisionEngine {
  private static final Logger log = LogManager.getLogger();
  private static final Logger auditLog = LogManager.getLogger("auditlog");

  private static void testInvokeMultithread(DecisionEngineRequest ruleEngineRequest, int threadCount) {
    long startTime = System.currentTimeMillis();
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    List<Future<DecisionEngineResponse>> responseList = new ArrayList<Future<DecisionEngineResponse>>();
    for (int i = 0; i < threadCount; i++) {
      try {
        DecisionEngine de = new DecisionEngine(ruleEngineRequest, ProcessorResponse.getInstance());
        Future<DecisionEngineResponse> ruleEngineResponse = executor.submit(de);
        responseList.add(ruleEngineResponse);
      } catch (Exception e) {
        log.error("Error while processing", e);
      }
    }
    for (Future<DecisionEngineResponse> futResponse : responseList) {
      try {
        LogUtil.printResults(log, futResponse.get().getProcessorResponses());
        auditLog.info("Milti Threading time(millis):" + futResponse.get().getAudit().getTimespan());
      } catch (InterruptedException | ExecutionException e) {
        log.error("Error while processing", e);
      }
    }
    System.out.println("Processing Time MultiThreading:" + (System.currentTimeMillis() - startTime));
  }

  private static void testInvokeSinglethread(DecisionEngineRequest ruleEngineRequest, int threadCount) {
    long startTime = System.currentTimeMillis();
    DecisionEngine de = new DecisionEngine(ruleEngineRequest, ProcessorResponse.getInstance());
    for (int i = 0; i < threadCount; i++) {
      DecisionEngineResponse ruleEngineResponse;
      try {
        ruleEngineResponse = de.processSequentially(ruleEngineRequest, ProcessorResponse2.getInstance());
        List<ProcessorResponse> resultsSeq = ruleEngineResponse.getProcessorResponses();
        LogUtil.printResults(log, resultsSeq);
        auditLog.info("Single Threading time(millis):" + ruleEngineResponse.getAudit().getTimespan());
      } catch (Exception e) {
        log.error("Error while processing", e);
      }
    }
    System.out.println("Processing Time SingleThreading:" + (System.currentTimeMillis() - startTime));
  }

  public static void main(String[] args) throws Exception {
    System.out.println("Started...");
    DecisionEngineRequest ruleEngineRequest = new DecisionEngineRequest();
    testInvokeMultithread(ruleEngineRequest, 1);
    testInvokeSinglethread(ruleEngineRequest, 1);
    System.out.println("Done...");
  }
}
