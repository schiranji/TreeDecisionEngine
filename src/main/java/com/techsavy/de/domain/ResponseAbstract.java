package com.techsavy.de.domain;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public abstract class ResponseAbstract implements Response {

  private static final long serialVersionUID = 4224747591239345243L;
  @JsonInclude(Include.NON_EMPTY)
  private Audit audit;

  @Override
  public void setAuditTime() {
    if(Audit.auditEnabled(getType())) {
       getAudit().setEndTime(System.currentTimeMillis());
    }
  }

  @Override
  @JsonInclude(Include.NON_EMPTY)
  public Audit getAudit() {
    return audit;
  }

  @Override
  @JsonInclude(Include.NON_EMPTY)
  public void setAudit(Audit audit) {
    this.audit = audit;
  }
  
  @JsonIgnore
  public abstract String getType();
}
