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
package org.vuphone.wwatch.media.incoming;

import javax.servlet.http.HttpServletRequest;

import org.vuphone.wwatch.notification.Notification;

public class ImageNotification extends Notification {
	
	private byte[] imageBytes;
	private long wreckId;
	private long time;
	
	public ImageNotification(){
		super("image");
	}
	
	public void setBytes(byte[] bytes){
		imageBytes = bytes;
	}
	
	public byte[] getBytes()
	{
		return imageBytes;
	}
	
	public void setWreckId(long wreckId)
	{
		this.wreckId = wreckId;
	}
	
	public long getWreckId()
	{
		return wreckId;
	}
	
	public void setTime(long time)
	{
		this.time = time;
	}
	
	public long getTime()
	{
		return time;
	}
}
