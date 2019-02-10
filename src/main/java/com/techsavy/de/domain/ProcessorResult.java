package com.techsavy.de.domain;

import java.util.Map;

public class RuleEngineResult implements Response {

  private static final long serialVersionUID = -7819915903074744197L;
  
  private String processor;
  private int score;
  private String decision;
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
  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
  public String getProcessor() {
    return processor;
  }
  public void setProcessor(String processor) {
    this.processor = processor;
  }
}
