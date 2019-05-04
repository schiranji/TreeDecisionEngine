package com.techsavy.de.processor;

import java.io.IOException;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
  
  @Override
  protected void buildPrerequistes() {
  }
  
  @Override
  protected void buildRules() {
    rules.add((argDecisionEngineRequest, processorResponse) -> {
      RuleResponse ruleResponse = RuleResponse.getInstance("HttpProcessor:Rule1");
      log.debug("Processing HttpProcessor:Rule1: Url: "+params.get("url"));
      HttpUtil.postRequest(getParam("url"), params, argDecisionEngineRequest);
      processorResponse.setScore(processorResponse.getScore()+1);
      return ruleResponse;
    });
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
