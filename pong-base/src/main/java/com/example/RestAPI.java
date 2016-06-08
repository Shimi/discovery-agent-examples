package com.example;

import static spark.Spark.get;
import static spark.Spark.put;
import static spark.SparkBase.port;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestAPI {
  
  private static final Logger Logger =  LoggerFactory.getLogger(RestAPI.class);
  
  private boolean isHealthy = true;
  
  public RestAPI(int port, String serverId) {
    Logger.info("Starting REST API on port: " + port + "...");
    
    port(port);
    
    get("/pong", (req, res) -> {
      return "pong " + serverId;
    });
    
    get("/health", (req, res) -> {
      if (isHealthy) {
        res.status(200);
        return "GOOD";
      } else {
        res.status(418);
        return "BAD";
      }
    });
    
    put("/health", (req, res) -> {
      String health = req.queryParams("health");
      if (health != null) {
        if (health.equalsIgnoreCase("GOOD")) {
          isHealthy = true;
        } else {
          isHealthy = false;
        }
        return "OK";
      } else {
        res.status(400);
        return "Bad request";
      }
      
    });
    
    Logger.info("SREST API started");
  }
  
}
