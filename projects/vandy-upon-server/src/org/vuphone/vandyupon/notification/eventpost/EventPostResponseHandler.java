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
package org.vuphone.vandyupon.notification.eventpost;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.vuphone.vandyupon.notification.HandlerFailedException;
import org.vuphone.vandyupon.notification.InvalidFormatException;
import org.vuphone.vandyupon.notification.NotificationResponseHandler;
import org.vuphone.vandyupon.notification.ResponseNotification;
import org.vuphone.vandyupon.utils.Emitter;
import org.vuphone.vandyupon.utils.FieldAliasContainer;
import org.vuphone.vandyupon.utils.ObjectAliasContainer;

public class EventPostResponseHandler extends NotificationResponseHandler {

	private Map<String, Emitter> emitters_;

	public EventPostResponseHandler() {
		super("eventpost");

	}

	public Map<String, Emitter> getEmitters(){
		return emitters_;
	}

	@Override
	public void handle(HttpServletResponse resp, ResponseNotification nr) throws HandlerFailedException {
		EventPostResponse epr;

		if (nr instanceof EventPostResponse){
			epr = (EventPostResponse)nr;
		}else{
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(new InvalidFormatException());
			throw hfe;
		}
		ArrayList<FieldAliasContainer> fields = new ArrayList<FieldAliasContainer> ();
		fields.add(new FieldAliasContainer("eventid", EventPostResponse.class, "id_"));
		ArrayList<ObjectAliasContainer> objects = new ArrayList<ObjectAliasContainer>();
		objects.add(new ObjectAliasContainer("EventPostResponse", EventPostResponse.class));

		String response = emitters_.get(nr.getResponseType()).emit(nr, objects, fields);

		if (nr.getResponseType().equalsIgnoreCase("json")){
			response = epr.getCallback() + "( " + response + " )";
		}

		try {
			resp.getWriter().write(response);
		} catch (IOException e) {
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(e);
			throw hfe;
		}


	}

	public void setEmitters(Map<String, Emitter> e){
		emitters_ = e;
	}

}
