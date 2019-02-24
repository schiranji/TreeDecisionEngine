package com.techsavy.de.domain;

import com.techsavy.de.common.AppConfig;

public abstract class ResponseAbstract implements Response {

  private static final long serialVersionUID = 4224747591239345243L;
  private Audit audit;

  @Override
  public void setAuditTime() {
    if("true".equals(AppConfig.getSystemProperty(getType()+".audit.enable"))) {
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
  
  public abstract String getType();
}
