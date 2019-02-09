package com.techsavy.de.processor.loan.auto;

import com.techsavy.de.processor.BaseProcessor;

public class AutoApprovalProcecssorBank2 extends BaseProcessor {

  @Override
  protected void buildRules() {
    rules.add((ruleEngineData1, result2) -> { 
      System.out.println(Thread.currentThread().getName()+", Processing AutoApprovalProcecssorBank2:Rule1: Score: "+result2.getScore() +" depth:"+depth); 
      result2.setScore(result2.getScore()+1);
    });
  }

}
