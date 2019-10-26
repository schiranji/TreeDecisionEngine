package com.techsavy.de.processor.sample3;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.techsavy.de.DecisionEngine;
import com.techsavy.de.common.Constants;
import com.techsavy.de.domain.DecisionEngineResponse;
import com.techsavy.de.processor.sample2.domain.ProcessorResponse2;
import com.techsavy.de.processor.sample3.domain.DecisionEngineRequestXls;
import com.techsavy.de.processor.sample3.domain.XlsProcessorResponse;
import com.techsavy.de.util.LogUtil;

public class TestXlsDecisionEngine implements Constants {
	private static final int REPEAT_COUNT = 1;
	private static final Logger log = LogManager.getLogger();
	  private static final Logger auditLog = LogManager.getLogger("auditlog");
	  public static void main(String[] args) throws Exception {
	    System.out.println("Started...");
	    long startTime = System.currentTimeMillis();
	    System.setProperty(PROCESSORS_FILE_PARAM_NAME, "XlsProcessors.yml");
	    for(int i=0;i<REPEAT_COUNT;i++) {
	      DecisionEngineRequestXls decisionEngineRequest = new DecisionEngineRequestXls();
	      ProcessorResponse2 processorResponse2 = ProcessorResponse2.getInstance();
	      decisionEngineRequest.setDelinquencies(i);
	      DecisionEngine de = new DecisionEngine(decisionEngineRequest, processorResponse2);
	      DecisionEngineResponse decisionEngineResponse = de.process();
	      auditLog.info("Milti Threading time(millis):" + (System.currentTimeMillis()-startTime));
	      LogUtil.logObject(log, decisionEngineResponse);    
	    }
	    for(int i=0;i<REPEAT_COUNT;i++) {
	      startTime = System.currentTimeMillis();
	      DecisionEngineRequestXls decisionEngineRequest = new DecisionEngineRequestXls();
	      decisionEngineRequest.setDelinquencies(i);
	      XlsProcessorResponse processorResponse2 = XlsProcessorResponse.getInstance();
	      DecisionEngine de = new DecisionEngine(decisionEngineRequest, processorResponse2);
	      DecisionEngineResponse decisionEngineResponse = de.processSequentially();
	      auditLog.info("Single Threading time(millis):" + (System.currentTimeMillis()-startTime));
	      LogUtil.logObject(log, decisionEngineResponse); 
	    }
	    System.out.println("Done...");
	  }
}
