package com.techsavy.de.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppConfig implements Constants {
  private static final Logger log = LogManager.getLogger();
  private static Properties properties = new Properties();
  static {
    String configFileName = System.getProperty(CONFIG_FILE_PARAM_NAME);
    configFileName = (StringUtils.isNotBlank(configFileName)) ? configFileName : DEFAULT_CONFIG_FILE_NAME;
    try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(configFileName);) {
      properties.load(inputStream);
    } catch (IOException e) {
      log.error("Error while loading configuration file.", e);
    }
  }
  
  public static String getProperty(String key) {
    return properties.getProperty(key);
  }
  
  public static boolean getBooleanProperty(String key) {
    return new Boolean(properties.getProperty(key)).booleanValue();
  }
  
  public static String getSystemProperty(String key) {
    return (System.getProperty(key) != null) ? System.getProperty(key) : getProperty(key);
  }
}
