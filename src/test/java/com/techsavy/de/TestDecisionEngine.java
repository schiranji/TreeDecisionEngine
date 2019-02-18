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
import com.techsavy.de.domain.RuleEngineRequest;
import com.techsavy.de.domain.RuleEngineResponse;

public class TestDecisionEngine {
  private static final Logger log = LogManager.getLogger();
  private static final Logger auditLog = LogManager.getLogger("auditlog");
  private static void testInvokeMultithread(RuleEngineRequest ruleEngineRequest, int threadCount) {
    long startTime = System.currentTimeMillis();
    DecisionEngine decisionEngine = new DecisionEngine();
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);;
    decisionEngine.setRuleEngineRequest(ruleEngineRequest);
    List<Future<RuleEngineResponse>> responseList = new ArrayList<Future<RuleEngineResponse>>();
    for(int i=0;i<threadCount;i++) {
      try {
        DecisionEngine de = new DecisionEngine();
        de.setRuleEngineRequest(ruleEngineRequest);
        Future<RuleEngineResponse> ruleEngineResponse = executor.submit(de);
        responseList.add(ruleEngineResponse);
        //log.debug("Processing thread. " + i);
      } catch (Exception e) {
        log.error("Error while processing",  e);
      }
    }
    for(Future<RuleEngineResponse> futResponse: responseList) {
      try {
        auditLog.info("Milti Threading time(millis):"+futResponse.get().getAudit().getTimespan());
      } catch (InterruptedException | ExecutionException e) {
        log.error("Error while processing",  e);
      }      
    }
    System.out.println("******************Processing Time MultiThreading:"+(System.currentTimeMillis()-startTime));
  }
  
  private static void testInvokeSinglethread(RuleEngineRequest ruleEngineRequest, int threadCount) {
    long startTime = System.currentTimeMillis();
    DecisionEngine de = new DecisionEngine();
    for(int i=0;i<threadCount;i++) {
      RuleEngineResponse ruleEngineResponse;
      try {
        ruleEngineResponse = de.processSequentially(ruleEngineRequest);
        List<ProcessorResponse> resultsSeq = ruleEngineResponse.getProcessorResponses();
        printResults(resultsSeq);
        auditLog.info("Single Threading time(millis):"+ruleEngineResponse.getAudit().getTimespan());
      } catch (Exception e) {
        log.error("Error while processing",  e);
      }
    }
    System.out.println("******************Processing Time SingleThreading:"+(System.currentTimeMillis()-startTime));
  }
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
    RuleEngineRequest ruleEngineRequest = new RuleEngineRequest();
    testInvokeMultithread(ruleEngineRequest, 100);
    testInvokeSinglethread(ruleEngineRequest, 100);
    
    //RuleEngineResponse ruleEngineResponse = DecisionEngine.process(ruleEngineRequest);
    //List<ProcessorResponse> results = ruleEngineResponse.getProcessorResponses();
    //auditLog.info("Milti Threading time(millis):"+ruleEngineResponse.getAudit().getTimespan());
    //printResults(results);
    //ruleEngineResponse = DecisionEngine.processSequentially(ruleEngineRequest);
    //List<ProcessorResponse> resultsSeq = ruleEngineResponse.getProcessorResponses();
    //auditLog.info("Single Threading time(millis):"+ruleEngineResponse.getAudit().getTimespan());
    //printResults(resultsSeq);
    System.out.println("Done...");
  }
}
