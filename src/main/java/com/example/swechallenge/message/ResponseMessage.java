package com.example.swechallenge.message;

public class ResponseMessage {
  private Integer success;

  public ResponseMessage(Integer success) {
    this.success = success;
  }

  public Integer getsuccess() {
    return success;
  }

  public void setsuccess(Integer success) {
    this.success = success;
  }
}
