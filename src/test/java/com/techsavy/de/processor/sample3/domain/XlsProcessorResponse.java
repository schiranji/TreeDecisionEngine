package com.techsavy.de.processor.sample3.domain;

import com.techsavy.de.domain.ProcessorResponse;

public class XlsProcessorResponse extends ProcessorResponse {

  private static final long serialVersionUID = 3809429523717257191L;
  private double approvedAmount;

  protected XlsProcessorResponse() { 
  }
  
  public static XlsProcessorResponse getInstance() {
    XlsProcessorResponse processorResponse = new XlsProcessorResponse();
    return processorResponse;
  }

  public double getApprovedAmount() {
    return approvedAmount;
  }

  public void setApprovedAmount(double approvedAmount) {
    this.approvedAmount = approvedAmount;
  }
}
