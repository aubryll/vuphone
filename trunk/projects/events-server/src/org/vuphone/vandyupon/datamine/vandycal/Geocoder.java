package org.vuphone.vandyupon.datamine.vandycal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.filter.ElementFilter;
import org.jdom.input.SAXBuilder;
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

	/**
	 *  @todo Externalize this property
	 */
	
//	private static final String googleApiKey = "";
	private static final String whitePagesApiKey = "338334e34d7ffb4451f48def3b88fa6b";

	public static Location getLocation(String address) throws IOException {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new URL(
					"http://maps.google.com/maps/geo?output=csv&sensor=false&q="
							+ URLEncoder.encode(address+ ",Nashville,TN", ENCODING)).openStream()));
			String line;
			int statusCode = -1;
			while ((line = in.readLine()) != null) {
				// Format: 200,6,42.730070,-73.690570
				statusCode = Integer.parseInt(line.substring(0, 3));
				if (statusCode == 200) {
					line = line.substring(6);
					final String lat = line.substring(0, line.indexOf(","));
					final String lon = line.substring(line.indexOf(",") + 1);

					if (lat.equals("36.1419303") && lon.equals("-86.8044586")) {
						// Got the entire VU campus
						break;
					} else if (lat.equals("36.1658899") && lon.equals("-86.7844432")) {
						// Got all of Nashville
						break;
					}
					
					return new Location(address, Double.parseDouble(lat), Double.parseDouble(lon));
				}
			}

			// Google failed to find it, so try WhitePages
			String uriString = "http://api.whitepages.com/find_business/1.0/"
				+ "?businessname=" + URLEncoder.encode(address, ENCODING)
				+ "&zip=37235"
				+ "&api_key=" + whitePagesApiKey;

			String lat = null;
			String lon = null;

			// Get the latitude and longitude from the XML response
			SAXBuilder builder = new SAXBuilder("org.apache.xerces.parsers.SAXParser");
			Document doc = (Document)builder.build(new URL(uriString));

			// Wait 1/2 second between requests because the geocoding service doesn't allow more than 2 requests/second
			Thread.sleep(500);

			Iterator descendants = doc.getDescendants(new ElementFilter());

			// Iterate through the descendants until we've found a latitude and longitude
			while(descendants.hasNext() && (lat==null || lon==null)) {
				Element descendent = (Element)descendants.next();
				if (descendent.getName().equals("latitude")) {
					lat = descendent.getValue();
				} else if (descendent.getName().equals("longitude")) {
					lon = descendent.getValue();
				}
			}
			
			if (lat != null && lon != null) {
				return new Location(address, Double.parseDouble(lat), Double.parseDouble(lon));
			} else {
				throw new IOException("Could not find location");
			}
		} catch (JDOMException e) {
			throw new IOException("Error when parsing geocoding server response");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
