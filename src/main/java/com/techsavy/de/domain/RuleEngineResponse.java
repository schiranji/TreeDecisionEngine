package com.techsavy.de.domain;

import java.util.List;

public class RuleEngineResponse implements Response {

  private static final long serialVersionUID = -5054894114864253976L;
  List<ProcessorResponse> processorResults;
  Audit audit;

  private RuleEngineResponse() { 
  }
  
  public static RuleEngineResponse getInstance() {
    Audit audit = new Audit();
    audit.setStartTime(System.currentTimeMillis());
    RuleEngineResponse ruleResult = new RuleEngineResponse();
    ruleResult.setAudit(audit);
    return ruleResult;
  }
  
  public List<ProcessorResponse> getProcessorResults() {
    return processorResults;
  }

  public void setProcessorResults(List<ProcessorResponse> processorResults) {
    this.processorResults = processorResults;
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
