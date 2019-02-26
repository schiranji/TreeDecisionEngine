package com.techsavy.de;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.techsavy.de.domain.DecisionEngineRequest;
import com.techsavy.de.domain.DecisionEngineResponse;
import com.techsavy.de.domain.ProcessorResponse;
import com.techsavy.de.util.LogUtil;
import com.techsavy.de.util.ObjectUtil;

public class Test1DecisionEngine {
  private static final Logger log = LogManager.getLogger();

  private static void testInvokeMultithread(DecisionEngineRequest decisionEngineRequest, int threadCount) {
    long startTime = System.currentTimeMillis();
    ExecutorService executor = ObjectUtil.getExecutor(threadCount);

    try {
      List<Future<DecisionEngineResponse>> responseList = new ArrayList<Future<DecisionEngineResponse>>();
      for (int i = 0; i < threadCount; i++) {
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
          LogUtil.logObject(log, futResponse.get());
          LogUtil.logAuditTimeMillis("Milti Threading time(millis):", startTime);
          LogUtil.logObject(log, decisionEngineRequest);
        } catch (InterruptedException | ExecutionException e) {
          log.error("Error while processing", e);
        }
      }
    } finally {
      executor.shutdown();
    }
    System.out.println("Processing Time MultiThreading:" + (System.currentTimeMillis() - startTime));
  }

  private static void testInvokeSinglethread(DecisionEngineRequest decisionEngineRequest, int threadCount) {
    long startTime = System.currentTimeMillis();
    DecisionEngine de = new DecisionEngine(decisionEngineRequest, new ProcessorResponse());
    for (int i = 0; i < threadCount; i++) {
      DecisionEngineResponse decisionEngineResponse;
      try {
        decisionEngineResponse = de.processSequentially();
        LogUtil.logObject(log, decisionEngineResponse);
        LogUtil.logObject(log, decisionEngineRequest);
      } catch (Exception e) {
        log.error("Error while processing", e);
      }
    }
    System.out.println("Processing Time SingleThreading:" + (System.currentTimeMillis() - startTime));
  }

  public static void main(String[] args) throws Exception {
    System.out.println("Started...");
    DecisionEngineRequest decisionEngineRequest = new DecisionEngineRequest();
    testInvokeMultithread(decisionEngineRequest, 2);
    testInvokeSinglethread(decisionEngineRequest, 2);
    System.out.println("Done...");
  }
}
