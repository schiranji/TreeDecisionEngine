package com.techsavy.de.processor;

import com.techsavy.de.domain.RuleEngineData;
import com.techsavy.de.domain.RuleEngineResult;

@FunctionalInterface
public interface Rule {
  public void process(RuleEngineData ruleData, RuleEngineResult ruleEngineResult);
}
