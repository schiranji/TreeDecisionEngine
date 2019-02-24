package com.techsavy.de.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.techsavy.de.common.AppConfig;
import com.techsavy.de.common.ResponseCode;

public class ProcessorResponse extends ResponseAbstract {

  private static final long serialVersionUID = -7819915903074744197L;
  private static final String AUDIT_TYPE_PROCESSOR = "Processor";

  private String processor;
  private int score;
  private String decision;
  private ResponseCode responseCode = ResponseCode.PR_200;
  private List<PrerequisiteResponse> prerequisiteResponses = new ArrayList<PrerequisiteResponse>();
  private List<RuleResponse> ruleResponses = new ArrayList<RuleResponse>();
  private Map<String, String> decisionArrivalSteps;
  
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
    if("true".equals(AppConfig.getSystemProperty(getType()+".audit.enable"))) {
      getAudit().setName(processor);
    }
  }

  public ResponseCode getResponseCode() {
    return responseCode;
  }

  public void setResponseCode(ResponseCode responseCode) {
    this.responseCode = responseCode;
  }

  public List<RuleResponse> getRuleResponses() {
    return ruleResponses;
  }

  public void setRuleResponses(List<RuleResponse> ruleResponses) {
    this.ruleResponses = ruleResponses;
  }
  
  public void addRuleResponse(RuleResponse ruleResponse) {
    this.ruleResponses.add(ruleResponse);
  }

  public List<PrerequisiteResponse> getPrerequisiteResponses() {
    return prerequisiteResponses;
  }

  public void setPrerequisiteResponses(List<PrerequisiteResponse> prerequisiteResponses) {
    this.prerequisiteResponses = prerequisiteResponses;
  }
  public void addPrerequisiteResponse(PrerequisiteResponse prerequisiteResponse) {
    this.prerequisiteResponses.add(prerequisiteResponse);
  }
  @Override
  public String getType() {
    return AUDIT_TYPE_PROCESSOR;
  }
}
