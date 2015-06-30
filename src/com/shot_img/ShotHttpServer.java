package com.shot_img;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.filter.ParameterFilter;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

public class ShotHttpServer {

	public void serverStart(int port)throws IOException {
		InetSocketAddress addr = new InetSocketAddress(port);  
        HttpServer server = HttpServer.create(addr, 0);  
  
        HttpContext context = server.createContext("/", new ShotHandler());  
        server.setExecutor(Executors.newCachedThreadPool());  
        context.getFilters().add(new ParameterFilter()); 
        server.start();  
        System.out.println("Server is listening on port " + port);  
	}
	
}
