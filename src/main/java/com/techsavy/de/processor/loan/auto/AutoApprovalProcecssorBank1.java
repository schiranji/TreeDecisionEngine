package com.techsavy.de.processor.loan.auto;

import com.techsavy.de.processor.BaseProcessor;

public class AutoApprovalProcecssorBank1 extends BaseProcessor {

  @Override
  protected void buildPrerequistes() {
    prerequisites.add((ruleEngineData1) -> {
      log.debug(Thread.currentThread().getName()+", Processing AutoApprovalProcecssorBank1:Prerequiste1: Score: "+" depth:"+depth );
      return true;
    });
  }
  
  @Override
  protected void buildRules() {
    rules.add((ruleEngineData1, result2) -> 
    { 
      log.debug(Thread.currentThread().getName()+", Processing AutoApprovalProcecssorBank1:Rule1: Score: "+result2.getScore() +" depth:"+depth); 
      result2.setScore(result2.getScore()+1);
      result2.setDecision("DECLINED");
    });
  }

}
