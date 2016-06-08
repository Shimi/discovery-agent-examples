package com.example;

import static spark.Spark.get;
import static spark.Spark.put;
import static spark.SparkBase.port;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totango.discoveryagent.ConsulClient;

public class RestAPI {
  
  private static final Logger Logger =  LoggerFactory.getLogger(RestAPI.class);
  
  private boolean isHealthy = true;
  
  public RestAPI(int port, PongClient pongClient, ConsulClient consulClient) {
    Logger.info("Starting REST API on port: " + port + "...");
    
    port(port);
    
    get("/ping", (req, res) -> {
      return "ping " + pongClient.ping();
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
   
    get("/kv/:key", (req, res) -> {
      String key = req.params(":key");
      if (consulClient.keyValue(key).isPresent()) {
        return consulClient.keyValue(key).get().getValueAsString().orElse("Missing");
      } else {
        res.status(404);
        return "Not Found";
      }
    });
    
    Logger.info("REST API started");
  }
  
}
