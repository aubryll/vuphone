package org.vuphone.wwatch.android.media;

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

	/** ID of the wreck */
	private long id_;

	/**
	 * Sets the default values for the member fields.
	 */
	public ImageUploadMetaInformation() {
		time_ = id_ = 0;
	}

	/**
	 * Get the id.
	 * 
	 * @return
	 */
	public long getId() {
		return id_;
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
	 * Set the id.
	 * 
	 * @param id
	 */
	public void setId(long id) {
		id_ = id;
		;
	}

	/**
	 * Set the time.
	 * 
	 * @param time
	 */
	public void setTime(long time) {
		time_ = time;
	}

	/**
	 * Return the representation of this object as a URL formatted string.
	 */
	public String toString() {
		return "time=" + Long.toString(time_) + "&wreckid="
				+ Long.toString(id_);
	}
}