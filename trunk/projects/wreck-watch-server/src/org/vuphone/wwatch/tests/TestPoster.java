package org.vuphone.wwatch.tests;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class TestPoster {
	private static final String SERVER = "http://localhost:8080";

	private static final String PATH = "/wreckwatch/notifications";

	private static final Logger logger_ = Logger.getLogger(TestPoster.class
			.getName());

	// There might be a better way to do these methods....
	/**
	 * This method will be responsible for posting data to the WreckWatch server
	 * and executing a Runnable if the post is successful.
	 * 
	 * @param message
	 * @param okRun
	 *            A Runnable to execute if the response comes back with a 200 OK
	 *            status
	 */
	public static void doAccidentPost(String androidid, Long time,
			Double speed, Double dec, double lat, double lon) {

		String timeStr = Long.toString(time.longValue());
		String speedStr = Double.toString(speed.doubleValue());
		String decStr = Double.toString(dec.doubleValue());

		try {
			timeStr = URLEncoder.encode(time.toString(), "UTF-8");
			speedStr = URLEncoder.encode(speed.toString(), "UTF-8");
			decStr = URLEncoder.encode(dec.toString(), "UTF-8");
			androidid = URLEncoder.encode(androidid, "UTF-8");
		} catch (UnsupportedEncodingException use) {
			logger_.log(Level.SEVERE,
					"HTTPPoster unable to encode one of the parameters");
		}

		// Log.v(LOG_LABEL, LOG_MSG_PREFIX +
		// "Entering HTTPPoster.doAccidentPost");
		final HttpClient c = new DefaultHttpClient();
		final HttpPost post = new HttpPost(SERVER + PATH);
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");

		StringBuffer params = new StringBuffer();

		// Create the parameter string
		params.append("type=accident&user=" + androidid + "&time=" + timeStr
				+ "&speed=" + speedStr + "&dec=" + decStr + "&lat=" + lat
				+ "&lon=" + lon);

		// Add the parameters
		// Log
		// .v(LOG_LABEL, LOG_MSG_PREFIX + "Created parameter string: "
		// + params);
		post.setEntity(new ByteArrayEntity(params.toString().getBytes()));

		// Do it
		HttpResponse resp = null;
		try {
			resp = c.execute(post);
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			resp.getEntity().writeTo(bao);
			logger_.log(Level.FINER, "Response from server: "
					+ new String(bao.toByteArray()));
		} catch (ClientProtocolException e) {
			logger_
					.log(Level.SEVERE,
							"ClientProtocolException executing post: "
									+ e.getMessage());
		} catch (IOException e) {
			logger_.log(Level.SEVERE,
					"IOException writing to ByteArrayOutputStream: "
							+ e.getMessage());
		} catch (Exception e) {
			logger_
					.log(Level.SEVERE, "Other Exception of type:"
							+ e.getClass());
			logger_.log(Level.SEVERE, "The message is: " + e.getMessage());
		}

	}

	public static void doRoutePost(String androidID, List<Waypoint> route) {
		// Log.v(LOG_LABEL, LOG_MSG_PREFIX + "Entering HTTPPoster.doRoutePost");
		final HttpClient c = new DefaultHttpClient();
		final HttpPost post = new HttpPost(SERVER + PATH);
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");

		StringBuffer params = new StringBuffer();

		// Log.v(LOG_LABEL, LOG_MSG_PREFIX + "Constructing parameter string");
		params.append("type=route&id=" + androidID);

		for (Waypoint w : route) {
			params.append("&lat=" + w.getLatitude() + "&lon="
					+ w.getLongitude() + "&timert=" + w.getTime());
		}

		// Log.v(LOG_LABEL, LOG_MSG_PREFIX + "Created parameter string: " +
		// params.toString());

		post.setEntity(new ByteArrayEntity(params.toString().getBytes()));

		// Log.i(LOG_LABEL, LOG_MSG_PREFIX + "Executing post to " +
		// VUphone.SERVER + PATH);
		// Log.d(LOG_LABEL, LOG_MSG_PREFIX + "Spawning thread for HTTP post");
		new Thread(new Runnable() {

			public void run() {
				HttpResponse resp;
				try {
					resp = c.execute(post);
					ByteArrayOutputStream bao = new ByteArrayOutputStream();
					resp.getEntity().writeTo(bao);
					// Log.d(LOG_LABEL, LOG_MSG_PREFIX +
					// "Response from server: "
					// + new String(bao.toByteArray()));

				} catch (ClientProtocolException e) {
					// Log.e(LOG_LABEL, LOG_MSG_PREFIX
					// + "ClientProtocolException executing post: "
					// + e.getMessage());
				} catch (IOException e) {
					// Log.e(LOG_LABEL, LOG_MSG_PREFIX
					// + "IOException writing to ByteArrayOutputStream: "
					// + e.getMessage());
				} catch (Exception e) {
					// Log.e(LOG_LABEL, LOG_MSG_PREFIX
					// + "Other Exception of type:" + e.getClass());
					// Log.e(LOG_LABEL, LOG_MSG_PREFIX + "The message is: "
					// + e.getMessage());
				}
			}

		}).start();
		// Log.d(LOG_LABEL, LOG_MSG_PREFIX + "Thread for HTTP post started");

		// Log.v(LOG_LABEL, LOG_MSG_PREFIX + "Leaving HTTPPoster.doRoutePost");

	}

	public static void main(String[] args) {

		double lat, lon;

		for (int i = 0; i < 5000; ++i) {
			lat = 36.0 + Math.random();
			lon = -87.0 + Math.random();

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				--i;
			}

			final String aid = "myAndroidID" + i;
			doAccidentPost(aid, System.currentTimeMillis(), 87.23, 35.74, lat,
					lon);
			System.out.println("Added " + lat + ", " + lon);

		}
	}

}
