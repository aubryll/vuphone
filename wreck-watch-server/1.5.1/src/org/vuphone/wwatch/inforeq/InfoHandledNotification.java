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
package org.vuphone.wwatch.inforeq;

import java.util.ArrayList;

import org.vuphone.wwatch.notification.Notification;

/**
 * This is generated by the InfoHandler, as it fetches data from the database.
 * This is returned to the main NotificationHandler, who calls the
 * getReturnString function and sends the result of that back to the client
 * 
 * @author hamiltont
 * 
 */
public class InfoHandledNotification extends Notification {

	private ArrayList<Wreck> accidents_;
	private String response_;

	public InfoHandledNotification() {
		super("infohandled");
		accidents_ = new ArrayList<Wreck>();
	}
	
	public void addWreck(Wreck w){
		accidents_.add(w);
	}
	
	public void addWreck(double lat, double lon, int id, long time){
		accidents_.add(new Wreck(lat, lon, id, time));
	}

	
	@Override
	public String getResponseString() {
		return response_;
	}

	protected void setResponse(String resp) {
		response_ = resp;
	}
	

}
