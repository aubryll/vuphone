/**************************************************************************
 * Copyright 2010 Esubalew T. Bekele                                           *
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

package org.vuphone.vandyupon.notification.diningrating;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.vuphone.vandyupon.notification.HandlerFailedException;
import org.vuphone.vandyupon.notification.InvalidFormatException;
import org.vuphone.vandyupon.notification.Notification;
import org.vuphone.vandyupon.notification.NotificationResponseHandler;
import org.vuphone.vandyupon.notification.ResponseNotification;
import org.vuphone.vandyupon.utils.EmitterFactory;
import com.thoughtworks.xstream.XStream;

/**
 * This is a dining response handler that serializes 
 * the dining response object. 
 * @see DiningRatingRequestResponse
 * @see DiningRatingResponse
 * @author Esubalew T. Bekele
 */
public class DiningRatingResponseHandler extends NotificationResponseHandler {
	// it is a member because if a user wants to externally omit fields
	// or make an alias so that this handler might be generic for the 
	// future.
	private XStream serializer_; /*<- the XML serializer*/
	
	public DiningRatingResponseHandler() {
		super("diningrating");
	}
	/** 
	 * This the actual handler method that handles the response
	 * @param resp the servlet response 
	 * @param nr the actual response (either {@link DiningRatingResponse}
	 * or {@link DiningRatingRequestResponse}) 
	 * @see NotificationResponseHandler
	 */
	@Override
	public void handle(HttpServletResponse resp, ResponseNotification nr)
			throws HandlerFailedException {
		ResponseNotification ratingResponse;
		
		// check whether the response notification is of the responses
		// that this handler can handle and if not throw hfe exception
		if (nr instanceof DiningRatingResponse){
			ratingResponse = (DiningRatingResponse)nr;
		}
		else if(nr instanceof DiningRatingRequestResponse){
			ratingResponse = (DiningRatingRequestResponse)nr;
		}
		else{
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(new InvalidFormatException());
			throw hfe;
		}
		// create the serializer based on the type of output response format
		if(ratingResponse.getResponseType().equalsIgnoreCase("json")){
			serializer_ = EmitterFactory.createXStream(EmitterFactory.ResponseType.JSON);
		}else {
			serializer_ = EmitterFactory.createXStream(EmitterFactory.ResponseType.XML);
		}
		// rename class and field names to be readable in the XML
		serializer_.alias("DiningRatingResponse", DiningRatingResponse.class);
		serializer_.alias("DiningRatingRequestResponse", 
				DiningRatingRequestResponse.class);
		serializer_.aliasField("status", DiningRatingResponse.class,
				"return_status_");
		serializer_.aliasField("rating", DiningRatingRequestResponse.class,
				"rating_");
		serializer_.omitField(ResponseNotification.class, "type_");
		serializer_.omitField(ResponseNotification.class, "responseType_");
		serializer_.omitField(Notification.class, "type_");
		serializer_.omitField(ResponseNotification.class, "callback_");
		// serialize the rating response to XML
		String response = serializer_.toXML(ratingResponse);
		// if the output format is JSON, change the XML response to JSON format
		if (ratingResponse.getResponseType().equalsIgnoreCase("json"))
			response = ratingResponse.getCallback() + "( " + response + " )";
        // try the servlet writer and if not successful throw hfe exception
		// hfe is too much generic such that there is no way to tell
		// if it is a servlet failure or a bad type exception from outside
		// should be specialized for the future
		try {
			resp.getWriter().write(response);
		} catch (IOException e) {
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(e);
			throw hfe;
		}

	}

}
