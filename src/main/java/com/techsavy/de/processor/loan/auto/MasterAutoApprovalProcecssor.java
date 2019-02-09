package com.techsavy.de.processor.loan.auto;

import com.techsavy.de.processor.BaseProcessor;

public class MasterAutoApprovalProcecssor extends BaseProcessor {

  @Override
  protected void buildPrerequistes() {
    prerequisites.add((ruleEngineData1) -> {
      System.out.println(Thread.currentThread().getName()+", Processing MasterAutoApprovalProcecssor:Prerequiste1: Score: "+" depth:"+depth );
      return true;
    });
  }

  @Override
  protected void buildRules() {
    rules.add((ruleEngineData1, result2) -> {
      System.out.println(Thread.currentThread().getName()+", Processing MasterAutoApprovalProcecssor:Rule1: Score: "+result2.getScore()+" depth:"+depth); 
      result2.setScore(result2.getScore()+ruleEngineData.i);
    });
  }
}
