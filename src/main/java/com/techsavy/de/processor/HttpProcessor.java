package com.techsavy.de.processor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.techsavy.de.domain.DecisionEngineRequest;
import com.techsavy.de.domain.ProcessorResponse;
import com.techsavy.de.domain.RuleResponse;
import com.techsavy.de.util.FileUtil;
import com.techsavy.de.util.HttpUtil;
import com.techsavy.de.util.JsonUtil;

public class HttpProcessor extends BaseProcessor {
  private static final Logger log = LogManager.getLogger();
  private static final String PROCESSOR_VERSION = "1.0.0";
  private String fileName;
  private String fileContents;
  private HashMap<String, String> params;
  List<ProcessorResponse> processorResponses;
  
  @Override
  protected void buildPrerequistes() {
  }
  
  @Override
  protected void buildRules() {
    rules.add((argDecisionEngineRequest, processorResponse) -> {
      RuleResponse ruleResponse = RuleResponse.getInstance("HttpProcessor:Rule1");
      log.debug("Processing HttpProcessor:Rule1: Url: "+params.get("url"));
      String requestBody = transformRequest(argDecisionEngineRequest);
      String response = HttpUtil.postRequest(getParam("url"), params, requestBody);
      processorResponses = transformResult(response);
      processorResponse.setScore(processorResponse.getScore()+1);
      return ruleResponse;
    });
  }
  
  private String transformRequest(DecisionEngineRequest argDecisionEngineRequest) {
    try {
      return JsonUtil.getJson(argDecisionEngineRequest);
    } catch (IOException e) {
      log.error("Error while tranforming request. " + argDecisionEngineRequest.getClass().getName(), e);
    }
    return null;
  }

  private List<ProcessorResponse> transformResult(String response) {
    // TODO write trasform from response to List of ProcessorResponses
    return new ArrayList<ProcessorResponse>();
  }

  @Override
  protected String getProcessorVersion() {
    return PROCESSOR_VERSION;
  }

  public void setParams(String fileName) {
    this.fileName = fileName;
    try {
      fileContents = FileUtil.readFromFile(this.fileName);
      params = JsonUtil.getMap(fileContents);
    } catch (IOException e) {
      log.error("Error while setting parameters.", e);
    }
  }
  
  public String getParam(String paramName) {
    return (String)params.get(paramName);
  }
}
