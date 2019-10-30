package com.techsavy.de.domain;

public class RuleResponse extends ResponseAbstract {

  private static final long serialVersionUID = 8508197356680409469L;
  private static final String AUDIT_TYPE_RULE = "Rule";
  private String ruleName;
  private boolean ruleResult = true;

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

  public boolean isRuleResult() {
	return ruleResult;
  }

  public void setRuleResult(boolean ruleResult) {
	this.ruleResult = ruleResult;
  }

  @Override
  public String getType() {
    return AUDIT_TYPE_RULE;
  }
}
