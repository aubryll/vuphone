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

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.vuphone.vandyupon.datastructs.MetaDataContainer;
import org.vuphone.vandyupon.notification.HandlerFailedException;
import org.vuphone.vandyupon.notification.InvalidFormatException;
import org.vuphone.vandyupon.notification.NotificationResponseHandler;
import org.vuphone.vandyupon.notification.ResponseNotification;
import org.vuphone.vandyupon.utils.EmitterFactory;

import com.thoughtworks.xstream.XStream;

public class EventMetaRequestResponseHandler extends NotificationResponseHandler {

	public EventMetaRequestResponseHandler(){
		super("eventmetarequest");
	}

	@Override
	public void handle(HttpServletResponse resp, ResponseNotification nr)
	throws HandlerFailedException {
		if (!(nr instanceof EventMetaRequestResponse)){
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(new InvalidFormatException());
			throw hfe;
		}

		EventMetaRequestResponse emrr = (EventMetaRequestResponse)nr;
		XStream emitter;
		if (emrr.getResponseType().equalsIgnoreCase("json")){
			emitter	= EmitterFactory.createXStream(EmitterFactory.ResponseType.JSON);
		}else{
			emitter = EmitterFactory.createXStream(EmitterFactory.ResponseType.XML);
		}
		
		emitter.addImplicitCollection(EventMetaRequestResponse.class, "meta_");
		emitter.alias("MetaData", MetaDataContainer.class);
		emitter.aliasField("Type", MetaDataContainer.class, "type_");
		emitter.aliasField("Value", MetaDataContainer.class, "value_");
		
		String response = emitter.toXML(emrr);
		if (emrr.getResponseType().equalsIgnoreCase("json")){
			response = emrr.getCallback() + " (" + response + ")";
		} 
		
		try{
			resp.getWriter().write(response);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
