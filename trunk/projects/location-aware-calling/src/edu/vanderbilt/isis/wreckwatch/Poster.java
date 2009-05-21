package edu.vanderbilt.isis.wreckwatch;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class Poster {
	
	public static void doPost(String location, double decel) {
    	
    	String server = "http://129.59.135.182";	// The ip of the server
    	String port = "8080";				// The port the server is running on
    	
        try{
        		Log.v("POSTER", "In the Poster.doPost()...");
                HttpClient c = new DefaultHttpClient();
                HttpPost post = new HttpPost(server+":"+port);
                post.addHeader("Content-Type","application/x-www-form-urlencoded");
                
                String info = "decel="+decel+"&loc="+location+"&";
                ByteArrayEntity str = new ByteArrayEntity(info.getBytes());
                post.setEntity(str);
                HttpResponse resp = c.execute(post);
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                resp.getEntity().writeTo(bao);
                String responsetext = new String(bao.toByteArray());
                Log.v("POSTER", "The response is: "+responsetext);
        } 
        catch (IOException ioe) {
                Log.v("VUPHONE", "IOException error:"+ioe.getMessage());
        }
        catch (Exception e) {
        		Log.v("VUPHONE", "Other Exception of type:"+e.getClass());
        		Log.v("VUPHONE", "The message is: "+e.getMessage());
        }

    }
}
