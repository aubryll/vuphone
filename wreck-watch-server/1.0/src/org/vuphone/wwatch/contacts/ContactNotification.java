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
package org.vuphone.wwatch.contacts;

import org.vuphone.wwatch.notification.Notification;

public class ContactNotification extends Notification {
	
	private String[] numbers_;
	private String androidId_;
	
	public ContactNotification(String androidID){
		super("contact");
		androidId_ = androidID;
	}
	
	public ContactNotification() {
		super("contact");
	}

	public void setNumbers(String[] number){
		numbers_ = number;
	}
	
	public String[] getNumbers(){
		return numbers_;
	}
	
	public String getAndroidID(){
		return androidId_;
	}
	
	public void setAndroidID(String id)
	{
		androidId_ = id;
	}

	@Override
	public String getResponseString() {
		return "";
	}

}
