package com.techsavy.de.processor;

import com.techsavy.de.domain.RuleEngineData;

@FunctionalInterface
public interface Prerequisite {
  public boolean process(RuleEngineData ruleData);
}
