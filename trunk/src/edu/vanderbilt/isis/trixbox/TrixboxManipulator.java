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
                post.addHeader("Referer","http://129.59.37.37/admin/config.php?display=findmefollow&extdisplay=GRP-201");
                post.addHeader("Accept","image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, application/x-silverlight, */*");
                post.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; .NET CLR 1.1.4322; .NET CLR 2.0.50727)");
                post.addHeader("Cache-Control","no cache");
                post.addHeader("UA-CPU","x86");
                post.addHeader("Content-Type","application/x-www-form-urlencoded");
                
                //String info = "grplist=203";
                String info = "display=findmefollow&action=edtGRP&account=201&pre_ring=0&strategy=ringallv2&grptime=20&grplist=203&annmsg=&ringing=Ring&grppre=&dring=&remotealert=&toolate=&Terminate_Call0=app-blackhole%2Changup%2C1&Extensions0=from-did-direct%2C200%2C1&";
                ByteArrayEntity str = new ByteArrayEntity(info.getBytes());
                tv.append("Hi again");
                post.setEntity(str);
                tv.append("One more time");
                //get.addHeader("Authorization","Basic "+authorization);
                 //tv.append("request expect-continue = "+post.expectContinue()+"\r\n");
                HttpResponse resp = c.execute(post);
                //tv.append("response expect-continue = "+resp.getParams().getParameter("http.protocol.expect-continue")+"\r\n");
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                tv.append("before getEntity");
                //BasicHttpEntity respe = (BasicHttpEntity)resp.getEntity();
                //byte[] b = new byte[50];
                //resp.getEntity().getContent().read(b);
                resp.getEntity().writeTo(bao);
                tv.append("after getEntity");
                String responsetext = new String(bao.toByteArray());
                //ByteArrayOutputStream bao2 = new ByteArrayOutputStream();
                //post.getEntity().writeTo(bao2);
                //String requesttext = new String(bao2.toByteArray());
                //tv.append("Request = "+post.toString());
                tv.append(responsetext);
        } catch (IOException ioe) {
                        tv.append("IOException error:"+ioe.getMessage());
        }
         catch (Exception e) {
        	tv.append("Other Exception of type:"+e.getClass());
        	tv.append("The message is: "+e.getMessage());
        }

        setContentView(tv);

    }
}