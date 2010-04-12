/*******************************************************************************
 * Copyright 2009 Jules White
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package org.vuphone.vandyupon;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cometd.Bayeux;
import org.cometd.Channel;
import org.cometd.Client;
import org.cometd.Message;
import org.mortbay.cometd.BayeuxService;

/**
 * This class manages the bidirectional HTTP event channel used by
 * WreckWatch to provide real-time updates to browser-based clients. 
 * 
 * @author jules
 *
 */
public class EventChannelManager extends BayeuxService {

	private static final Logger logger_ = Logger.getLogger(EventChannelManager.class
			.getName());

	private static EventChannelManager instance_;

	public static EventChannelManager getInstance() {
		return instance_;
	}

	public static EventChannelManager launch(Bayeux b) {
		if (instance_ == null) {
			instance_ = new EventChannelManager(b);
		}
		return instance_;
	}
	
	private EventChannelManager(Bayeux bayeux) {
		super(bayeux, "model");
		subscribe(VUPhoneServer.VANDY_UPON_EVENT_CHANNEL, "monitorEvents");
		subscribe("/meta/subscribe", "monitorSubscribe");
		subscribe("/meta/unsubscribe", "monitorUnsubscribe");
		subscribe("/meta/*", "monitorMeta");
	}

	public void monitorEvents(Client client, String channel,
			Map<String, String> data, String messageId) {
		try {
			logger_.log(Level.INFO, "Incoming Event <--" + data);

		} catch (Exception e) {
			logger_.log(Level.SEVERE, "Error Processing Event <--" + data);
		}
	}

	protected void exception(Client fromClient, Client toClient,
			Map<String, Object> msg, Throwable th) {
		th.printStackTrace();
		super.exception(fromClient, toClient, msg, th);
	}

	public void monitorSubscribe(Client client, Message message) {
		//String clientid = client.getId();
		String channelid = "" + message.get(Bayeux.SUBSCRIPTION_FIELD);

		Channel channel = getBayeux().getChannel(channelid, false);

		if (channel != null) {
			channel.subscribe(client);
		}
	}

	public void monitorUnsubscribe(Client client, Message message) {
		// String clientid = client.getId();
		// String channel = "" + message.get(Bayeux.SUBSCRIPTION_FIELD);
	}

	public void monitorMeta(Client client, Message message) {
	}
	
}
