package com.techsavy.de.domain;

import java.util.List;

import com.techsavy.de.DecisionEngine;

public class DecisionEngineResponse extends ResponseAbstract {
  private static final String AUDIT_DECISION_ENGINE_TYPE = "DecisionEngine";
  private static final long serialVersionUID = -5054894114864253976L;
  List<ProcessorResponse> processorResponses;

  private DecisionEngineResponse() { 
  }
  
  public static DecisionEngineResponse getInstance() {
    DecisionEngineResponse decisionEngineResponse = new DecisionEngineResponse();
    decisionEngineResponse.setAudit(Audit.getInstance(AUDIT_DECISION_ENGINE_TYPE, DecisionEngine.class.getName()));
    return decisionEngineResponse;
  }
  
  public List<ProcessorResponse> getProcessorResponses() {
    return processorResponses;
  }

  public void setProcessorResponses(List<ProcessorResponse> processorResponses) {
    this.processorResponses = processorResponses;
  }

  @Override
  public String getType() {
    return AUDIT_DECISION_ENGINE_TYPE;
  }
}
