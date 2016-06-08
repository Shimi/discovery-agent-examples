package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Pong {

	private static final Logger Logger =  LoggerFactory.getLogger(Pong.class);
	
	public static void main(String[] args) {
	  Logger.info("Starting Pong...");
	  PongFactory factory = new PongFactory();
	  RestAPI restAPI = factory.getRestAPI();
	  
    Logger.info("Pong started");
	}

}
