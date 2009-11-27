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
package org.vuphone.vandyupon.notification.eventrequest;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.vuphone.vandyupon.datastructs.Event;
import org.vuphone.vandyupon.datastructs.Location;
import org.vuphone.vandyupon.notification.HandlerFailedException;
import org.vuphone.vandyupon.notification.InvalidFormatException;
import org.vuphone.vandyupon.notification.Notification;
import org.vuphone.vandyupon.notification.NotificationResponseHandler;
import org.vuphone.vandyupon.notification.ResponseNotification;
import org.vuphone.vandyupon.utils.EmitterFactory;

import com.thoughtworks.xstream.XStream;

public class EventRequestResponseHandler extends NotificationResponseHandler {

	public EventRequestResponseHandler() {
		super("eventrequestresponse");
	}

	@Override
	public void handle(HttpServletResponse resp, ResponseNotification nr)
	throws HandlerFailedException {
		if (!(nr instanceof EventRequestResponse)) {
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(new InvalidFormatException());
			throw hfe;
		}
		EventRequestResponse err = (EventRequestResponse)nr;

		XStream emitter;
		if (err.getResponseType().equalsIgnoreCase("json")) {
			emitter = EmitterFactory.createXStream(EmitterFactory.ResponseType.JSON);
		} else {
			emitter = EmitterFactory.createXStream(EmitterFactory.ResponseType.XML);
		}
		
		
		emitter.omitField(ResponseNotification.class, "type_");
		emitter.omitField(ResponseNotification.class, "responseType_");
		emitter.omitField(Notification.class, "type_");
		emitter.omitField(ResponseNotification.class, "callback_");
		
		emitter.alias("EventRequestResponse", EventRequestResponse.class);
		emitter.alias("Event", Event.class);
		emitter.addImplicitCollection(EventRequestResponse.class, "events_");
		
		emitter.alias("Loc", Location.class);
		emitter.aliasField("Name", Location.class, "name_");
		emitter.aliasField("Lat", Location.class, "lat_");
		emitter.aliasField("Lon", Location.class, "lon_");
		
		emitter.aliasField("Name", Event.class, "name_");
		emitter.aliasField("Loc", Event.class, "loc_");
		emitter.aliasField("User", Event.class, "user_");
		emitter.aliasField("Start", Event.class, "startTime_");
		emitter.aliasField("End", Event.class, "endTime_");
		emitter.aliasField("EventId", Event.class, "id_");
		emitter.aliasField("Owner", Event.class, "owner_");
		emitter.aliasField("LastUpdate", Event.class, "lastUpdate_");
//		emitter.aliasField("Description", Event.class, "description_");
		emitter.omitField(Event.class, "description_");
		emitter.omitField(Event.class, "sourceUid_");
		
		String response = emitter.toXML(err);
		
		if (err.getResponseType().equalsIgnoreCase("json"))
			response = err.getCallback() + " (" + response + ") ";
		
		try {
			resp.getWriter().write(response);
		} catch(IOException e) {
			e.printStackTrace();
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(e);
			throw hfe;
		}
	}

}
