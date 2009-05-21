package edu.vanderbilt.isis.trixbox;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class TrixboxManipulator {

    public static void doPost(String newExtension) {
    	
    	String server = "http://129.59.37.37";	// The location of your trixbox server
    	String login = "maint";		// The login for your trixbox server
    	String password = "password";	// The password for your trixbox server
    	
    	String extension = "201";	// The extension you want to apply the changes to
    	
    	
        try{
        		// Do the first POST to set the FollowMe List
                HttpClient c = new DefaultHttpClient();
                HttpPost post = new HttpPost(server+"/admin/config.php?display=findmefollow&extdisplay=GRP-"+extension);
                String authorization = Base64.base64Encode(login+":"+password);
                post.addHeader("Authorization","Basic "+authorization);
                post.addHeader("Content-Type","application/x-www-form-urlencoded");
                
                String info = "display=findmefollow&action=edtGRP&account="+extension+"&pre_ring=0&strategy=ringallv2&grptime=20&grplist="+newExtension+"&annmsg=&ringing=Ring&grppre=&dring=&remotealert=&toolate=&Terminate_Call0=app-blackhole%2Changup%2C1&Extensions0=from-did-direct%2C200%2C1&";
                ByteArrayEntity str = new ByteArrayEntity(info.getBytes());
                post.setEntity(str);
                HttpResponse resp = c.execute(post);
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                resp.getEntity().writeTo(bao);
                
                // TODO - Is this used anywhere?
                // String responsetext = new String(bao.toByteArray());
                
                
                // Do the final POST to finalize the changes.
                HttpPost post2 = new HttpPost(server+"/admin/config.php");
                post2.addHeader("Authorization","Basic "+authorization);
                post2.addHeader("Content-Type","application/x-www-form-urlencoded");
                String info2 = "handler=reload";
                ByteArrayEntity str2 = new ByteArrayEntity(info2.getBytes());
                post2.setEntity(str2);
                HttpResponse resp2 = c.execute(post2);
                ByteArrayOutputStream bao2 = new ByteArrayOutputStream();
                resp2.getEntity().writeTo(bao2);
                
                // TODO - Is this used anywhere?
                // String responsetext2 = new String(bao2.toByteArray());     
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