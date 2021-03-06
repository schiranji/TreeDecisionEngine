package com.techsavy.de.domain;

import java.io.Serializable;
import java.util.Map;

import com.techsavy.de.common.AppConfig;

public class Audit implements Serializable {

  private static final long serialVersionUID = -6623128721174316332L;
  private String type; // Processor/Rule
  private String name; // Processor or Rule name
  private String auditText; // Any text in JSON to log.
  private Map<String, String> auditMap;
  private long startTime;
  private long endTime;

  private Audit() {

  }

  public static Audit getInstance(String type) {
    return getInstance(type, null);
  }

  public static Audit getInstance(String type, String name) {
    if (auditEnabled(type)) {
      Audit audit = new Audit();
      audit.setStartTime(System.currentTimeMillis());
      audit.setType(type);
      audit.setName(name);
      return audit;
    }
    return null;
  }
  
  public static boolean auditEnabled(String type) {
    return ( "true".equals(AppConfig.getProperty(type.toLowerCase() + ".audit.enable"))) ||
        ("true".equals(AppConfig.getProperty("audit.enable")));
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAuditText() {
    return auditText;
  }

  public void setAuditText(String auditText) {
    this.auditText = auditText;
  }

  public long getStartTime() {
    return startTime;
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  public long getEndTime() {
    return endTime;
  }

  public void setEndTime(long endTime) {
    this.endTime = endTime;
  }

  public long getTimespan() {
    if (startTime == 0 || endTime == 0) {
      throw new RuntimeException("Missing start/end time.");
    }
    return (endTime - startTime);
  }

  public Map<String, String> getAuditMap() {
    return auditMap;
  }

  public void setAuditMap(Map<String, String> auditMap) {
    this.auditMap = auditMap;
  }
}
