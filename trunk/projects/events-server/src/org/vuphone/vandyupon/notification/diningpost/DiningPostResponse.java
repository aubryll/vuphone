package org.vuphone.vandyupon.notification.diningpost;

import org.vuphone.vandyupon.datastructs.Rating;
import org.vuphone.vandyupon.notification.ResponseNotification;

public abstract class DiningPostResponse extends ResponseNotification {
	
	private Rating rating_;
	
	public DiningPostResponse(String type, String responseType, String callback){
		super(type, responseType, callback);
	}
	
	public Rating getRating()
	{
		return rating_;
	}

	public void setRating(Rating r)
	{
		rating_ = r;
	}

}
