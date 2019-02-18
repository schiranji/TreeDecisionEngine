package com.techsavy.de.util;

import java.util.List;

import org.apache.logging.log4j.Logger;

import com.techsavy.de.domain.ProcessorResponse;

public class LogUtil {

  public static void printResults(Logger log, List<ProcessorResponse> results) {
    if (results != null) {
      log.debug("**** Results Start ***");
      for (ProcessorResponse result : results) {
        log.debug("Processor:" + result.getProcessor() + ", Decision:" + result.getDecision() + ", Score:"
            + result.getScore());
      }
      log.debug("**** Results End ***");
    }
  }
}
