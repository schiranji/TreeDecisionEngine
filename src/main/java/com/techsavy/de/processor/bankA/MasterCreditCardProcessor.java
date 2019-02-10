package com.techsavy.de.processor.bankA;

import com.techsavy.de.processor.BaseAbstractProcessor;

public class MasterCreditCardProcessor extends BaseAbstractProcessor {
  @Override
  protected void buildPrerequistes() {
    prerequisites.add((ruleEngineData1) -> {
      System.out.println(Thread.currentThread().getName()+", Processing MasterCreditCardProcessor:Prerequiste1: Score: "+" depth:"+depth );
      return true;
    });
  }

  @Override
  protected void buildRules() {
    rules.add((ruleEngineData1, result2) -> 
    { 
      System.out.println(Thread.currentThread().getName()+", Processing MasterCreditCardProcessor:Rule1: Score: "+result2.getScore() +" depth:"+depth); 
      result2.setScore(result2.getScore()+1);
      result2.setDecision("DECLINED");
    });
  }
}
