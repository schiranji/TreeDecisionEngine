package com.techsavy.de.domain;

import java.util.concurrent.ExecutorService;

public class DecisionEngineScope {

  private static InheritableThreadLocal<DecisionEngineRequest> decisionEngineRequest = new InheritableThreadLocal<DecisionEngineRequest>();
  private static InheritableThreadLocal<ExecutorService> executorService = new InheritableThreadLocal<ExecutorService>();
  
  public static void setDecisionEngineRequest(DecisionEngineRequest request) {
    decisionEngineRequest.set(request);
  }
  public static DecisionEngineRequest getDecisionEngineRequest() {
    return decisionEngineRequest.get();
  }
  public static void setExecutorService(ExecutorService request) {
    executorService.set(request);
  }
  public static ExecutorService getExecutorService() {
    return executorService.get();
  }
  
  public static void cleanScope() {
    DecisionEngineScope.setDecisionEngineRequest(null);
    DecisionEngineScope.setExecutorService(null);
  }

}
