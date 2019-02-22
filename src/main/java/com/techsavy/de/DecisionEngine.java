package com.techsavy.de;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import com.techsavy.de.common.Constants;
import com.techsavy.de.domain.ProcessorResponse;
import com.techsavy.de.domain.DecisionEngineRequest;
import com.techsavy.de.domain.DecisionEngineResponse;
import com.techsavy.de.processor.BaseAbstractProcessor;
import com.techsavy.de.processor.BaseProcessor;
import com.techsavy.de.util.FileUtil;
import com.techsavy.de.util.ObjectUtil;

public class DecisionEngine implements Callable<DecisionEngineResponse>, Constants {
  private static final Logger log = LogManager.getLogger();  
  private static Map<String, Object> processorNamesMap;
  private static int mapSize = 0;
  private DecisionEngineRequest decisionEngineRequest;
  private ProcessorResponse processorResponse;

  static {
    Yaml yaml = new Yaml();
    String configFileName = System.getProperty(PROCESSORS_FILE_PARAM_NAME);
    configFileName = (StringUtils.isNotBlank(configFileName)) ? configFileName : DEFAULT_CONFIG_FILE_NAME;
    InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(configFileName);
    processorNamesMap = yaml.load(inputStream);
    mapSize = (new Long(FileUtil.getLineCount(configFileName))).intValue();
  }
  
  public DecisionEngine(DecisionEngineRequest argDecisionEngineRequest, ProcessorResponse processorResponse) {
    this.decisionEngineRequest = argDecisionEngineRequest;
    this.processorResponse = processorResponse;
  }

  public Map<String, Object> loadProcessorMap(Map<String, Object> argProcessorMap, DecisionEngineRequest argDecisionEngineRequest, ProcessorResponse processorResponse, int depth) throws Exception {
    Map<String, Object> processorMap = new HashMap<String, Object>();
    for (String key : argProcessorMap.keySet()) {
      BaseAbstractProcessor processor = (BaseAbstractProcessor)ObjectUtil.getInstanceByName(key);
      processor.setProcessorData(processor, argDecisionEngineRequest, processorResponse, processorMap, depth);
      if (argProcessorMap.get(key) != null) {
        processorMap.put(key, processor);
      } else {
        argProcessorMap.put(key, processorMap);
      }
    }
    return argProcessorMap;
  }
  
  public DecisionEngineResponse process(DecisionEngineRequest argDecisionEngineRequest, ProcessorResponse processorResponse) {
    ExecutorService executor = null;
    try {
      DecisionEngineResponse decisionEngineResponse = DecisionEngineResponse.getInstance();
      BaseProcessor processor = new BaseProcessor();
      Map<String, Object> map = loadProcessorMap(processorNamesMap, argDecisionEngineRequest, processorResponse, 0);
      processor.setProcessorData(processor, argDecisionEngineRequest, processorResponse, map, 0);
      executor = Executors.newFixedThreadPool(mapSize);
      printMap(map, "");
      List<ProcessorResponse> results = new ArrayList<ProcessorResponse>();
      processor.process(executor, argDecisionEngineRequest, processorResponse, results, map, 0, PROCESSOR_MAX_WAIT_TIME);
      decisionEngineResponse.setProcessorResponses(results);
      decisionEngineResponse.setAuditTime();
      return decisionEngineResponse;
    } catch(Throwable t) {
      t.printStackTrace();
      return null;
    } finally {
      if(executor != null) executor.shutdown();
    }
  }
  
  public DecisionEngineResponse processSequentially(DecisionEngineRequest decisionEngineRequest, ProcessorResponse processorResponse) throws Exception {
    DecisionEngineResponse decisionEngineResponse = DecisionEngineResponse.getInstance();
    BaseProcessor processor = new BaseProcessor();
    Map<String, Object> map = loadProcessorMap(processorNamesMap, decisionEngineRequest, processorResponse, 0);
    processor.setProcessorData(processor, decisionEngineRequest, processorResponse, map, 0);
    printMap(map, "");
    List<ProcessorResponse> results = new ArrayList<ProcessorResponse>();
    processor.processSequentially(decisionEngineRequest, processorResponse, results, map, 0);
    decisionEngineResponse.setProcessorResponses(results);
    decisionEngineResponse.setAuditTime();
    return decisionEngineResponse;
  }
  
  @SuppressWarnings("unchecked")
  private void printMap(Map<String, Object> argProcessorMap, String indentation) {
    for(String key: argProcessorMap.keySet()) {
      Object val = argProcessorMap.get(key);
      log.debug(indentation + key);
      if(val instanceof Map) {
        printMap((Map<String, Object>)val, indentation + "  ");
      }
    }
  }
  
  @Override
  public DecisionEngineResponse call() throws Exception {
    return process(decisionEngineRequest, processorResponse);
  }
}
