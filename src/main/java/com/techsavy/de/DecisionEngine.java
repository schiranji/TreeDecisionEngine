package com.techsavy.de;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import com.techsavy.de.domain.ProcessorResponse;
import com.techsavy.de.domain.RuleEngineRequest;
import com.techsavy.de.domain.RuleEngineResponse;
import com.techsavy.de.processor.BaseAbstractProcessor;
import com.techsavy.de.processor.BaseProcessor;
import com.techsavy.de.util.ObjectUtil;

public class DecisionEngine implements Callable<RuleEngineResponse> {
  private static final Logger log = LogManager.getLogger();
  private static final Logger auditLog = LogManager.getLogger("auditlog");
  private static final int PROCESSOR_MAX_WAIT_TIME = 5;
  private static Map<String, Object> processorNamesMap;
  
  private static final ThreadLocal<RuleEngineRequest> ruleEngineData = new InheritableThreadLocal<RuleEngineRequest>();

  static {
    Yaml yaml = new Yaml();
    InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("processors.yml");
    processorNamesMap = yaml.load(inputStream);
  }
  
  public RuleEngineRequest getRuleEngineRequest() {
    return ruleEngineData.get();
  }

  public static Map<String, Object> loadProcessorMap(Map<String, Object> argProcessorMap, RuleEngineRequest ruleEngineData, int depth) throws Exception {
    Map<String, Object> processorMap = new HashMap<String, Object>();
    for (String key : argProcessorMap.keySet()) {
      BaseAbstractProcessor processor = (BaseAbstractProcessor)ObjectUtil.getInstanceByName(key);
      processor.setProcessorData(processor, ruleEngineData, ProcessorResponse.getInstance(), processorMap, depth);
      if (argProcessorMap.get(key) != null) {
        processorMap.put(key, processor);
      } else {
        argProcessorMap.put(key, processorMap);
      }
    }
    return argProcessorMap;
  }
  
  private static RuleEngineResponse process() {
    return process(ruleEngineData.get());
  }
  
  private static RuleEngineResponse process(RuleEngineRequest ruleEngineRequest) {
    ExecutorService executor = null;
    try {
      ruleEngineData.set(ruleEngineRequest);
      RuleEngineResponse ruleEngineResponse = RuleEngineResponse.getInstance();
      ProcessorResponse processorResponse = ProcessorResponse.getInstance();
      BaseProcessor processor = new BaseProcessor();
      Map<String, Object> map = loadProcessorMap(processorNamesMap, ruleEngineRequest, 0);
      processor.setProcessorData(processor, ruleEngineRequest, processorResponse, map, 0);
      setMapSize(map);
      executor = Executors.newFixedThreadPool(mapSize);
      printMap(map, "");
      List<ProcessorResponse> results = new ArrayList<ProcessorResponse>();
      processor.process(executor, ruleEngineRequest, processorResponse, results, map, 0, PROCESSOR_MAX_WAIT_TIME);
      ruleEngineResponse.setProcessorResponses(results);
      ruleEngineResponse.setAuditTime();
      return ruleEngineResponse;
    } catch(Throwable t) {
      t.printStackTrace();
      return null;
    } finally {
      if(executor != null) executor.shutdown();
    }
  }
  
  private static RuleEngineResponse processSequentially(RuleEngineRequest ruleEngineRequest) throws Exception {
    RuleEngineResponse ruleEngineResponse = RuleEngineResponse.getInstance();
    ProcessorResponse processorResponse = ProcessorResponse.getInstance();
    BaseProcessor processor = new BaseProcessor();
    Map<String, Object> map = loadProcessorMap(processorNamesMap, ruleEngineRequest, 0);
    processor.setProcessorData(processor, ruleEngineRequest, processorResponse, map, 0);
    setMapSize(map);
    printMap(map, "");
    List<ProcessorResponse> results = new ArrayList<ProcessorResponse>();
    processor.processSequentially(ruleEngineRequest, processorResponse, results, map, 0);
    ruleEngineResponse.setProcessorResponses(results);
    ruleEngineResponse.setAuditTime();
    return ruleEngineResponse;
  }
  
  private static int mapSize = 0;
  @SuppressWarnings("unchecked")
  private static int setMapSize(Map<String, Object> argProcessorMap) {
    for(String key: argProcessorMap.keySet()) {
      Object val = argProcessorMap.get(key);
      mapSize++;
      if(val instanceof Map) {
        setMapSize((Map<String, Object>)val);
      }
    }
    return mapSize;
  }
  
  @SuppressWarnings("unchecked")
  private static void printMap(Map<String, Object> argProcessorMap, String indentation) {
    for(String key: argProcessorMap.keySet()) {
      Object val = argProcessorMap.get(key);
      log.debug(indentation + key);
      if(val instanceof Map) {
        printMap((Map<String, Object>)val, indentation + "  ");
      }
    }
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
  
  private static void testInvokeMultithread(RuleEngineRequest ruleEngineRequest, int threadCount) {
    long startTime = System.currentTimeMillis();
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);;
    ruleEngineData.set(ruleEngineRequest);
    List<Future<RuleEngineResponse>> responseList = new ArrayList<Future<RuleEngineResponse>>();
    for(int i=0;i<threadCount;i++) {
      try {
        DecisionEngine de = new DecisionEngine();
        Future<RuleEngineResponse> ruleEngineResponse = executor.submit(de);
        responseList.add(ruleEngineResponse);
        System.out.println("Processing thread. " + i);
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
    for(int i=0;i<threadCount;i++) {
      RuleEngineResponse ruleEngineResponse;
      try {
        ruleEngineResponse = processSequentially(ruleEngineRequest);
        List<ProcessorResponse> resultsSeq = ruleEngineResponse.getProcessorResponses();
        auditLog.info("Single Threading time(millis):"+ruleEngineResponse.getAudit().getTimespan());
      } catch (Exception e) {
        log.error("Error while processing",  e);
      }
    }
    System.out.println("******************Processing Time SingleThreading:"+(System.currentTimeMillis()-startTime));
  }
  
  public static void main(String[] args) throws Exception {
    System.out.println("Started...");
    RuleEngineRequest ruleEngineRequest = new RuleEngineRequest();
    testInvokeMultithread(ruleEngineRequest, 100);
    testInvokeSinglethread(ruleEngineRequest, 100);
    
    //RuleEngineResponse ruleEngineResponse = process(ruleEngineRequest);
    //List<ProcessorResponse> results = ruleEngineResponse.getProcessorResponses();
    //auditLog.info("Milti Threading time(millis):"+ruleEngineResponse.getAudit().getTimespan());
    //printResults(results);
    //ruleEngineResponse = processSequentially(ruleEngineRequest);
    //List<ProcessorResponse> resultsSeq = ruleEngineResponse.getProcessorResponses();
    //auditLog.info("Single Threading time(millis):"+ruleEngineResponse.getAudit().getTimespan());
    //printResults(resultsSeq);
    System.out.println("Done...");
  }

  @Override
  public RuleEngineResponse call() throws Exception {
    return process();
  }
}
