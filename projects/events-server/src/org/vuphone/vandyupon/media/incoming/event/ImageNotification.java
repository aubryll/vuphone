 /**************************************************************************
 * Copyright 2009 Chris Thompson                                           *
 *                                                                         *
 * Licensed under the Apache License, Version 2.0 (the "License");         *
 * you may not use this file except in compliance with the License.        *
 * You may obtain a copy of the License at                                 *
 *                                                                         *
 * http://www.apache.org/licenses/LICENSE-2.0                              *
 *                                                                         *
 * Unless required by applicable law or agreed to in writing, software     *
 * distributed under the License is distributed on an "AS IS" BASIS,       *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.*
 * See the License for the specific language governing permissions and     *
 * limitations under the License.                                          *
 **************************************************************************/
package org.vuphone.vandyupon.media.incoming.event;

import org.vuphone.vandyupon.notification.Notification;

public class ImageNotification extends Notification {
	
	private byte[] imageBytes_;
	private long eventId_;
	private long time_;
	private String responseType_;
	private String callback_;
	
	public ImageNotification(){
		super("eventimagepost");
	}
	
	public void setBytes(byte[] bytes){
		imageBytes_ = bytes;
	}
	
	public byte[] getBytes()
	{
		return imageBytes_;
	}
	
	public void setEventId(long eventId)
	{
		this.eventId_ = eventId;
	}
	
	public long getEventId()
	{
		return eventId_;
	}
	
	public void setTime(long time)
	{
		time_ = time;
	}
	
	public long getTime()
	{
		return time_;
	}
	
	public void setResponseType(String resp){
		responseType_ = resp;
	}
	
	public String getResponseType(){
		return responseType_;
	}
	
	public void setCallback(String cb){
		callback_ = cb;
	}
	
	public String getCallback(){
		return callback_;
	}
}
