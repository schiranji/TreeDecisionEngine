package com.techsavy.de.domain;

public class DecisionEngineRequest implements Request {

  private static final long serialVersionUID = -2411323258847526625L;
  //Types to filter processors during prerequisite. So only relevant processors continue.
  private String type;
  private String category1;
  private String category2;
  private String category3;  
  private int testCounter = 1;
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getCategory1() {
    return category1;
  }
  public void setCategory1(String category1) {
    this.category1 = category1;
  }
  public String getCategory2() {
    return category2;
  }
  public void setCategory2(String category2) {
    this.category2 = category2;
  }
  public String getCategory3() {
    return category3;
  }
  public void setCategory3(String category3) {
    this.category3 = category3;
  }
  public int getTestCounter() {
    return testCounter;
  }
  public void setTestCounter(int testCounter) {
    this.testCounter = testCounter;
  }
}
