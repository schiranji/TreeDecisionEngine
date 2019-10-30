package com.techsavy.de.processor;

public class BaseProcessor extends AbstractProcessor {
  private static final String PROCESSOR_VERSION = "1.0.0";

  @Override
  protected void buildPrerequistes() {
  }
  @Override
  protected void buildRules() {
  }
  @Override
  protected void buildActions() {  
  }
  @Override
  protected String getProcessorVersion() {
    return PROCESSOR_VERSION;
  }
}
