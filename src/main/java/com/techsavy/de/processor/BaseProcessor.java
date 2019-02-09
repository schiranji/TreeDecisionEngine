package com.techsavy.de.processor;

public class BaseProcessor extends BaseAbstractProcessor {

  public BaseProcessor() {
    buildPrerequistes();
    buildRules();
  }

  @Override
  protected void buildPrerequistes() {
    prerequisites.add((ruleEngineData1) -> {
      System.out.println(Thread.currentThread().getName()+", Processing BaseProcessor:Prerequiste: Score: "+" depth:"+depth);
      return true;
    });
  }
  
  @Override
  protected void buildRules() {
    rules.add((ruleEngineData1, result2) -> { 
      System.out.println(Thread.currentThread().getName()+", Processing BaseProcessor:Rule1: Score: "+result2.getScore() +" depth:"+depth); 
      result2.setScore(result2.getScore()+1);
    });
  }
}
