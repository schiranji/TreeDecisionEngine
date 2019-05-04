package com.techsavy.de.util;

import java.io.IOException;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonUtil {
  private static final Logger log = LogManager.getLogger();
  @SuppressWarnings("unchecked")
  public static HashMap<String, String> getMap(String jsonStr) {
    try {
      return new ObjectMapper().readValue(jsonStr, HashMap.class);
    } catch (IOException e) {
      log.error("Error while converting string to Map.", e);
    }
    return null;
  }
  
  public static String getJson(Object obj) throws JsonGenerationException, JsonMappingException, IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(obj);
  }
}
