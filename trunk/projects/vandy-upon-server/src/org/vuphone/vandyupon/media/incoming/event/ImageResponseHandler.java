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
package org.vuphone.vandyupon.media.incoming.event;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.vuphone.vandyupon.notification.HandlerFailedException;
import org.vuphone.vandyupon.notification.InvalidFormatException;
import org.vuphone.vandyupon.notification.NotificationResponseHandler;
import org.vuphone.vandyupon.notification.ResponseNotification;
import org.vuphone.vandyupon.utils.EmitterFactory;

import com.thoughtworks.xstream.XStream;

public class ImageResponseHandler extends NotificationResponseHandler {
	
	public ImageResponseHandler(){
		super("eventimagepost");
	}

	@Override
	public void handle(HttpServletResponse resp, ResponseNotification nr)
			throws HandlerFailedException {
		
		if (!(nr instanceof ImageHandledNotification)){
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(new InvalidFormatException());
			throw hfe;
		}
		
		ImageHandledNotification ihn = (ImageHandledNotification) nr;
		XStream emitter;
		if (ihn.getResponseType().equalsIgnoreCase("json")){
			emitter = EmitterFactory.createXStream(EmitterFactory.ResponseType.JSON, true);
		}else{
			emitter = EmitterFactory.createXStream(EmitterFactory.ResponseType.XML, true);
		}
		
		emitter.alias("ImageHandledNotification", ImageHandledNotification.class);
		emitter.aliasField("EventId", ImageHandledNotification.class, "eventId_");
		emitter.aliasField("FileName", ImageHandledNotification.class, "fileName_");
		emitter.aliasField("Time", ImageHandledNotification.class, "time_");
		
		String response = emitter.toXML(ihn);
		
		if (ihn.getResponseType().equalsIgnoreCase("json"))
			response = ihn.getCallback() + " (" + response + ") ";
		
		try {
			resp.getWriter().write(response);
		}catch(IOException e){
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(e);
			throw hfe;
		}
	}

}
