package com.techsavy.de.processor;

import com.techsavy.de.domain.RuleEngineRequest;
import com.techsavy.de.domain.ProcessorResponse;

@FunctionalInterface
public interface Rule {
  public void process(RuleEngineRequest ruleEngineRequest, ProcessorResponse processorResponse);
}
