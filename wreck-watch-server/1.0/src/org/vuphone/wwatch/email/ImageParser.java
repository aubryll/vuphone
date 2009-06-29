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
package org.vuphone.wwatch.email;

import java.awt.Image;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;


/**
 * This class is responsible for processing email
 * messages for image content.
 * @author Chris Thompson
 *
 */
public class ImageParser {

	public Image[] parseMessages(Message[] msgs){
		try {
			for (int i = 0; i < msgs.length; i++) {
				System.out.println(msgs[i].getSize() + " bytes long.");
				System.out.println(msgs[i].getLineCount() + " lines.");
				String disposition = msgs[i].getDisposition();
				if (disposition == null){
					; // do nothing
				}else if (disposition.equals(Part.INLINE)) {
					System.out.println("This part should be displayed inline");
				} else if (disposition.equals(Part.ATTACHMENT)) {
					System.out.println("This part is an attachment");
					String fileName;

					fileName = msgs[i].getFileName();

					System.out.println("The file name of this attachment is " + fileName);
				}
				String description = msgs[i].getDescription();
				if (description != null) {
					System.out.println("The description of this message is " + description);
				}
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return null;
	}

}
