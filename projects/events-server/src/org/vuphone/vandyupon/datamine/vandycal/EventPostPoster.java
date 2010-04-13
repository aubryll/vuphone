/**
 * 
 */
package org.vuphone.vandyupon.datamine.vandycal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.vuphone.vandyupon.datastructs.Location;
import org.vuphone.vandyupon.notification.eventpost.EventPost;

/**
 * @author Hamilton Turner
 * 
 */
public class EventPostPoster {

	private static final int REVISION_NUM = 928;

	private static final String PATH = "/vandyupon/events/";
	private static final String HOST = "http://localhost:8082";
	private static final String ID = "VU Ical Bot: Rev." + REVISION_NUM;

	public static void doPost(EventPost eventPost) {
		String postParams = createPostParameters(eventPost);

		System.out.println(postParams);

		executePost(postParams);
	}

	private static void executePost(String postURL) {
		// Prepare post object
		final HttpClient c = new DefaultHttpClient();
		final HttpPost post = new HttpPost(HOST + PATH);
		c.getParams().setParameter("http.socket.timeout", new Integer(1000));
		c.getParams()
				.setParameter("http.connection.timeout", new Integer(1000));
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");
		post.addHeader("Content-Encoding", "UTF-8");

		// Add the parameters to the post
		post.setEntity(new ByteArrayEntity(postURL.getBytes()));

		// Execute
		HttpResponse resp = null;
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		try {
			resp = c.execute(post);
			resp.getEntity().writeTo(bao);

		} catch (ClientProtocolException e) {
			e.printStackTrace();

			// lastError_ = "General HTTP Error";
		} catch (IOException e) {
			e.printStackTrace();

			// lastError_ =
			// "Unable to access server response. Is Internet available?";
		} catch (Exception e) {
			e.printStackTrace();

			// lastError_ = "Unknown problem";
		}

		if (resp == null)
			return;
		else if (resp.getStatusLine().getStatusCode() != 200) {
			// if (lastError_.equals("") == false)
			// lastError_ += ". ";
			// lastError_ += "Server returned "
			// + resp.getStatusLine().getStatusCode() + " status";
			return;
		} else if (bao.size() == 0) {
			// if (lastError_.equals("") == false)
			// lastError_ += ". ";
			// lastError_ += "Server did not acknowledge";
			return;
		}
	}

	private static String createPostParameters(EventPost ep) {

		String startTime = Long.toString(ep.getStartTime());
		String endTime = Long.toString(ep.getEndTime());
		String latitude = null, longitude = null;
		Location loc = ep.getLocation();
		if (loc != null) {
			latitude = Double.toString(loc.getLat());
			longitude = Double.toString(loc.getLon());
		}
		String androidID = ID;

		String name = "", desc = "", locName = "", sourceUid = "", tags = "";

		try {

			name = clean(ep.getName());
			startTime = URLEncoder.encode(startTime, "UTF-8");
			endTime = URLEncoder.encode(endTime, "UTF-8");
			if (ep.getLocationName() != null)
				locName = clean(ep.getLocationName());
			if (loc != null) {
				latitude = URLEncoder.encode(latitude, "UTF-8");
				longitude = URLEncoder.encode(longitude, "UTF-8");
			}
			androidID = clean(androidID);

			if (ep.getDescription() != null) {
				desc = URLEncoder.encode(ep.getDescription(), "UTF-8");

				// Fix bizarre URL encoding issues
				desc = desc.replaceAll("%26%2339%3B", "%27");
				desc = desc.replaceAll("%E2%80%99", "%27");
				desc = desc.replaceAll("%C2%A0", "%A0");
				desc = clean(desc);
			} else
				ep.setDescription("");

			sourceUid = URLEncoder.encode(ep.getSourceUid(), "UTF-8");

			if (ep.getTags() != null) {
				StringBuffer tagBuffer = new StringBuffer();
				for (String s : ep.getTags()) {
					tagBuffer.append(s);
					tagBuffer.append(',');
				}

				// Remove last comma
				tagBuffer.setLength(tagBuffer.length() - 1);

				tags = URLEncoder.encode(tagBuffer.toString(), "UTF-8");
			}
		} catch (UnsupportedEncodingException use) {
			use.printStackTrace();
		}

		// Create the parameter string
		StringBuffer params = new StringBuffer();
		params.append("type=eventpost");

		if (locName != null)
			params.append("&locationname=").append(locName);
		if (latitude != null)
			params.append("&locationlat=").append(latitude);
		if (longitude != null)
			params.append("&locationlon=").append(longitude);
		if (desc.equalsIgnoreCase("") == false)
			params.append("&desc=").append(desc);
		if (tags.equalsIgnoreCase("") == false)
			params.append("&tags=").append(tags);
		params.append("&eventname=").append(name);
		params.append("&starttime=").append(startTime);
		params.append("&endtime=").append(endTime);
		params.append("&userid=").append(androidID);
		params.append("&sourceuid=").append(sourceUid);
		params.append("&resp=xml");

		return params.toString();
	}

	private static String clean(String nonUTFstring)
			throws UnsupportedEncodingException {

		byte[] bytes = URLDecoder.decode(nonUTFstring, "UTF-8").getBytes();
		int i = 0;
		for (byte b : bytes) {
			if ((int) b < 0 || (int) b > 127) {
				System.err.println("Changing byte " + (int) b + " to 32");
				bytes[i] = 32;
			}
			++i;
		}

		return URLEncoder.encode(new String(bytes, "UTF-8"), "UTF-8");
	}
}
