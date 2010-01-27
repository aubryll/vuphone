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
package org.vuphone.vandyupon.utils;

import org.vuphone.vandyupon.notification.Notification;
import org.vuphone.vandyupon.notification.ResponseNotification;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * This class is a factory for creating emitters using in response handlers.
 * Currently this factory can create XML or JSON objects.  There is also a
 * convenience method that prepopulates some of the commonly omitted fields
 * so that users can avoid dealing with them.
 * @author Chris Thompson
 *
 */
public class EmitterFactory {

	/**
	 * This enum defines the response types supported by this factory.
	 * This could be later increased to other emitter types.
	 * @author Chris Thompson
	 *
	 */
	public enum ResponseType{
		JSON, XML
	}

	/**
	 * This factory method creates an Xstream object preconfigured to emit the proper
	 * object type.  
	 * @param type
	 * @return XStream object configured for the proper object type
	 */
	public static XStream createXStream(ResponseType type){

		XStream temp;

		switch (type){
		case JSON:{
			temp = new XStream (new JettisonMappedXmlDriver());
			temp.setMode(XStream.NO_REFERENCES);
			break;
		}
		case XML:{
			temp = new XStream(new DomDriver("UTF-8"));
			break;
		}
		default:{
			temp = new XStream(new DomDriver("UTF-8"));
		}

		}

		return temp;

	}

	/**
	 * This is a convenience method designed to prepopulate the emitter with
	 * commonly omitted fields.  This is equivalent to calling 
	 * EmitterFactory.createXStream(ResponseType) and then adding the omit's
	 * on the returned object.
	 * @param type
	 * @param prepopulate
	 * @return
	 */
	public static XStream createXStream(ResponseType type, boolean prepopulate){

		XStream temp = createXStream(type);
		if (prepopulate){
			temp.omitField(Notification.class, "type_");
			temp.omitField(ResponseNotification.class, "type_");
			temp.omitField(ResponseNotification.class, "responseType_");
		}

		return temp;

	}

}
