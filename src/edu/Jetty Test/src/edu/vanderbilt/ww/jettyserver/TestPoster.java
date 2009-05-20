package edu.vanderbilt.ww.jettyserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class TestPoster {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try{

			// This doesn't seem to actually connect to the server...
			// See the tutorial at 
			// http://java.sun.com/docs/books/tutorial/networking/urls/readingWriting.html
			URL localhost = new URL("http://localhost:8080");
			URLConnection con = localhost.openConnection();
			con.setDoOutput(true);
			OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
			out.write("decel=-23.478&loc=here&shouldCall=false&callNumber=8001234567&");
			out.close();
			con.getInputStream();
            /*HttpClient c = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://localhost:8080");
            post.addHeader("Content-Type","application/x-www-form-urlencoded");
            
            String info = "This is the content I will try to post.";
            ByteArrayEntity str = new ByteArrayEntity(info.getBytes());
            post.setEntity(str);
            HttpResponse resp = c.execute(post);
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            resp.getEntity().writeTo(bao);
            String responsetext = new String(bao.toByteArray());
            System.out.println("The response was: "+responsetext);*/
              
    } 
    catch (IOException ioe) {
            System.out.println("IOException error:"+ioe.getMessage());
    }
    catch (Exception e) {
    		System.out.println("Other Exception of type:"+e.getClass());
    		System.out.println("The message is: "+e.getMessage());
    }

	}

}
