package com.example;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.squareup.okhttp.OkHttpClient;
import com.totango.discoveryagent.ConsulClient;
import com.totango.discoveryagent.ConsulClientFactory;
import com.totango.discoveryagent.DiscoveryService;

public class PingFactory {

  private RestAPI restAPI;

  private DiscoveryService discoveryService;

  private PongClient pongClient;

  public PingFactory() throws IOException {
    OkHttpClient okHttpClient = new OkHttpClient();
    ConsulClientFactory consulClientFactory = new ConsulClientFactory();
    ConsulClient consulClient = consulClientFactory
        .waitTimeInSec(600)
        .client();
    
    this.discoveryService = new DiscoveryService(consulClient, 10, i -> (int)Math.pow(i, 3), TimeUnit.MILLISECONDS);
    this.pongClient = new PongClient(okHttpClient, discoveryService);
    
    this.restAPI = new RestAPI(9876, pongClient, consulClient);
  }

  public RestAPI getRestAPI() {
    return restAPI;
  }
}
