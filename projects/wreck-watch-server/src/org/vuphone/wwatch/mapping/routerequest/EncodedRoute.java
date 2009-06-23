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
package org.vuphone.wwatch.mapping.routerequest;

public class EncodedRoute {
	
	private String encodedRoute_;
	private String encodedLevels_;
	private int id_;
	
	public EncodedRoute(String route, String levels, int id){
		encodedRoute_ = route;
		encodedLevels_ = levels;
		id_ = id;
	}
	
	public String getEncodedRoute(){
		return encodedRoute_;
	}
	
	public String getEncodedLevels(){
		return encodedLevels_;
	}
	
	public int getId(){
		return id_;
	}
	
	

}
