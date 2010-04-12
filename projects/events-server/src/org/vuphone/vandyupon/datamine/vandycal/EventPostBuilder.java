/**
 * 
 */
package org.vuphone.vandyupon.datamine.vandycal;

import java.io.IOException;
import java.util.ArrayList;

import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.Summary;

import org.vuphone.vandyupon.datastructs.Location;
import org.vuphone.vandyupon.notification.eventpost.EventPost;

/**
 * @author Hamilton Turner
 * 
 */
// TODO - Make this return null if a critical piece of info is missing
public class EventPostBuilder {
	public static final Location vandy = new Location(36.1419303, -86.8044586);
	public static final Location nashville = new Location(36.1658899,
			-86.7844432);

	public static EventPost build(Component calendarComponent) {
		EventPost p = new EventPost();

		setLocation(p, calendarComponent);

		setName(p, calendarComponent);

		setDescription(p, calendarComponent);

		setStartAndEndTime(p, calendarComponent);

		setTags(p, calendarComponent);

		setSourceUserID(p, calendarComponent);

		return p;
	}

	private static void setSourceUserID(EventPost p, Component calendarComponent) {
		p.setSourceUid(calendarComponent.getProperty(Property.UID).getValue());
	}

	private static void setStartAndEndTime(EventPost p,
			Component calendarComponent) {

		// get start time
		DtStart start = (DtStart) calendarComponent
				.getProperty(Property.DTSTART);
		if (start != null)
			p.setStartTime(start.getDate().getTime() / 1000);

		// get end time
		DtEnd end = (DtEnd) calendarComponent.getProperty(Property.DTEND);

		if (end != null)
			p.setEndTime(end.getDate().getTime() / 1000);
		else if (start != null)
			p.setEndTime(p.getStartTime());
		else
			return; // We have a problem here!
	}

	private static void setDescription(EventPost p, Component calendarComponent) {
		// get description
		Description desc = (Description) calendarComponent
				.getProperty(Property.DESCRIPTION);
		if (desc != null)
			// Strip backslashes before colons that occur in the ics file
			p.setDescription(desc.getValue().replaceAll("\\\\", ""));
	}

	private static void setName(EventPost p, Component calendarComponent) {
		Summary name = (Summary) calendarComponent
				.getProperty(Property.SUMMARY);
		if (name != null)
			// Strip backslashes before colons that occur in the ics file
			p.setName(name.getValue().replaceAll("\\\\", ""));
	}

	private static void setTags(EventPost p, Component c) {
		Property categories = c.getProperty(Property.CATEGORIES);
		if (categories == null)
			return;

		String categoryValue = categories.getValue();

		if (categoryValue == null)
			return;

		String[] tagArray = categoryValue.split(",");
		ArrayList<String> tags = new ArrayList<String>();
		for (String s : tagArray)
			tags.add(s);

		p.setTags(tags);
	}

	private static void setLocation(EventPost p, Component c) {
		Property locName = c.getProperty(Property.LOCATION);
		if (locName != null)
			// Strip backslashes before colons that occur in the ics file
			p.setLocationName(locName.getValue().replaceAll("\\\\", ""));
		else {
			p.setLocationName(null);
			return;
		}

		// get geolocation
		Location location = getLocation(locName.getValue());
		if (location != null)
			p.setLocation(location);
	}

	private static Location getLocation(String locationName) {

		Location geoLocation = null;

		// Remove the extra Room info and what not
		String locValue = locationName;
		if (locValue.indexOf(",") != -1)
			locValue = locValue.substring(0, locValue.indexOf(","));
		if (locValue.indexOf("-") != -1)
			locValue = locValue.substring(0, locValue.indexOf("-"));

		// Get a Lat / Lon from the String value
		try {
			geoLocation = Geocoder.getLocation(locValue);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		if (geoLocation.equals(vandy))
			System.out.println("Got: Vandy");
		else if (geoLocation.equals(nashville))
			System.out.println("Got: Nash");
		else
			System.out.println("Got: " + geoLocation.getLat() + ", "
					+ geoLocation.getLon());

		if ((geoLocation.getLat() == 0) || (geoLocation.getLon() == 0))
			return null;
		return geoLocation;
	}
}
