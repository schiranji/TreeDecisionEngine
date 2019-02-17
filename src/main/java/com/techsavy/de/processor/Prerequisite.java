package com.techsavy.de.processor;

import com.techsavy.de.domain.RuleEngineRequest;

@FunctionalInterface
public interface Prerequisite {
  public boolean process(RuleEngineRequest ruleEngineRequest);
}
