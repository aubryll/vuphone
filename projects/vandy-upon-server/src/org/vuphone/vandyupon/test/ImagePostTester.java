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
package org.vuphone.vandyupon.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class ImagePostTester {

	public static void main(String[] args){
		try {
			File image = new File("2008-05-8 ChrisMikeNinaGrad.jpg");
			byte[] bytes = new byte[(int) image.length()];
			FileInputStream fis = new FileInputStream(image);

			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length
					&& (numRead=fis.read(bytes, offset, bytes.length-offset)) >= 0) {
				offset += numRead;
			}


			fis.close();
			
			HttpClient c = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://afrl-gift.dre.vanderbilt.edu:8080/vandyupon/events/?type=eventimagepost&eventid=2" +
					"&time=" + System.currentTimeMillis() + "&resp=xml");
			
			post.addHeader("Content-Type", "image/jpeg");
			ByteArrayEntity bae = new ByteArrayEntity(bytes);
			post.setEntity(bae);
			
			try {
				HttpResponse resp = c.execute(post);
				resp.getEntity().writeTo(System.out);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
