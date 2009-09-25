/**
 * 
 */
package org.vuphone.vandyupon.android.submitevent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.GregorianCalendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.vuphone.vandyupon.android.Constants;

import android.content.Context;
import android.util.Log;

import com.google.android.maps.GeoPoint;

/**
 * 
 * Contacts the server and attempts to post a new event.
 * 
 * @author Hamilton Turner
 * 
 */
public class EventPoster {
	private static final String tag = Constants.tag;
	private static final String pre = "EventPoster: ";
	private static final String PATH = "/vandyupon/events/";

	private static String lastError_ = "";

	/**
	 * Attempts to contact the server and post an event.
	 * 
	 * @param name
	 *            event name
	 * @param start
	 *            event start calendar
	 * @param end
	 *            event end calendar
	 * @param location
	 *            event location
	 * @param desc
	 *            event description
	 * @param context
	 *            application context used to fetch the android ID
	 * @return true if the server was contacted, and returned 200 OK, false
	 *         otherwise
	 */
	public static boolean doEventPost(String name, GregorianCalendar start,
			GregorianCalendar end, GeoPoint location, String desc,
			Context context) {

		lastError_ = "";
		
		String startTime = Long.toString(start.getTimeInMillis());
		String endTime = Long.toString(end.getTimeInMillis());
		String latitude = Double
				.toString((double) location.getLatitudeE6() / 1E6);
		String longitude = Double
				.toString((double) location.getLongitudeE6() / 1E6);
		String androidID = Constants.getAndroidID(context);

		try {
			name = URLEncoder.encode(name, "UTF-8");
			startTime = URLEncoder.encode(startTime, "UTF-8");
			endTime = URLEncoder.encode(endTime, "UTF-8");
			latitude = URLEncoder.encode(latitude, "UTF-8");
			longitude = URLEncoder.encode(longitude, "UTF-8");
			androidID = URLEncoder.encode(androidID, "UTF-8");
			desc = URLEncoder.encode(desc, "UTF-8");
		} catch (UnsupportedEncodingException use) {
			use.printStackTrace();
			Log.w(tag, pre + "Unable to encode one of the parameters");
		}

		// Prepare to make the post
		final HttpClient c = new DefaultHttpClient();
		final HttpPost post = new HttpPost(Constants.SERVER + PATH);
		c.getParams().setParameter("http.socket.timeout", new Integer(1000));
		c.getParams().setParameter("http.connection.timeout", new Integer(1000));
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");
		post.addHeader("Content-Encoding", "UTF-8");

		// Create the parameter string
		StringBuffer params = new StringBuffer();
		params.append("type=eventpost");
		params.append("&locationlat=");
		params.append(latitude);
		params.append("&locationlon=");
		params.append(longitude);
		params.append("&eventname=");
		params.append(name);
		params.append("&starttime=");
		params.append(startTime);
		params.append("&endtime=");
		params.append(endTime);
		params.append("&userid=");
		params.append(androidID);
		params.append("&resp=xml");
		params.append("&desc=");
		params.append(desc);

		// Add the parameters to the post
		Log.v(tag, pre + "Created parameter string: " + params);
		post.setEntity(new ByteArrayEntity(params.toString().getBytes()));

		// Execute post
		Log.i(tag, pre + "Executing post to " + Constants.SERVER + PATH);
		HttpResponse resp = null;
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		try {
			resp = c.execute(post);
			resp.getEntity().writeTo(bao);
			Log.v(tag, pre + "Response from server: "
					+ bao.toString());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.e(tag, pre + "ClientProtocolException executing post: "
					+ e.getMessage());
			lastError_ = "General HTTP Error";
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(tag, pre + "IOException writing to ByteArrayOutputStream: "
					+ e.getMessage());
			lastError_ = "Unable to access server response. Is Internet available?";
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(tag, pre + "Other Exception of type:" + e.getClass());
			Log.e(tag, pre + "The message is: " + e.getMessage());
			lastError_ = "Unknown problem";
		}

		if (resp == null)
			return false;
		else if (resp.getStatusLine().getStatusCode() != 200) {
			if (lastError_.equals("") == false)
				lastError_ += ". ";
			lastError_ += "Server returned "
					+ resp.getStatusLine().getStatusCode() + " status";
			return false;
		} else if (bao.size() == 0) {
			if (lastError_.equals("") == false)
				lastError_ += ". ";
			lastError_ += "Server did not acknowledge";
			return false;
		}

		else
			return true;
	}

	public static String getLastError() {
		return lastError_;
	}
}
