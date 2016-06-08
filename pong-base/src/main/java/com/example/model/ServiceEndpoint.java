package com.example.model;

import java.util.List;

public class ServiceEndpoint {

  private String node;
  private String address;
  private String serviceId;
  private String serviceName;
  private List<String> serviceTags;
  private int servicePort;

  public ServiceEndpoint(String node, String address, String serviceId, String serviceName,
      List<String> serviceTags, int servicePort) {
    this.node = node;
    this.address = address;
    this.serviceId = serviceId;
    this.serviceName = serviceName;
    this.serviceTags = serviceTags;
    this.servicePort = servicePort;
  }

  public String getNode() {
    return node;
  }

  public String getAddress() {
    return address;
  }

  public String getServiceId() {
    return serviceId;
  }

  public String getServiceName() {
    return serviceName;
  }

  public List<String> getServiceTags() {
    return serviceTags;
  }

  public int getServicePort() {
    return servicePort;
  }

}