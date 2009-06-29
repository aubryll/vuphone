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
package org.vuphone.wwatch.mapping;

public abstract class MapResponse {
	
	private String type_;
	
	public MapResponse(String type){
		type_ = type;
	}
	
	/**
	 * This method is used to get the response the server should send.  
	 * Depending on the type of the subclass, this byte[] could represent
	 * any number of objects.  The encoding is left to subclasses to 
	 * determine.
	 * @return
	 */
	public abstract byte[] getRespose();
	
	public String getType(){
		return type_;
	}

}
