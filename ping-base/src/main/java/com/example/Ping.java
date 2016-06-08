package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ping {

  private static final Logger Logger =  LoggerFactory.getLogger(Ping.class);
  
  public static void main(String[] args) {
    Logger.info("Starting Ping...");
    try {
      PingFactory factory = new PingFactory();
      @SuppressWarnings("unused")
      RestAPI restAPI = factory.getRestAPI();
      Logger.info("Ping started");
    } catch (Exception e) {
      Logger.error("Failed to start Ping", e);
    }
  }
}
