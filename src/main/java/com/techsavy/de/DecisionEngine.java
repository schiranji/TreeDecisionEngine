package com.techsavy.de;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.yaml.snakeyaml.Yaml;

import com.techsavy.de.domain.RuleEngineRequest;
import com.techsavy.de.domain.ProcessorResponse;
import com.techsavy.de.processor.BaseAbstractProcessor;
import com.techsavy.de.processor.BaseProcessor;
import com.techsavy.de.util.ObjectUtil;

public class DecisionEngine {
  private static final int PROCESSOR_MAX_WAIT_TIME = 5;
  private static Map<String, Object> processorNamesMap;
  static {
    Yaml yaml = new Yaml();
    InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("processors.yml");
    processorNamesMap = yaml.load(inputStream);
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
  
  private static List<ProcessorResponse> process(RuleEngineRequest ruleData) {
    ExecutorService executor = null;
    try {
      ProcessorResponse result = ProcessorResponse.getInstance();
      BaseProcessor processor = new BaseProcessor();
      Map<String, Object> map = loadProcessorMap(processorNamesMap, ruleData, 0);
      processor.setProcessorData(processor, ruleData, result, map, 0);
      setMapSize(map);
      executor = Executors.newFixedThreadPool(mapSize);
      printMap(map, "");
      List<ProcessorResponse> results = new ArrayList<ProcessorResponse>();
      processor.process(executor, ruleData, result, results, map, 0, PROCESSOR_MAX_WAIT_TIME);
      return results;
    } catch(Throwable t) {
      t.printStackTrace();
      return null;
    } finally {
      if(executor != null) executor.shutdown();
    }
  }
  
  private static List<ProcessorResponse> processSequentially(RuleEngineRequest ruleData) throws Exception {
    ProcessorResponse result = ProcessorResponse.getInstance();
    BaseProcessor processor = new BaseProcessor();
    Map<String, Object> map = loadProcessorMap(processorNamesMap, ruleData, 0);
    processor.setProcessorData(processor, ruleData, result, map, 0);
    setMapSize(map);
    printMap(map, "");
    List<ProcessorResponse> results = new ArrayList<ProcessorResponse>();
    processor.processSequentially(ruleData, result, results, map, 0);
    return results;
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
      //System.out.println(indentation + key);
      if(val instanceof Map) {
        printMap((Map<String, Object>)val, indentation + "  ");
      }
    }
  }
  
  private static void printResults(List<ProcessorResponse> results) {
    if(results != null) {
      System.out.println("**** Results Start ***");
      for(ProcessorResponse result : results) {
        System.out.println("Processor:"+result.getProcessor()+", Decision:"+result.getDecision()+", Score:"+result.getScore());
      }
      System.out.println("**** Results End ***");
    }
  }
  
  public static void main(String[] args) throws Exception {
    RuleEngineRequest ruleData = new RuleEngineRequest();
    long startTime = System.currentTimeMillis();
    List<ProcessorResponse> results = process(ruleData);
    System.out.println("Milti Threading time(millis):"+(System.currentTimeMillis()-startTime));
    printResults(results);
    startTime = System.currentTimeMillis();
    List<ProcessorResponse> resultsSeq = processSequentially(ruleData);
    System.out.println("Single Threading time(millis):"+(System.currentTimeMillis()-startTime));
    printResults(resultsSeq);
  }
}
