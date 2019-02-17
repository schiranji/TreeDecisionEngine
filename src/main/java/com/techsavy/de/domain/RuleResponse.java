package com.techsavy.de.domain;

public class RuleResponse implements Response {

  private static final long serialVersionUID = 8508197356680409469L;
  private Audit audit;
  
  private RuleResponse() { 
  }
  
  public static RuleResponse getInstance() {
    Audit audit = new Audit();
    audit.setStartTime(System.currentTimeMillis());
    RuleResponse ruleResponse = new RuleResponse();
    ruleResponse.setAudit(audit);
    return ruleResponse;
  }
  
  public void setAuditTime() {
    getAudit().setEndTime(System.currentTimeMillis());
  }

  public Audit getAudit() {
    return audit;
  }

  public void setAudit(Audit audit) {
    this.audit = audit;
  }
}
