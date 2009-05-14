package edu.vanderbilt.isis.trixbox;


import java.io.ByteArrayOutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
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
                HttpClient c = new DefaultHttpClient();
                //HttpGet get = new HttpGet("http://129.59.37.37/admin/config.php?display=findmefollow&extdisplay=GRP-201&grplist=203");
                HttpPost post = new HttpPost("http://129.59.37.37/admin/config.php?display=findmefollow&extdisplay=GRP-201");
                //String authorization = System.Convert.ToBase64String("maint:password");
                String authorization = Base64.base64Encode("maint:password");
                tv.setText("authorization (after encoding) = "+authorization);
                System.out.println("authorization (after encoding) = "+authorization);
                post.addHeader("Authorization","Basic "+authorization);
                //HttpParams params = post.getParams();
                //params.setParameter("grplist", "204");
                //post.setParams(params);
                tv.append("Hi");
                post.addHeader("Cookie","PHPSESSID=7eb2d447a90356bdeaa6c8c4b614c033");
                //String info = "grplist=203";
                String info = "display=findmefollow&action=edtGRP&account=201&pre_ring=0&strategy=ringallv2&grptime=20&grplist=203&annmsg=&ringing=Ring&grppre=&dring=&remotealert=&toolate=&Terminate_Call0=app-blackhole%2Changup%2C1&Extensions0=from-did-direct%2C200%2C1&";
                ByteArrayEntity str = new ByteArrayEntity(info.getBytes());
                tv.append("Hi again");
                post.setEntity(str);
                tv.append("One more time");
                //get.addHeader("Authorization","Basic "+authorization);
                 //tv.append("request expect-continue = "+post.expectContinue()+"\r\n");
                HttpResponse resp = c.execute(post);
                // Debug this to find what the final string for the parameter
                // name should be!!! 
                //tv.append("response expect-continue = "+resp.getParams().getParameter("http.protocol.expect-continue")+"\r\n");
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                tv.append("before getEntity");
                BasicHttpEntity respe = (BasicHttpEntity)resp.getEntity();
                tv.append("after getEntity");
                byte[] b = new byte[50];
                respe.getContent().read(b);
                respe.writeTo(bao);
                String responsetext = new String(bao.toByteArray());
                //ByteArrayOutputStream bao2 = new ByteArrayOutputStream();
                //post.getEntity().writeTo(bao2);
                //String requesttext = new String(bao2.toByteArray());
                //tv.append("Request = "+post.toString());
                tv.append(responsetext);
        } catch (Exception e) {
                        tv.append("error:"+e.getMessage());
                }

        setContentView(tv);

    }
}