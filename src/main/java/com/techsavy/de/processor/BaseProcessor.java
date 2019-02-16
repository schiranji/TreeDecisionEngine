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
    rules.add((ruleEngineData1, result2) -> { 
      log.debug(Thread.currentThread().getName()+", Processing BaseProcessor:Rule1: Score: "+result2.getScore() +" depth:"+depth); 
      result2.setScore(result2.getScore()+1);
    });
  }
}
