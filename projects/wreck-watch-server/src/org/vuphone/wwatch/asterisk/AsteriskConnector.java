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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.response.ManagerResponse;


/**
 * This is a class that provides static methods to generate a call
 * to either an already existing SIP extension or a regular
 * telephone number, and then play a pre-recorded message once the
 * phone is answered.
 * 
 * @author Scott Campbell
 *
 */
public class AsteriskConnector {
	
	/**
	 * This is the IP of the server where the trixbox resides.
	 * Do NOT include 'http://'.
	 */
	private static final String SERVER = "129.59.129.97";
	
	/**
	 * There are the credentials for the maint account on the trixbox.
	 * They are necessary for interacting with the web interface via
	 * Http Post and Get.
	 */
	private static final String MAINT_USERNAME = "maint";
	private static final String MAINT_PASSWORD = "password";
	
	/**
	 * These are the credentials for the admin account on the trixbox.
	 * They are necessary for using asterisk-java to interact with
	 * the trixbox server directly.
	 */
	private static final String ADMIN_USERNAME = "admin";
	private static final String ADMIN_PASSWORD = "amp111";
	
	/**
	 * This is the default message that will be played to every phone
	 * number dialed via the playRecordingToPSTNNumber method.  This
	 * must be a .gsm file located on the trixbox server at
	 * /var/lib/asterisk/sounds, and it must not include the file type.
	 */
	private static final String MESSAGE = "wreck-notification";
	
	/**
	 * This method will do all the work necessary to play a recording to
	 * any regular telephone number.
	 * 
	 * @param number - a String representation of a 10 digit telephone
	 * number that will be called.
	 */
	public static void playRecordingToPSTNNumber(String number) {
		
		final String callNumber = number;
		
		new Thread(new Runnable(){
			public void run() {
		
				String ext = lookupSipExtension(callNumber);
				if (ext.equals("")) {
					ext = createSipExtension(callNumber);
				}
				if (!ext.equals("")) {
					makeCallPlayRecording(ext,MESSAGE);
				}
				else {
					// Insert some proper type of error reporting here...
					System.out.println("There was an error creating an extension.");
				}
			}
		}).start();
	}
	
	/**
	 * This is a helper method for playRecordingToPSTNNumber that will
	 * check and see if the provided number is already registered to
	 * SERVER as a SIP extension.
	 * 
	 * @param number - a String representation of a 10 digit telephone
	 * number that will be searched for among the current extensions.
	 * 
	 * @return - a String representation of the extension number that
	 * will dial the provided telephone number, or an empty String ""
	 * if no such extension exists.
	 */
	private static String lookupSipExtension(String number) {
		
		String extension = "";
		
		
		// First, get the list of extensions currently on the server.
		HttpClient c = new DefaultHttpClient();
        HttpGet get = new HttpGet("http://"+SERVER+"/admin/config.php?type=setup&display=extensions");
        String authorization = Base64.base64Encode(MAINT_USERNAME+":"+MAINT_PASSWORD);
        get.addHeader("Authorization","Basic "+authorization);
        
        try {
        	HttpResponse resp = c.execute(get);
        	ByteArrayOutputStream bao = new ByteArrayOutputStream();
        	resp.getEntity().writeTo(bao);

            String responsetext = new String(bao.toByteArray());
            
            // Now actually get the list of extensions.
            List<String> extensionUrls = new ArrayList<String>();
            int divStart = responsetext.indexOf("rnav");
            int divEnd = responsetext.indexOf("</div>",divStart);
            int refStart = responsetext.indexOf("href",divStart);
            // ignore the first href - it's the address we just visited
        	refStart = responsetext.indexOf("href",refStart+1);
            int refEnd = responsetext.indexOf("\"",refStart+6);

            while (refStart < divEnd && refStart != -1) {
            	String thisUrl = responsetext.substring(refStart+6,refEnd);
            	thisUrl = thisUrl.replace("&amp;","&");
            	extensionUrls.add(thisUrl);
            	refStart = responsetext.indexOf("href",refStart+1);
            	refEnd = responsetext.indexOf("\"",refStart+6);           
            }
            
            
            // Visit each of these extensions and see if any of them dial
            // the input pstn number
            for (int i = 0; i < extensionUrls.size(); i++) {
            	get = new HttpGet("http://"+SERVER+"/admin/"+extensionUrls.get(i));
            	get.addHeader("Authorization","Basic "+authorization);
                resp = c.execute(get);
            	bao = new ByteArrayOutputStream();
                resp.getEntity().writeTo(bao);
                responsetext = new String(bao.toByteArray());           
                
                // Get the entry in the Dial field
                int dialStart = responsetext.indexOf("<td>dial</td>");
                int valueStart = responsetext.indexOf("value=",dialStart);
                int valueEnd = responsetext.indexOf("\"",valueStart+8);
                String entry = responsetext.substring(valueStart+7,valueEnd);
                
                // The below requires that the number in the dial field be exactly
                // the same as the number that was passed into this method.  It
                // might make sense to change this to allow for things like missing
                // the 1 in front of the area code or adding a 9 in front to dial
                // an outbound line.
                if (entry.equals(number)) {
                	int extStart = extensionUrls.get(i).indexOf("extdisplay");
                	int extEnd = extensionUrls.get(i).indexOf("&",extStart);
                	extension = extensionUrls.get(i).substring(extStart+11,extEnd);
                	break;
                }
            }
        	
        } catch (IOException e) {
        	
        	e.printStackTrace();
        }

    	System.out.println("I'm going to return: "+extension);
		return extension;
	}
	
