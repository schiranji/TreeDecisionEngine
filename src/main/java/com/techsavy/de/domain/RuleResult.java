package com.techsavy.de.domain;

public class RuleResult implements Response {

  private static final long serialVersionUID = 8508197356680409469L;
  private Audit audit;
  
  public static RuleResult getInstance() {
    Audit audit = new Audit();
    audit.setStartTime(System.currentTimeMillis());
    RuleResult ruleResult = new RuleResult();
    ruleResult.setAudit(audit);
    return ruleResult;
  }
  
  public RuleResult populateAudit() {
    getAudit().setEndTime(System.currentTimeMillis());
    return this;
  }

  public Audit getAudit() {
    return audit;
  }

  public void setAudit(Audit audit) {
    this.audit = audit;
  }
}
