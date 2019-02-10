package com.techsavy.de.processor;

import com.techsavy.de.domain.RuleEngineData;
import com.techsavy.de.domain.ProcessorResult;

@FunctionalInterface
public interface Rule {
  public void process(RuleEngineData ruleData, ProcessorResult ruleEngineResult);
}
