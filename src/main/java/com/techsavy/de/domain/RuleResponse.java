package com.techsavy.de.domain;

public class RuleResponse implements Response {

  private static final long serialVersionUID = 8508197356680409469L;
  private String ruleName;
  private Audit audit;
  
  private RuleResponse() { 
  }
  
  public static RuleResponse getInstance(String ruleName) {
    Audit audit = new Audit();
    audit.setStartTime(System.currentTimeMillis());
    audit.setType("Rule");
    audit.setName(ruleName);
    RuleResponse ruleResponse = new RuleResponse();
    ruleResponse.setRuleName(ruleName);
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

  public String getRuleName() {
    return ruleName;
  }

  public void setRuleName(String ruleName) {
    this.ruleName = ruleName;
  }
}
