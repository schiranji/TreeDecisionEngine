package com.techsavy.de.processor;

import com.techsavy.de.domain.ProcessorResponse;
import com.techsavy.de.domain.DecisionEngineRequest;
import com.techsavy.de.domain.RuleResponse;

@FunctionalInterface
public interface Rule {
  public RuleResponse process(DecisionEngineRequest ruleEngineRequest, ProcessorResponse processorResponse);
}
