package org.vuphone.wwatch.android;

import android.location.Location;

/**
 * A wrapper class that holds the information about the image to be uploaded.
 * This class provides setter and getter methods for the actual data as well as
 * a toString() method that returns the information as an URL formatted string.
 * The main reason for this class is the setMetaInformation() callback method in
 * the ImageUploaderListener interface.
 * 
 * @author Krzysztof Zienkiewicz
 * 
 */
public class ImageUploadMetaInformation {
	/** UTC time */
	private long time_;
	/** Location in degrees */
	private double longitude_, latitude_;

	/**
	 * Sets the default values for the member fields.
	 */
	public ImageUploadMetaInformation() {
		time_ = 0;
		longitude_ = latitude_ = 0.0;
	}

	/**
	 * Get the latitude.
	 * 
	 * @return
	 */
	public double getLatitude() {
		return latitude_;
	}

	/**
	 * Get the longitude.
	 * 
	 * @return
	 */
	public double getLongitude() {
		return longitude_;
	}

	/**
	 * Get the time.
	 * 
	 * @return
	 */
	public long getTime() {
		return time_;
	}

	/**
	 * Set the latitude.
	 * 
	 * @param latitude
	 */
	public void setLatitude(double latitude) {
		latitude_ = latitude;
	}

	/**
	 * Set the latitude and the longitude based on the Location object
	 * parameter. If loc is null, sets longitude and latitude to 0.
	 * 
	 * @param loc
	 */
	public void setLocation(Location loc) {
		if (loc == null)
			longitude_ = latitude_ = 0.0;
		else {
			longitude_ = loc.getLongitude();
			latitude_ = loc.getLatitude();
		}
	}

	/**
	 * Set the longitude.
	 * @param longitude
	 */
	public void setLongitude(double longitude) {
		longitude_ = longitude;
	}

	/**
	 * Set the time.
	 * @param time
	 */
	public void setTime(long time) {
		time_ = time;
	}

	/**
	 * Return the representation of this object as a URL formatted string.
	 */
	public String toString() {
		return "time=" + Long.toString(time_) + "&longitude="
				+ Double.toString(longitude_) + "&latitude="
				+ Double.toString(latitude_);
	}
}