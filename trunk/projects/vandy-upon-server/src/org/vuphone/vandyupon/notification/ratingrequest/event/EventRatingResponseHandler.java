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
package org.vuphone.vandyupon.notification.ratingrequest.event;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.vuphone.vandyupon.notification.HandlerFailedException;
import org.vuphone.vandyupon.notification.InvalidFormatException;
import org.vuphone.vandyupon.notification.NotificationResponseHandler;
import org.vuphone.vandyupon.notification.ResponseNotification;
import org.vuphone.vandyupon.utils.EmitterFactory;

import com.thoughtworks.xstream.XStream;

public class EventRatingResponseHandler extends NotificationResponseHandler{

	public EventRatingResponseHandler() {
		super("eventratingrequestresponse");
	}

	@Override
	public void handle(HttpServletResponse resp, ResponseNotification nr)
			throws HandlerFailedException {
		
		if (!(nr instanceof EventRatingResponse)){
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(new InvalidFormatException());
			throw hfe;
		}
		
		
		XStream emitter;
		
		if (nr.getResponseType().equalsIgnoreCase("json"))
			emitter = EmitterFactory.createXStream(EmitterFactory.ResponseType.JSON, true);
		else
			emitter = EmitterFactory.createXStream(EmitterFactory.ResponseType.XML, true);
		
		emitter.addImplicitCollection(EventRating.class, "ratings_");
		emitter.alias("Ratings", EventRating.class);
		emitter.alias("EventRatingReqResp", EventRatingResponse.class);
		emitter.aliasField("EventId", EventRatingResponse.class, "id_");
		emitter.alias("Rating", EventRatingContainer.class);
		emitter.aliasField("Comment", EventRatingContainer.class, "comment_");
		emitter.aliasField("SubmittedBy", EventRatingContainer.class, "user_");
		emitter.aliasField("Value", EventRatingContainer.class, "value_");
		emitter.aliasField("VotesUp", EventRating.class, "totalUp_");
		emitter.aliasField("VotesDown", EventRating.class, "totalDown_");
		emitter.aliasField("Average",EventRating.class, "avg_");
		
		String response = emitter.toXML(nr);
		
		if (nr.getResponseType().equalsIgnoreCase("json"))
			response = nr.getCallback() + " (" + response + ") ";
		
		try{
			resp.getWriter().write(response);
		}catch(IOException e){
			e.printStackTrace();
		}
		
	}

}
