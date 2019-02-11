package com.techsavy.de.processor.bankA;

import com.techsavy.de.processor.BaseProcessor;

public class AutoApprovalProcecssorBankA extends BaseProcessor {

  @Override
  protected void buildPrerequistes() {
    prerequisites.add((ruleEngineData1) -> {
      log.debug(Thread.currentThread().getName()+", Processing AutoApprovalProcecssorBankA:Prerequiste1: Score: "+" depth:"+depth);
      return true;
    });
  }
  
  @Override
  protected void buildRules() {
    rules.add((ruleEngineData1, result2) -> { 
      log.debug(Thread.currentThread().getName()+", Processing AutoApprovalProcecssorBankA:Rule1: Score: "+result2.getScore() +" depth:"+depth); 
      result2.setScore(result2.getScore()+1);
    });
  }

}
