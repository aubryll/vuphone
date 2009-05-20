package edu.vanderbilt.ww.jettyserver;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.log.Log;

public class ServerMain{

    public static void main(String[] args){

        Server server = new Server(8080);
 
        ServletHandler handler=new ServletHandler();
        server.setHandler(handler);   
        handler.addServletWithMapping("edu.vanderbilt.ww.jettyserver.ListeningServlet", "/");

        
        try {
        	server.start();
        	server.join();
        }
        catch (Exception e) {
        	Log.warn("Exception starting server with message: "+e.getMessage());
        }
	}

}
