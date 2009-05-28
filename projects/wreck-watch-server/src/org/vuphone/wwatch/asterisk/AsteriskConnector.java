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

import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiOperations;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.ZapDialOffhookAction;
import org.asteriskjava.manager.response.ManagerResponse;


public class AsteriskConnector {
	
	private static final String SERVER = "129.59.129.103";
	private static final String USERNAME = "maint";
	private static final String PASSWORD = "password";
	
	private ManagerConnection mc_;
	
	public AsteriskConnector(){
		ManagerConnectionFactory f = new ManagerConnectionFactory(SERVER, USERNAME, PASSWORD);
		mc_ = f.createManagerConnection();
		
	}
	
	public void makeCallPlayRecording(String file){
		ZapDialOffhookAction da = new ZapDialOffhookAction();
		ManagerResponse response = new ManagerResponse();
		
		
		da.setZapChannel(1);
		da.setNumber("7274812833");
		da.setActionId("10101010");
		
		try {
			mc_.login();
			response = mc_.sendAction(da);
			System.out.println(response.toString());
			AgiOperations ops = new AgiOperations();
			ops.streamFile(file);
			ops.hangup();
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
		} catch (AgiException e) {
			
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String args[]){
		AsteriskConnector ac = new AsteriskConnector();
		ac.makeCallPlayRecording("test.wma");
	}

}
