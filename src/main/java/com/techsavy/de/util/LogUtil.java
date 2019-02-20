package com.techsavy.de.util;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

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
  
  public static void logObject(Logger log, Object object) {
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    try {
      String json = ow.writeValueAsString(object);
      log.debug(json);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
