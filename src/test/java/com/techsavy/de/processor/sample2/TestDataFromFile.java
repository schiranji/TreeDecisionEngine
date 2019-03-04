package com.techsavy.de.processor.sample2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.techsavy.de.DecisionEngine;
import com.techsavy.de.common.Constants;
import com.techsavy.de.domain.DecisionEngineResponse;
import com.techsavy.de.processor.sample2.domain.DecisionEngineRequest2;
import com.techsavy.de.processor.sample2.domain.ProcessorResponse2;
import com.techsavy.de.util.LogUtil;

public class TestDataFromFile implements Constants {
  private static final String PROCESSORS2_YML = "processors2.yml";
  private static final String TEST_DATA_FILE = "test.txt";
  private static final Logger log = LogManager.getLogger();
  private static final Logger auditLog = LogManager.getLogger("auditlog");

  public static void main(String[] args) throws Exception {
    System.out.println("Started TestDataFromFile...");
    long engineStartTime = System.currentTimeMillis();
    System.setProperty(PROCESSORS_FILE_PARAM_NAME, PROCESSORS2_YML);
    try (Stream<String> stream = Files.lines(Paths.get(ClassLoader.getSystemResource(TEST_DATA_FILE).toURI()))) {
      stream.forEach(line -> {
        long startTime = System.currentTimeMillis();
        DecisionEngineRequest2 decisionEngineRequest = getDecisionEngineRequest(line);
        ProcessorResponse2 processorResponse2 = ProcessorResponse2.getInstance();
        DecisionEngine de = new DecisionEngine(decisionEngineRequest, processorResponse2);
        DecisionEngineResponse decisionEngineResponse = de.process();
        auditLog.info("Milti Threading time(millis):" + (System.currentTimeMillis()-startTime));
        LogUtil.logObject(log, decisionEngineResponse);
        try {
          decisionEngineResponse = de.processSequentially();
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        auditLog.info("Single Threading time(millis):" + (System.currentTimeMillis()-startTime));
        LogUtil.logObject(log, decisionEngineResponse);
      });
    } catch (IOException e) { log.error("Error while closing stream.", e); }
    System.out.println("Done TestDataFromFile..." + (System.currentTimeMillis()-engineStartTime));
  }

  private static DecisionEngineRequest2 getDecisionEngineRequest(String line) {
    DecisionEngineRequest2 decisionEngineRequest = new DecisionEngineRequest2();
    decisionEngineRequest.setDelinquencies(Integer.parseInt(line));
    return decisionEngineRequest;
  }
}
