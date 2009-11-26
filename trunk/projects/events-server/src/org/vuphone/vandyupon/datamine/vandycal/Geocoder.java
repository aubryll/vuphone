package org.vuphone.vandyupon.datamine.vandycal;

import java.io.IOException;
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
	private static final String whitePagesApiKey = "338334e34d7ffb4451f48def3b88fa6b";

	public static Location getLocation(String address) throws IOException {
		try {
			/* Relevant XML in response:
			<wp:wp xmlns:wp="http://api.whitepages.com/schema/">
			  <wp:result wp:type="success" wp:message=" " wp:code="Found Data"/>
			  <wp:listings>
			    <wp:listing>
			      <wp:geodata>
			        <wp:latitude>36.158181</wp:latitude>
			        <wp:longitude>-86.783578</wp:longitude>
			      </wp:geodata>
			    </wp:listing>
			  </wp:listings>
			</wp:wp>
			 */

			String uriString = "http://api.whitepages.com/find_business/1.0/"
				+ "?businessname=" + URLEncoder.encode(address, ENCODING)
				+ "&zip=37235"
				+ "&api_key=" + whitePagesApiKey;

			String lat = null;
			String lon = null;

			// Get the latitude and longitude from the XML response
			SAXBuilder builder = new SAXBuilder("org.apache.xerces.parsers.SAXParser");
			Document doc = (Document)builder.build(new URL(uriString));
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
				return new Location(Double.parseDouble(lat), Double.parseDouble(lon));
			} else {
				throw new IOException("Could not find location");
			}
		} catch (JDOMException e) {
			throw new IOException("Error when parsing geocoding server response");
		}
	}
}
