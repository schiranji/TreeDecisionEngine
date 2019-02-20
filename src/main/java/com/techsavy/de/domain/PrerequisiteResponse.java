package com.techsavy.de.domain;

public class PrerequisiteResponse implements Response {

  private static final long serialVersionUID = 8508197356680409469L;
  private String prerequisiteName;
  private boolean passed;
  private Audit audit;
  
  private PrerequisiteResponse() { 
  }
  
  public static PrerequisiteResponse getInstance(String name) {
    Audit audit = new Audit();
    audit.setStartTime(System.currentTimeMillis());
    audit.setType("Prerequisite");
    audit.setName(name);
    PrerequisiteResponse prerequisiteResponse = new PrerequisiteResponse();
    prerequisiteResponse.setPrerequisiteName(name);
    prerequisiteResponse.setAudit(audit);
    return prerequisiteResponse;
  }
  
  public void setAuditTime() {
    getAudit().setEndTime(System.currentTimeMillis());
  }

  public Audit getAudit() {
    return audit;
  }

  public void setAudit(Audit audit) {
    this.audit = audit;
  }

  public String getPrerequisiteName() {
    return prerequisiteName;
  }

  public void setPrerequisiteName(String prerequisiteName) {
    this.prerequisiteName = prerequisiteName;
  }

  public boolean isPassed() {
    return passed;
  }

  public void setPassed(boolean passed) {
    this.passed = passed;
  }
}
