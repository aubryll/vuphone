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
package org.vuphone.vandyupon.notification.eventmetarequest;

import java.util.ArrayList;

import org.vuphone.vandyupon.datastructs.MetaDataContainer;
import org.vuphone.vandyupon.notification.ResponseNotification;

public class EventMetaRequestResponse extends ResponseNotification {
	
	private ArrayList<MetaDataContainer> meta_ = new ArrayList<MetaDataContainer>();
	private int event_;
	
	public EventMetaRequestResponse(int id, String responsetype, String callback){
		super("eventmeta", responsetype, callback);
		event_ = id;
	}
	
	public void addMeta(String type, String value){
		meta_.add(new MetaDataContainer(type, value));
	}
	
	public int getEvent(){
		return event_;
	}
	
	public ArrayList<MetaDataContainer> getMetaData(){
		return meta_;
	}
}
