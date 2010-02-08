package org.vuphone.vandyupon.notification.diningpost;

import org.vuphone.vandyupon.notification.ResponseNotification;

public class DiningPostResponse extends ResponseNotification {
	private int id_;
	
	public DiningPostResponse(int id, String responseType, String callback){
		super("diningpost", responseType, callback);
		id_ = id;
	}
	
	public int getId(){
		return id_;
	}


}
