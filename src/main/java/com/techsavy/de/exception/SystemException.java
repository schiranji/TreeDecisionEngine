package com.techsavy.de.exception;

public class SystemException extends RuntimeException {

  private static final long serialVersionUID = -7200574679610550253L;

  public SystemException(String string, Exception e) {
    super(string, e);
  }

  public SystemException(String string) {
    super(string);
  }
}
