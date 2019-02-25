package com.techsavy.de.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import com.techsavy.de.domain.ProcessorResponse;

public class LogUtil {
  private static final Logger auditLog = LogManager.getLogger("auditlog");
  private static final Marker AUDIT_MARKER = MarkerManager.getMarker("AUDIT");

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
  
  public static String logObject(Logger log, Object object) {
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    try {
      String json = ow.writeValueAsString(object);
      log.debug(json);
      return json;
    } catch (IOException e) {
      log.error("Error while printing Object.", e);
    }
    return null;
  }
  
  public static String logYml(Logger log, Object object) {
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    try {
      String json = ow.writeValueAsString(object);
      json = json.replace("{", "").replace("}", "").replaceAll("\"", "").replace(",", "").replaceAll(" : null", ":").replaceAll(" :", ":");
      log.debug(json);
      return json;
    } catch (IOException e) {
      log.error("Error while printing Yml.", e);
    }
    return null;
  }
  
  public static long logAuditTimeMillis(String messsag, long startTime) {
    long timeSpan = System.currentTimeMillis() - startTime;
    auditLog.info(AUDIT_MARKER, messsag + timeSpan);
    return timeSpan;
  }

  public static long logAuditTimeMicros(String messsage, long ruleStartTime) {
    long timeSpan = (System.nanoTime()-ruleStartTime)/1000;
    auditLog.info(AUDIT_MARKER, messsage + timeSpan);
    return timeSpan;
  }

  @SuppressWarnings("unchecked")
  public static void printMap(Logger log, Map<String, Object> argProcessorMap, String indentation) {
    for(String key: argProcessorMap.keySet()) {
      Object val = argProcessorMap.get(key);
      log.debug(indentation + key);
      if(val instanceof Map) {
        printMap(log, (Map<String, Object>)val, indentation + "  ");
      }
    }
  }
}
