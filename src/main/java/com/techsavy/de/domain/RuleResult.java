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
  
  public static void setAudit(RuleResult ruleResult) {
    ruleResult.getAudit().setEndTime(System.currentTimeMillis());
  }

  public Audit getAudit() {
    return audit;
  }

  public void setAudit(Audit audit) {
    this.audit = audit;
  }
}
