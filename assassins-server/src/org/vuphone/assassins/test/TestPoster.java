package org.vuphone.assassins.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.vuphone.assassins.landminerequest.LandMine;

public class TestPoster {
	
	private static final String SERVER = "http://129.59.135.147:8080";
	
	private static final String PATH = "/assassins/notifications";
	
	private static final HttpClient c = new DefaultHttpClient();
	
	public static void doLandMinePost(LandMine lm) {
		
		final HttpPost post = new HttpPost(SERVER + PATH);
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");
		
		StringBuffer params = new StringBuffer();
		params.append("type=landMinePost&lat="+lm.getLatitude()+
				"&lon="+lm.getLongitude()+"&radius="+lm.getRadius());
		
		post.setEntity(new ByteArrayEntity(params.toString().getBytes()));

		// Do it
;		HttpResponse resp = null;
		try {
			resp = c.execute(post);
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			resp.getEntity().writeTo(bao);
			System.out.println("Response from server: "
					+ new String(bao.toByteArray()));					
		} catch (ClientProtocolException e) {
			System.out.println("ClientProtocolException executing post: "
					+ e.getMessage());
		} catch (IOException e) {
			System.out.println("IOException writing to ByteArrayOutputStream: "
					+ e.getMessage());
		} catch (Exception e) {
			System.out.println("Other Exception of type:" 
					+ e.getClass());
			System.out.println("The message is: "
					+ e.getMessage());
		}
	}
	
	public static void doLandMineRemove(LandMine lm) {
		
		final HttpPost post = new HttpPost(SERVER + PATH);
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");
		
		StringBuffer params = new StringBuffer();
		params.append("type=landMineRemove&lat="+lm.getLatitude()+
				"&lon="+lm.getLongitude());
		
		post.setEntity(new ByteArrayEntity(params.toString().getBytes()));

		// Do it
		HttpResponse resp = null;
		try {
			resp = c.execute(post);
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			resp.getEntity().writeTo(bao);
			System.out.println("Response from server: "
					+ new String(bao.toByteArray()));					
		} catch (ClientProtocolException e) {
			System.out.println("ClientProtocolException executing post: "
					+ e.getMessage());
		} catch (IOException e) {
			System.out.println("IOException writing to ByteArrayOutputStream: "
					+ e.getMessage());
		} catch (Exception e) {
			System.out.println("Other Exception of type:" 
					+ e.getClass());
			System.out.println("The message is: "
					+ e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		//double lat = 36.149 + (0.001 * Math.random());
		//double lon = -86.800 + (0.001 * Math.random());
		
		// This puts a land mine in the middle of the parking lot
		// between ISIS and 21st.
		double lat = 36.14923059940338;
		double lon = -86.79988324642181;
		double rad = 5.0;
		LandMine lm = new LandMine(lat, lon, rad);
		//doLandMinePost(lm);
		doLandMineRemove(lm);
	}

}
