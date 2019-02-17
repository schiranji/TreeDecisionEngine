package com.techsavy.de.domain;

import java.util.List;

public class RuleEngineResponse implements Response {

  private static final long serialVersionUID = -5054894114864253976L;
  List<ProcessorResponse> processorResponses;
  Audit audit;

  private RuleEngineResponse() { 
  }
  
  public static RuleEngineResponse getInstance() {
    Audit audit = new Audit();
    audit.setStartTime(System.currentTimeMillis());
    RuleEngineResponse ruleEngineResponse = new RuleEngineResponse();
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
