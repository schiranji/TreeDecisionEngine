package com.techsavy.de.processor.loan.home;

import com.techsavy.de.processor.BaseProcessor;

public class MasterHomeApprovalProcessor extends BaseProcessor {
  
  @Override
  protected void buildPrerequistes() {
    prerequisites.add((ruleEngineData1) -> {
      System.out.println(Thread.currentThread().getName()+", Processing MasterHomeApprovalProcessor:Prerequiste1: Score: "+" depth:"+depth );
      return true;
    });
  }
  
  @Override
  protected void buildRules() {
    rules.add((ruleEngineData1, result2) -> { 
      System.out.println(Thread.currentThread().getName()+", Processing MasterHomeApprovalProcessor:Rule1: Score: "+result2.getScore() +" depth:"+depth); 
      result2.setScore(result2.getScore()+1);
    });
  }
}
