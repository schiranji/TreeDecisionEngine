package com.techsavy.de.processor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BaseProcessor extends BaseAbstractProcessor {
  private static final Logger log = LogManager.getLogger();
  public BaseProcessor() {
    buildPrerequistes();
    buildRules();
  }

  @Override
  protected void buildPrerequistes() {
    prerequisites.add((ruleEngineData1) -> {
      log.debug(Thread.currentThread().getName()+", Processing BaseProcessor:Prerequiste: Score: "+" depth:"+depth);
      return true;
    });
  }
  
  @Override
  protected void buildRules() {
    rules.add((ruleEngineRequest, ruleEngineResult) -> { 
      log.debug(Thread.currentThread().getName()+", Processing BaseProcessor:Rule1: Score: "+ruleEngineResult.getScore() +" depth:"+depth); 
      ruleEngineResult.setScore(ruleEngineResult.getScore()+1);
    });
  }
}
