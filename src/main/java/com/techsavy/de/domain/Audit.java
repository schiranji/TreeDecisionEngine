package com.techsavy.de.domain;

import java.io.Serializable;

public class Audit implements Serializable {

  private static final long serialVersionUID = -6623128721174316332L;
  private String type; //Processor/Rule
  private String name; //Processor or Rule name
  private String auditText; //Any text in JSON to log.
  private long startTime;
  private long endTime;
  
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
}
