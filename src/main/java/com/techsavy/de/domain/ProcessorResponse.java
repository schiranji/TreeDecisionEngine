package com.techsavy.de.domain;

import java.util.Map;

public class ProcessorResponse implements Response {

  private static final long serialVersionUID = -7819915903074744197L;
  
  private String processor;
  private int score;
  private String decision;
  private Map<String, String> decisionArrivalSteps;
  private Audit audit;
  
  private ProcessorResponse() { 
  }
  
  public static ProcessorResponse getInstance() {
    Audit audit = new Audit();
    audit.setStartTime(System.currentTimeMillis());
    ProcessorResponse processorResponse = new ProcessorResponse();
    processorResponse.setAudit(audit);
    return processorResponse;
  }
  
  public void setAuditTime() {
    getAudit().setEndTime(System.currentTimeMillis());
  }
  
  public int getScore() {
    return score;
  }
  public void setScore(int score) {
    this.score = score;
  }
  public String getDecision() {
    return decision;
  }
  public void setDecision(String decision) {
    this.decision = decision;
  }
  public Map<String, String> getDecisionArrivalSteps() {
    return decisionArrivalSteps;
  }
  public void setDecisionArrivalSteps(Map<String, String> decisionArrivalSteps) {
    this.decisionArrivalSteps = decisionArrivalSteps;
  }
  public String getProcessor() {
    return processor;
  }
  public void setProcessor(String processor) {
    this.processor = processor;
  }

  public Audit getAudit() {
    return audit;
  }

  public void setAudit(Audit audit) {
    this.audit = audit;
  }
}
