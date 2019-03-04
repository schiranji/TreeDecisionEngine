package com.techsavy.de.exception;

public class UserException extends Exception {

  private static final long serialVersionUID = 3479374383487097413L;

  public UserException(String string, Exception e) {
    super(string, e);
  }

}
