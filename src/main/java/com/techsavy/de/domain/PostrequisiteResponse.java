package com.techsavy.de.domain;

import org.codehaus.jackson.annotate.JsonIgnore;

public class PostrequisiteResponse  extends ResponseAbstract {

  private static final long serialVersionUID = -3105760287623807197L;
  @JsonIgnore
  private static final String AUDIT_POSTREQUISITE_TYPE = "Postrequisite";
  private String postrequisiteName;
  private boolean passed;

  private PostrequisiteResponse() { 
  }
  
  public static PostrequisiteResponse getInstance(String name) {
    PostrequisiteResponse postrequisiteResponse = new PostrequisiteResponse();
    postrequisiteResponse.setPostrequisiteName(name);
    postrequisiteResponse.setAudit(Audit.getInstance(AUDIT_POSTREQUISITE_TYPE, name));
    return postrequisiteResponse;
  }
  
  
  public String getPostrequisiteName() {
    return postrequisiteName;
  }

  public void setPostrequisiteName(String postrequisiteName) {
    this.postrequisiteName = postrequisiteName;
  }

  public boolean isPassed() {
    return passed;
  }

  public void setPassed(boolean passed) {
    this.passed = passed;
  }

  @Override
  public String getType() {
    return AUDIT_POSTREQUISITE_TYPE;
  }
}
