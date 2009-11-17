/**
 * 
 */
package org.vuphone.vandyupon.datamine.vandycal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import org.vuphone.vandyupon.datastructs.Location;

/**
 * @author hamiltont
 * 
 *         Minor modifications to original code. Original found at {@link http
 *         ://
 *         unserializableone.blogspot.com/2007/08/lightweight-google-geocoder-
 *         with-java.html}
 */
public class Geocoder {
	private final static String ENCODING = "UTF-8";

	/*
	 * If you would like to use a GoogleAPI key, put it in this var, and append
	 * it to the URL as &key=KEY private final static String KEY = "xyz";
	 */
//
//	public static class Location {
//		public float lon, lat;
//
//		private Location(String lat, String lon) {
//			this.lon = Float.parseFloat(lon);
//			this.lat = Float.parseFloat(lat);
//		}
//
//		public float getLat() {
//			return lat;
//		}
//
//		public float getLon() {
//			return lon;
//		}
//
//		public String toString() {
//			return "Lat: " + lat + ", Lon: " + lon;
//		}
//	}

	public static Location getLocation(String address) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(new URL(
				"http://maps.google.com/maps/geo?output=csv&q="
						+ URLEncoder.encode(address, ENCODING)).openStream()));
		String line;
		Location location = null;
		int statusCode = -1;
		while ((line = in.readLine()) != null) {
			// Format: 200,6,42.730070,-73.690570
			statusCode = Integer.parseInt(line.substring(0, 3));
			if (statusCode == 200) {
				line = line.substring(6);
				final String lat = line.substring(0, line.indexOf(","));
				final String lon = line.substring(line.indexOf(",") + 1);

				location = new Location(Double.parseDouble(lat), Double.parseDouble(lon));
			}
		}
		if (location == null) {
			switch (statusCode) {
			case 400:
				throw new IOException("Bad Request, returned 400");
			case 500:
				throw new IOException(
						"Unknown error from Google Encoder, returned 500");
			case 601:
				throw new IOException("Missing query, returned 601");
			case 602:
				return null;
			case 603:
				throw new IOException("Legal problem, retunred 603");
			case 604:
				throw new IOException("No route, returned 604");
			case 610:
				throw new IOException("Bad key, returned 610");
			case 620:
				throw new IOException("Too many queries, returned 620");
			}
		}
		return location;
	}

	public static void main(String[] argv) throws Exception {
		System.out.println(Geocoder.getLocation("New York"));
	}
}
