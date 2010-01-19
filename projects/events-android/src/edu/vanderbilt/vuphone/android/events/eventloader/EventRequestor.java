/**
 * 
 */
package edu.vanderbilt.vuphone.android.events.eventloader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.util.Log;

import com.google.android.maps.GeoPoint;

import edu.vanderbilt.vuphone.android.events.Constants;

/**
 * Contains the code that contacts the server via HTTP and downloads the XML for
 * the events
 * 
 * @author Hamilton Turner
 * 
 */
public class EventRequestor {
	/** Used for logging */
	private static final String tag = Constants.tag;
	private static final String pre = "EventRequestor: ";

	/** The path to contact the server on */
	private static final String PATH = "/vandyupon/events/";

	/**
	 * Performs the event request
	 * 
	 * @param anchorLocation
	 *            the central location around which we are requesting events
	 *            from
	 * @param radiusInFeet
	 *            the radius of the circle. Events within this radius will be
	 *            returned by the server, other events will not
	 * @param latestUpdatedTime
	 *            the latest update time that we have for any event. This will
	 *            allow the server to only return events that are new or have
	 *            been modified
	 * @param context
	 *            used to access the Telephony service and get the device ID
	 * @return the OutputStream containing the XML response, or null if an error
	 *         occurred
	 */
	public static ByteArrayOutputStream doEventRequest(GeoPoint anchorLocation,
			int radiusInFeet, long latestUpdatedTime, String androidID) {

		// Convert all params to strings
		String radius = Integer.toString(radiusInFeet);
		String latestTime = Long.toString(latestUpdatedTime);
		String latitude = Double.toString((double) anchorLocation
				.getLatitudeE6() / 1E6);
		String longitude = Double.toString((double) anchorLocation
				.getLongitudeE6() / 1E6);

		// URL encode all params
		try {
			radius = URLEncoder.encode(radius, "UTF-8");
			latestTime = URLEncoder.encode(latestTime, "UTF-8");
			latitude = URLEncoder.encode(latitude, "UTF-8");
			longitude = URLEncoder.encode(longitude, "UTF-8");
			androidID = URLEncoder.encode(androidID, "UTF-8");
		} catch (UnsupportedEncodingException use) {
			use.printStackTrace();
			Log.w(tag, pre + "Unable to encode one of the parameters");
		}

		// Prepare to make the post
		final HttpClient c = new DefaultHttpClient();
		final HttpPost post = new HttpPost(Constants.SERVER + PATH);
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");
		post.addHeader("Content-Encoding", "UTF-8");

		// Create the parameter string
		StringBuffer params = new StringBuffer();
		params.append("type=eventrequest");
		params.append("&lat=");
		params.append(latitude);
		params.append("&lon=");
		params.append(longitude);
		params.append("&updatetime=");
		params.append(latestTime);
		params.append("&dist=");
		params.append(radius);
		params.append("&userid=");
		params.append(androidID);
		params.append("&resp=xml");

		// Add the parameters to the post
		Log.v(tag, pre + "Created parameter string: " + params);
		post.setEntity(new ByteArrayEntity(params.toString().getBytes()));

		// Execute post
		Log.i(tag, pre + "Executing post to " + Constants.SERVER + "?" + PATH + params);
		HttpResponse resp = null;
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		try {
			resp = c.execute(post);
			resp.getEntity().writeTo(bao);
			Log.i(tag, pre + "Received " + bao.size() + " bytes back");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.e(tag, pre + "ClientProtocolException executing post: "
					+ e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(tag, pre + "IOException writing to ByteArrayOutputStream: "
					+ e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(tag, pre + "Other Exception of type:" + e.getClass());
			Log.e(tag, pre + "The message is: " + e.getMessage());
		}

		if (resp == null) {
			Log.e(tag, pre + "Some error occurred, we had no response");
			return null;
		} else if (resp.getStatusLine().getStatusCode() != 200) {
			// log status returned
			Log.e(tag, pre + "Server returned a Status-Code: "
					+ resp.getStatusLine().getStatusCode());
			return null;
		} else if (bao.size() == 0) {
			// log absolutely nothing returned from server!
			Log.e(tag, pre + "We contacted the server, but they did"
					+ " not acknowledge. No data was send back");
			return null;
		}

		return bao;
	}
}
