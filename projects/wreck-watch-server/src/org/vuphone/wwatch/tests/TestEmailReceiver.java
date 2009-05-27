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
package org.vuphone.wwatch.tests;

import java.io.IOException;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;

import junit.framework.TestCase;

import org.vuphone.wwatch.email.EmailReceiver;

public class TestEmailReceiver extends TestCase {

	public void testEmailReceive() throws IOException, MessagingException{
		EmailReceiver er = new EmailReceiver("vuphone.1@gmail.com", "isisvuphone.1");
		Message[] msgs = er.checkMail("INBOX");

		for (int i = 0; i < msgs.length; i++) {
			System.out.println("--------------------------");
			System.out.println("MESSAGE #" + (i + 1) + ":");

			if (msgs[i].getContent() instanceof MimeMultipart){
				try {
					msgs[i].getFolder().open(Folder.READ_ONLY);
					for (int j = 0; j < ((MimeMultipart)msgs[i].getContent()).getCount(); ++j){
						BodyPart part = ((MimeMultipart)msgs[i].getContent()).getBodyPart(j);
						
						
						System.out.println(part.getContent());
					}
					msgs[i].getFolder().close(false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if (msgs[i].isMimeType("text/plain")){
				System.out.println((String)msgs[i].getContent());
			}else{
				System.out.println(msgs[i].getContent());
			}

		}    
	}

}
