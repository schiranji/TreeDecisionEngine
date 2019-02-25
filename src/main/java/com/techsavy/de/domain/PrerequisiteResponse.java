package com.techsavy.de.domain;

import org.codehaus.jackson.annotate.JsonIgnore;

public class PrerequisiteResponse extends ResponseAbstract {

  @JsonIgnore
  private static final String AUDIT_PREREQUISITE_TYPE = "Prerequisite";
  private static final long serialVersionUID = 8508197356680409469L;
  private String prerequisiteName;
  private boolean passed;
  
  private PrerequisiteResponse() { 
  }
  
  public static PrerequisiteResponse getInstance(String name) {
    PrerequisiteResponse prerequisiteResponse = new PrerequisiteResponse();
    prerequisiteResponse.setPrerequisiteName(name);
    prerequisiteResponse.setAudit(Audit.getInstance(AUDIT_PREREQUISITE_TYPE, name));
    return prerequisiteResponse;
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

  @Override
  public String getType() {
    return AUDIT_PREREQUISITE_TYPE;
  }
}
