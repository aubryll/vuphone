 /**************************************************************************
 * Copyright 2009 Scott Campbell                                           *
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
package org.vuphone.wwatch.asterisk;

import java.io.IOException;

import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.response.ManagerResponse;


public class AsteriskConnector {
	
	private static final String SERVER = "129.59.129.229";
	private static final String USERNAME = "admin";
	private static final String PASSWORD = "__eece261";
	
	/**
	 * This will call the specified user and play the specified file
	 * 
	 * @param user - The user to call.  This must be a sip account.  If it is not
	 * 				an extension on the connected Asterisk server (trixbox), it
	 * 	 			must contain the fully qualified host location.  It may or may
	 * 				not contain the prefix 'SIP/'.
	 * 				Examples:		juleswhite-1@pbxes.org
	 * 								SIP/210@129.59.177.177
	 * 
	 * @param file - The audio file to play back to the user.  The file type must
	 * 				NOT be specified.  The file must be located in
	 * 				/var/lib/asterisk/sounds on the Asterisk server (trixbox),
	 * 				and it must be of type .gsm
	 * 
	 */
	public static void makeCallPlayRecording(String user, String file){
		
		ManagerConnectionFactory f = new ManagerConnectionFactory(SERVER, USERNAME, PASSWORD);
		ManagerConnection mc = f.createManagerConnection();
		
		// Make sure that user includes the prefix 'SIP/'
		if (!user.startsWith("SIP/")) {
			user = "SIP/"+user;
		}
		
		OriginateAction originateAction;
        ManagerResponse originateResponse;

        originateAction = new OriginateAction();
        originateAction.setChannel(user);
        
        // This will set it to play back a pre-recorded message that is located
        // in /var/lib/asterisk/sounds on the Asterisk Server (trixbox)
        // The audio file must be of type .gsm.  The four lines below and these
        // are mutually exclusive.
        originateAction.setApplication("Playback");
        originateAction.setData(file);
        
        // This will set it to establish a call between the user above
        // and the destinationExtension given below.  Note that this extension
        // must be one that resides on the trixbox you connected to above.
        // The two lines above and these are mutually exclusive.
		//String destinationExtension = "200";
        //originateAction.setContext("default");
        //originateAction.setExten(destinationExtension);
        //originateAction.setPriority(new Integer(1));

        try {
        	mc.login();
        	
        	// send the originate action and wait for a maximum of 30 seconds for 
        	// Asterisk to send a reply
        	originateResponse = mc.sendAction(originateAction, 30000);

        	System.out.println(originateResponse.getResponse());
        	System.out.println(originateResponse.getMessage());
        	System.out.println(originateResponse.toString());
        	
        	mc.logoff();
        	
        } catch (IllegalArgumentException e) {

			e.printStackTrace();
		} catch (IllegalStateException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (TimeoutException e) {
			
			e.printStackTrace();
		} catch (AuthenticationFailedException e) {
			
			e.printStackTrace();
		}
		
	}
	
	public static void main(String args[]){

		AsteriskConnector.makeCallPlayRecording("campbesh-302@pbxes.org","zip-code");
	}

}