	/**
	 * This is a helper method for playRecordingToPSTNNumber that will
	 * create a new SIP extension to dial the telephone number provided.
	 * 
	 * @param number - a String representation of a 10 digit telephone
	 * number that will be dialed from the newly created extension.
	 * 
	 * @return - a String representation of the extension number that
	 * will dial the provided telephone number, or an empty String ""
	 * if there is an error creating the extension.
	 */
	private static String createSipExtension(String number) {
		
		String extension = "";
		
		
		// The first post will open the screen to create a new generic
		// SIP extension
		HttpClient c = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://"+SERVER+"/admin/config.php?type=setup&display=extensions");
        String authorization = Base64.base64Encode(MAINT_USERNAME+":"+MAINT_PASSWORD);
        post.addHeader("Authorization","Basic "+authorization);
        post.addHeader("Content-Type","application/x-www-form-urlencoded");
        
        String info = "display=extensions&type=setup&tech_hardware=sip_generic&Submit=Submit";
        ByteArrayEntity str = new ByteArrayEntity(info.getBytes());
        post.setEntity(str);

		try {
			c.execute(post);
	        
		} catch (ClientProtocolException e) {

			e.printStackTrace();
			return extension;
		} catch (IOException e) {

			e.printStackTrace();
			return extension;
		}
		
		
		// The second post will create the new extension and set it to dial
		// the provided number.  It will be named Extension<number>, with
		// password <number>-password.
		HttpClient c1 = new DefaultHttpClient();
		post = new HttpPost("http://"+SERVER+"/admin/config.php");
        post.addHeader("Authorization","Basic "+authorization);
        post.addHeader("Content-Type","application/x-www-form-urlencoded");
        
        String extenNum = number;
        String extenName = "Extension"+extenNum;
        String extenSecret = extenNum+"-password";
        info = "display=extensions&type=setup&action=add&extdisplay=&action=add&extdisplay=&extension="+extenNum+"&name="+extenName+"&cid_masquerade=&sipname=&directdid=&didalert=&mohclass=default&outboundcid=&ringtimer=0&callwaiting=enabled&emergency_cid=&tech=sip&hardware=generic&devinfo_secret="+extenSecret+"&devinfo_dtmfmode=rfc2833&devinfo_canreinvite=no&devinfo_context=from-internal&devinfo_host=dynamic&devinfo_type=friend&devinfo_nat=yes&devinfo_port=5060&devinfo_qualify=yes&devinfo_callgroup=&devinfo_pickupgroup=&devinfo_disallow=&devinfo_allow=&devinfo_dial="+number+"&devinfo_accountcode=&devinfo_mailbox=&faxexten=default&faxemail=&answer=0&wait=0&privacyman=0&in_default_page_grp=0&langcode=&record_in=Adhoc&record_out=Adhoc&vm=disabled&vmpwd=&email=&pager=&attach=attach%3Dno&saycid=saycid%3Dno&envelope=envelope%3Dno&delete=delete%3Dno&options=&vmcontext=default&vmx_state=&Submit=Submit";
        str = new ByteArrayEntity(info.getBytes());
        post.setEntity(str);

		try {
			c1.execute(post);
	        
		} catch (ClientProtocolException e) {

			e.printStackTrace();
			return extension;
		} catch (IOException e) {

			e.printStackTrace();
			return extension;
		}
		
		
		// The third post will submit the changes.
		HttpClient c2 = new DefaultHttpClient();
		post = new HttpPost("http://"+SERVER+"/admin/config.php");
        post.addHeader("Authorization","Basic "+authorization);
        post.addHeader("Content-Type","application/x-www-form-urlencoded");
        
        info = "handler=reload";
        str = new ByteArrayEntity(info.getBytes());
        post.setEntity(str);

		try {
			c2.execute(post);
	        
		} catch (ClientProtocolException e) {

			e.printStackTrace();
			return extension;
		} catch (IOException e) {

			e.printStackTrace();
			return extension;
		}
        
		extension = "SIP/"+extenNum;
		System.out.println("I'm going to return: "+extension);
		return extension;
	}
	
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
		
		ManagerConnectionFactory f = 
			new ManagerConnectionFactory(SERVER, ADMIN_USERNAME, ADMIN_PASSWORD);
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

		//AsteriskConnector.makeCallPlayRecording("campbesh-302@pbxes.org",MESSAGE);
		AsteriskConnector.playRecordingToPSTNNumber("1234567890");
	}

}
