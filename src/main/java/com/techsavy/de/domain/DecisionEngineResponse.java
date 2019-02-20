package com.techsavy.de.domain;

import java.util.List;

import com.techsavy.de.DecisionEngine;

public class DecisionEngineResponse implements Response {

  private static final long serialVersionUID = -5054894114864253976L;
  List<ProcessorResponse> processorResponses;
  Audit audit;

  private DecisionEngineResponse() { 
  }
  
  public static DecisionEngineResponse getInstance() {
    Audit audit = new Audit();
    audit.setStartTime(System.currentTimeMillis());
    audit.setType("DecisionEngine");
    audit.setName(DecisionEngine.class.getName());
    DecisionEngineResponse ruleEngineResponse = new DecisionEngineResponse();
    ruleEngineResponse.setAudit(audit);
    return ruleEngineResponse;
  }
  
  public List<ProcessorResponse> getProcessorResponses() {
    return processorResponses;
  }

  public void setProcessorResponses(List<ProcessorResponse> processorResponses) {
    this.processorResponses = processorResponses;
  }

  public Audit getAudit() {
    return audit;
  }

  public void setAudit(Audit audit) {
    this.audit = audit;
  }

  @Override
  public void setAuditTime() {
    audit.setEndTime(System.currentTimeMillis());
    
  }
}
