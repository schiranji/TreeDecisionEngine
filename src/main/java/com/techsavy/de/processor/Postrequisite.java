package com.techsavy.de.processor;

import com.techsavy.de.domain.DecisionEngineRequest;
import com.techsavy.de.domain.PostrequisiteResponse;
import com.techsavy.de.domain.ProcessorResponse;

@FunctionalInterface
public interface Postrequisite {
  public PostrequisiteResponse process(DecisionEngineRequest decisionEngineRequest, ProcessorResponse processorResponse);
}