package com.techsavy.de.domain;

import java.io.Serializable;

public interface Response extends Serializable, Cloneable {
  public void setAuditTime();
  public Audit getAudit();
  public void setAudit(Audit audit);
}
