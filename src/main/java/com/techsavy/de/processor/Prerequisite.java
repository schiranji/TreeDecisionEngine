package com.techsavy.de.processor;

import com.techsavy.de.domain.PrerequisiteResponse;
import com.techsavy.de.domain.DecisionEngineRequest;

@FunctionalInterface
public interface Prerequisite {
  public PrerequisiteResponse process(DecisionEngineRequest decisionEngineRequest);
}
