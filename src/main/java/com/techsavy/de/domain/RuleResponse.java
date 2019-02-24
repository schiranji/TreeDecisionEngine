package com.techsavy.de.domain;

public class RuleResponse extends ResponseAbstract {

  private static final long serialVersionUID = 8508197356680409469L;
  private static final String AUDIT_TYPE_RULE = "Rule";
  private String ruleName;

  private RuleResponse() {
  }

  public static RuleResponse getInstance(String ruleName) {
    RuleResponse ruleResponse = new RuleResponse();
    ruleResponse.setRuleName(ruleName);
    ruleResponse.setAudit(Audit.getInstance(AUDIT_TYPE_RULE, ruleName));
    return ruleResponse;
  }

  public String getRuleName() {
    return ruleName;
  }

  public void setRuleName(String ruleName) {
    this.ruleName = ruleName;
  }

  @Override
  public String getType() {
    return AUDIT_TYPE_RULE;
  }
}
