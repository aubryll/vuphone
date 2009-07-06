 /**************************************************************************
 * Copyright 2009 Scott Campbell                                           *
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
package org.vuphone.wwatch.media.outgoing;

import org.vuphone.wwatch.notification.Notification;

public class ImageRequestNotification extends Notification{

	private int wreckID_ = -1;
	private boolean isFull_ = false;
	private int imageID_ = -1;
	
	public ImageRequestNotification() {
		super("imageRequest");
	}

	public void setIsFullImage(boolean b) {
		isFull_ = b;
	}
	
	public void setImageID(int id) {
		imageID_ = id;
	}
	
	public void setWreckID(int id) {
		wreckID_ = id;
	}
	
	public int getWreckID() {
		return wreckID_;
	}
	
	public boolean isFullImage() {
		return isFull_;
	}
	
	public int getImageID() {
		return imageID_;
	}
	
	public String getResponseString() {
		return "";
	}

}
