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

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class EventRatingPostTest {

	
	public static void main(String[] args){
		HttpClient c = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://localhost:8080/vandyupon/events/");
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");
		
		String params = "type=eventratingpost&event=1&user=chris&comment=awesome&value=1&resp=xml";
		post.setEntity(new ByteArrayEntity(params.toString().getBytes()));
		
		try {
			HttpResponse resp = c.execute(post);
			resp.getEntity().writeTo(System.out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
