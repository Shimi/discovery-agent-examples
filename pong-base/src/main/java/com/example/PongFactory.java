package com.example;


public class PongFactory {

  private RestAPI restAPI;

  public PongFactory() {    
    String serverId = System.getProperty("server-id");
    this.restAPI = new RestAPI(9877, serverId);
  }

  
  public RestAPI getRestAPI() {
    return restAPI;
  }
}
