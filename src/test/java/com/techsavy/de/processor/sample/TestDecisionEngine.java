package com.techsavy.de.processor.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.techsavy.de.DecisionEngine;
import com.techsavy.de.domain.DecisionEngineRequest;
import com.techsavy.de.domain.DecisionEngineResponse;
import com.techsavy.de.domain.ProcessorResponse;
import com.techsavy.de.util.LogUtil;
import com.techsavy.de.util.ObjectUtil;

public class TestDecisionEngine {
  private static final Logger log = LogManager.getLogger();

  private static void testInvokeMultithread(DecisionEngineRequest decisionEngineRequest, int threadCount) {
    long startTime = System.currentTimeMillis();
    ExecutorService executor = ObjectUtil.getExecutor(threadCount);
    try {
      List<Future<DecisionEngineResponse>> responseList = new ArrayList<Future<DecisionEngineResponse>>();
      for (int i = 0; i < threadCount; i++) {
        decisionEngineRequest.setTestCounter(i);
        try {
          DecisionEngine de = new DecisionEngine(decisionEngineRequest, new ProcessorResponse());
          Future<DecisionEngineResponse> decisionEngineResponse = executor.submit(de);
          responseList.add(decisionEngineResponse);
        } catch (Exception e) {
          log.error("Error while processing", e);
        }
      }
      for (Future<DecisionEngineResponse> futResponse : responseList) {
        try {
          LogUtil.logObject(log, decisionEngineRequest);
          LogUtil.logObject(log, futResponse.get());
        } catch (InterruptedException | ExecutionException e) {
          log.error("Error while processing", e);
        }
      }
    } finally {
      LogUtil.logAuditTimeMillis("Milti Threading time(millis):", startTime);
      executor.shutdown();
    }
    System.out.println("Processing Time MultiThreading:" + (System.currentTimeMillis() - startTime));
  }

  private static void testInvokeSinglethread(DecisionEngineRequest decisionEngineRequest, int threadCount) {
    long startTime = System.currentTimeMillis();
    DecisionEngine de = new DecisionEngine(decisionEngineRequest, new ProcessorResponse());
    for (int i = 0; i < threadCount; i++) {
      decisionEngineRequest.setTestCounter(i);
      DecisionEngineResponse decisionEngineResponse;
      try {
        decisionEngineResponse = de.processSequentially();
        LogUtil.logObject(log, decisionEngineRequest);
        LogUtil.logObject(log, decisionEngineResponse);
      } catch (Exception e) {
        log.error("Error while processing", e);
      }
    }
    System.out.println("Processing Time SingleThreading:" + (System.currentTimeMillis() - startTime));
  }

  public static void main(String[] args) throws Exception {
    System.out.println("Started...");
    DecisionEngineRequest decisionEngineRequest = new DecisionEngineRequest();
    testInvokeMultithread(decisionEngineRequest, 5000);
    testInvokeSinglethread(decisionEngineRequest, 5000);
    System.out.println("Done...");
  }
}
