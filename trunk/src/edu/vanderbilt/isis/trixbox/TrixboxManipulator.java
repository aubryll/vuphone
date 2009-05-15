package edu.vanderbilt.isis.trixbox;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

//import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TrixboxManipulator extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);


        try{
        		// Do the first POST to set the FollowMe List
                HttpClient c = new DefaultHttpClient();
                HttpPost post = new HttpPost("http://129.59.37.37/admin/config.php?display=findmefollow&extdisplay=GRP-201");
                String authorization = Base64.base64Encode("maint:password");
                post.addHeader("Authorization","Basic "+authorization);
                post.addHeader("Content-Type","application/x-www-form-urlencoded");
                
                String info = "display=findmefollow&action=edtGRP&account=201&pre_ring=0&strategy=ringallv2&grptime=20&grplist=203&annmsg=&ringing=Ring&grppre=&dring=&remotealert=&toolate=&Terminate_Call0=app-blackhole%2Changup%2C1&Extensions0=from-did-direct%2C200%2C1&";
                ByteArrayEntity str = new ByteArrayEntity(info.getBytes());
                post.setEntity(str);
                HttpResponse resp = c.execute(post);
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                resp.getEntity().writeTo(bao);
                String responsetext = new String(bao.toByteArray());
                tv.setText(responsetext);
                
                
                // Do the final POST to finalize the changes.
                HttpPost post2 = new HttpPost("http://129.59.37.37/admin/config.php");
                post2.addHeader("Authorization","Basic "+authorization);
                post2.addHeader("Content-Type","application/x-www-form-urlencoded");
                String info2 = "handler=reload";
                ByteArrayEntity str2 = new ByteArrayEntity(info2.getBytes());
                post2.setEntity(str2);
                HttpResponse resp2 = c.execute(post2);
                ByteArrayOutputStream bao2 = new ByteArrayOutputStream();
                resp2.getEntity().writeTo(bao2);
                String responsetext2 = new String(bao2.toByteArray());
                tv.setText("Post 2 Response"+responsetext2);        
        } 
        catch (IOException ioe) {
                tv.setText("IOException error:"+ioe.getMessage());
        }
        catch (Exception e) {
        	 	tv.setText("Other Exception of type:"+e.getClass());
        	 	tv.append("The message is: "+e.getMessage());
        }

        setContentView(tv);

    }
}