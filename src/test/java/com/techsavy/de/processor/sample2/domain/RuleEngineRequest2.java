package com.techsavy.de.processor.sample2.domain;

import com.techsavy.de.domain.RuleEngineRequest;

public class RuleEngineRequest2 extends RuleEngineRequest {

  private static final long serialVersionUID = 4562601397789368017L;
  public int delinquencies = 5;
  public int getDelinquencies() {
    return delinquencies;
  }
  public void setDelinquencies(int delinquencies) {
    this.delinquencies = delinquencies;
  }
}
