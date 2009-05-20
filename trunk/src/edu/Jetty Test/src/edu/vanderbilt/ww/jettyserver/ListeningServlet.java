package edu.vanderbilt.ww.jettyserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.log.Log;

@SuppressWarnings("serial")
public class ListeningServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException
    {
		//response.setContentType("text/html");
		//response.setStatus(HttpServletResponse.SC_OK);
		//response.getWriter().println("This is the doGet...");
		doPost(request, response);
    }
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException
	{
		//doGet(request, response);
		Log.info("In the doPost method...");
		//Eventually, figure out some way to switch on user settings to decide
		//which of these to execute and which to ignore
		String contentType = request.getContentType();
		if (contentType != null && contentType.equals("application/x-www-form-urlencoded")) {
			BufferedReader contentstream = new BufferedReader (
								new InputStreamReader(request.getInputStream()));
			String content = contentstream.readLine();
			response.getWriter().println("This is the first line of content: "+content);
			Log.info("This is the first line of content: "+content);
			
			// Get the deceleration value from the post
			int start = content.indexOf("decel");
			int begin = content.indexOf("=",start);
			int end = content.indexOf("&",begin);
			String value = content.substring(begin+1,end);
			double decel = Double.parseDouble(value);
			Log.info("The decel value is "+decel);
			
			// Get the location value from the post
			start = content.indexOf("loc");
			begin = content.indexOf("=",start);
			end = content.indexOf("&",begin);
			value = content.substring(begin+1,end);
			// TODO figure out what datatype this should be
			String loc = value;
			Log.info("The location value is "+loc);
			
			// Get the shouldCall value from the post
			start = content.indexOf("shouldCall");
			begin = content.indexOf("=",start);
			end = content.indexOf("&",begin);
			value = content.substring(begin+1,end);
			boolean shouldCall = Boolean.parseBoolean(value);
			Log.info("The shouldCall value is "+shouldCall);
			
			if (shouldCall) {
				// Get the callNumber value from the post
				start = content.indexOf("callNumber");
				begin = content.indexOf("=",start);
				end = content.indexOf("&",begin);
				value = content.substring(begin+1,end);
				String callNumber = value;
				Log.info("The callNumber value is "+callNumber);
			}
		}
		else {
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println("You didn't set the Content-Type!");
			Log.info("You didn't set the Content-Type!");
		}
	}
}
