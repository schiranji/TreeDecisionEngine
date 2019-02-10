package com.techsavy.de.domain;

import java.io.Serializable;
import java.util.List;

public class RuleEngineResponse implements Serializable {

  private static final long serialVersionUID = -5054894114864253976L;
  List<ProcessorResponse> processorResults;
  Audit audit;

  private RuleEngineResponse() { 
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
}
