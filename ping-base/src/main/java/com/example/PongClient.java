package com.example;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.totango.discoveryagent.DiscoveryService;
import com.totango.discoveryagent.NoServiceAvailable;
import com.totango.discoveryagent.RoundRobinLoadBalancer;

public class PongClient {

  private static final Logger Logger =  LoggerFactory.getLogger(PongClient.class);
  
  private static final String PONG_SERVICE_NAME = "pong";

  private OkHttpClient httpClient;

  private RoundRobinLoadBalancer balancer;
  
  public PongClient(OkHttpClient httpClient, DiscoveryService discoveryService) throws IOException {
    this.httpClient = httpClient;
    this.balancer = new RoundRobinLoadBalancer(discoveryService, PONG_SERVICE_NAME);
    balancer.init();
  }
  
  public String ping() throws NoServiceAvailable {
    return balancer.withNextEndpoint((host, port) -> {
      try {
        String url = String.format("http://%s:%d/pong", host, port);
        
        Request request = new Request.Builder()
          .url(url)
          .build();
    
        Response response = httpClient.newCall(request).execute();
        return response.body().string();
      } catch (Exception e) {
        Logger.error("Failed to send ping request", e);
        throw new RuntimeException(e);
      }
    });
  }
  
}
