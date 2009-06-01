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
package org.vuphone.wwatch.asterisk;

import java.io.IOException;

import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.response.ManagerResponse;


public class AsteriskConnector {
	
	private static final String SERVER = "129.59.177.177";
	private static final String USERNAME = "admin";
	private static final String PASSWORD = "amp111";
	
	private ManagerConnection mc_;
	
	public AsteriskConnector(){
		ManagerConnectionFactory f = new ManagerConnectionFactory(SERVER, USERNAME, PASSWORD);
		mc_ = f.createManagerConnection();
		
	}
	
	// Right now, this makes the call, but it does not play the recording.
	// When this is run, it will:
	//		1) Make the sourceExtension (extension 210) ring
	//		2) When sourceExtension is answered, OriginateAction will return success
	//		3) When sourceExtension is answered, it will immediately call
	//				destinationExtension (204)
	//		4) Things will then proceed as if extension 210 had dialed 204.
	public void makeCallPlayRecording(String file){
		String sourceExtension = "210";
		String destinationExtension = "204";
		
		OriginateAction originateAction;
        ManagerResponse originateResponse;

        originateAction = new OriginateAction();
        originateAction.setChannel("SIP/"+sourceExtension);
        originateAction.setContext("default");
        originateAction.setExten(destinationExtension);
        originateAction.setPriority(new Integer(1));

        try {
        	mc_.login();
        	
        	// send the originate action and wait for a maximum of 30 seconds for 
        	// Asterisk to send a reply
        	originateResponse = mc_.sendAction(originateAction, 30000);

        	System.out.println(originateResponse.getResponse());
        	System.out.println(originateResponse.getMessage());
        	System.out.println(originateResponse.toString());
        	mc_.logoff();
        	
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
		AsteriskConnector ac = new AsteriskConnector();
		ac.makeCallPlayRecording("test.wma");
	}

}
