package com.techsavy.de.processor.sample2.domain;

import com.techsavy.de.domain.DecisionEngineRequest;

public class DecisionEngineRequest2 extends DecisionEngineRequest {

  private static final long serialVersionUID = 4562601397789368017L;
  public int delinquencies = 5;
  public int getDelinquencies() {
    return delinquencies;
  }
  public void setDelinquencies(int delinquencies) {
    this.delinquencies = delinquencies;
  }
}
