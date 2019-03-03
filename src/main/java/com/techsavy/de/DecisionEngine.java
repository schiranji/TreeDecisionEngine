package com.techsavy.de;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import com.techsavy.de.common.Constants;
import com.techsavy.de.domain.DecisionEngineRequest;
import com.techsavy.de.domain.DecisionEngineResponse;
import com.techsavy.de.domain.DecisionEngineScope;
import com.techsavy.de.domain.ProcessorResponse;
import com.techsavy.de.processor.BaseAbstractProcessor;
import com.techsavy.de.processor.BaseProcessor;
import com.techsavy.de.util.FileUtil;
import com.techsavy.de.util.LogUtil;
import com.techsavy.de.util.ObjectUtil;

public class DecisionEngine implements Callable<DecisionEngineResponse>, Constants {
  private static final Logger log = LogManager.getLogger();
  private static Map<String, Object> processorNamesMap;
  private static int MAX_THREADPOOL_SIZE = 20;
  private static int mapSize = 0;
  private ProcessorResponse processorResponse;

  static {
    Yaml yaml = new Yaml();
    String configFileName = System.getProperty(PROCESSORS_FILE_PARAM_NAME);
    configFileName = (StringUtils.isNotBlank(configFileName)) ? configFileName : DEFAULT_PROCESSOR_CONFIG_FILE_NAME;
    try (
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(configFileName);) {
      processorNamesMap = yaml.load(inputStream);
      LogUtil.logYml(log, processorNamesMap);
      mapSize = (new Long(FileUtil.getLineCount(configFileName))).intValue();
    } catch (IOException e) {
      log.error("Error while loading processors file.", e);
    }
  }

  public DecisionEngine(DecisionEngineRequest argDecisionEngineRequest, ProcessorResponse processorResponse) {
    this.processorResponse = processorResponse;
    DecisionEngineScope.setDecisionEngineRequest(argDecisionEngineRequest);
  }

  public Map<String, Object> loadProcessorMap(Map<String, Object> argProcessorMap,
      ProcessorResponse processorResponse, int depth) throws Exception {
    Map<String, Object> processorMap = new HashMap<String, Object>();
    for (String key : argProcessorMap.keySet()) {
      BaseAbstractProcessor processor = (BaseAbstractProcessor) ObjectUtil.getInstanceByName(key);
      processor.setProcessorData(processor, processorResponse, processorMap, depth);
      if (argProcessorMap.get(key) != null) {
        processorMap.put(key, processor);
      } else {
        argProcessorMap.put(key, processorMap);
      }
    }
    return argProcessorMap;
  }

  public DecisionEngineResponse process() {
    long startTime = System.currentTimeMillis();
    try {
      DecisionEngineResponse decisionEngineResponse = DecisionEngineResponse.getInstance();
      Map<String, Object> map = loadProcessorMap(processorNamesMap, processorResponse, 0);
      initExecutor();
      BaseProcessor processor = new BaseProcessor();
      processor.setProcessorData(processor, processorResponse, map, 0);
      List<ProcessorResponse> results = new ArrayList<ProcessorResponse>();
      processor.process(processorResponse, results, map, 0, PROCESSOR_MAX_WAIT_TIME);
      decisionEngineResponse.setProcessorResponses(results);
      decisionEngineResponse.setAuditTime();
      LogUtil.logAuditTimeMillis("Decision Engine Response time(millis):", startTime);
      return decisionEngineResponse;
    } catch (Throwable t) {
      t.printStackTrace();
      return null;
    } finally {
      DecisionEngineScope.cleanScope();
    }
  }

  private void initExecutor() {
    ExecutorService executor = ObjectUtil.getExecutor(getMaxThreadCount());
    DecisionEngineScope.setExecutorService(executor);
  }

  private int getMaxThreadCount() {
    return (mapSize > MAX_THREADPOOL_SIZE) ? MAX_THREADPOOL_SIZE : mapSize;
  }

  public DecisionEngineResponse processSequentially() throws Exception {
    try {
      DecisionEngineResponse decisionEngineResponse = DecisionEngineResponse.getInstance();
      BaseProcessor processor = new BaseProcessor();
      Map<String, Object> map = loadProcessorMap(processorNamesMap, processorResponse, 0);
      processor.setProcessorData(processor, processorResponse, map, 0);
      List<ProcessorResponse> results = new ArrayList<ProcessorResponse>();
      processor.processSequentially(processorResponse, results, map, 0);
      decisionEngineResponse.setProcessorResponses(results);
      decisionEngineResponse.setAuditTime();
      return decisionEngineResponse;
    } finally {
      DecisionEngineScope.cleanScope();
    }
  }

  @Override
  public DecisionEngineResponse call() throws Exception {
    return process();
  }
}
