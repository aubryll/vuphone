package org.vuphone.vandyupon.utils;

import java.util.ArrayList;

import org.vuphone.vandyupon.notification.ResponseNotification;

public abstract class Emitter {
	
	private String type_;
	
	public Emitter(String type){
		type_ = type;
	}
	
	public abstract String emit(ResponseNotification rn, ArrayList<ObjectAliasContainer> objaliases,
			ArrayList<FieldAliasContainer> fldaliases);
	
	public String getType(){
		return type_;
	}
}