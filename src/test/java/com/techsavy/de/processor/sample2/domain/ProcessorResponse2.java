package com.techsavy.de.processor.sample2.domain;

import com.techsavy.de.domain.ProcessorResponse;

public class ProcessorResponse2 extends ProcessorResponse {

  private static final long serialVersionUID = 3809429523717257191L;
  private double approvedAmount;

  protected ProcessorResponse2() { 
  }
  
  public static ProcessorResponse2 getInstance() {
    ProcessorResponse2 processorResponse = new ProcessorResponse2();
    return processorResponse;
  }

  public double getApprovedAmount() {
    return approvedAmount;
  }

  public void setApprovedAmount(double approvedAmount) {
    this.approvedAmount = approvedAmount;
  }
}
