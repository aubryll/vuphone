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
package org.vuphone.vandyupon.notification;

public abstract class ResponseNotification extends Notification {
	
	private String type_;
	private String callback_;
	private String responseType_;
	
	public ResponseNotification(String type, String responseType, String callback){
		super("response");
		type_ = type;
		callback_ = callback;
		responseType_ = responseType;
	}
	
	public String getCallback(){
		return callback_;
	}
	
	public String getResponseNotificationType(){
		return type_;
	}
	
	public String getResponseType(){
		return responseType_;
	}
	
	public void setCallback(String cb){
		callback_ = cb;
	}
	
	public void setReponseType(String type){
		responseType_ = type;
	}
	

}
