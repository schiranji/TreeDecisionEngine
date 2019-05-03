package com.techsavy.de.domain;

import org.codehaus.jackson.annotate.JsonIgnore;

public abstract class ResponseAbstract implements Response {

  private static final long serialVersionUID = 4224747591239345243L;
  private Audit audit;

  @Override
  public void setAuditTime() {
    if(Audit.auditEnabled(getType())) {
       getAudit().setEndTime(System.currentTimeMillis());
    }
  }

  @Override
  public Audit getAudit() {
    return audit;
  }

  @Override
  public void setAudit(Audit audit) {
    this.audit = audit;
  }
  
  @JsonIgnore
  public abstract String getType();
  
  //@JsonIgnore
  //TODO Based on result processor version and result expiration date
  //public abstract String getIsResultStillValid();

}
