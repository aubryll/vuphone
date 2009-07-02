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

import org.vuphone.wwatch.mapping.MapEvent;

public class RouteRequestEvent extends MapEvent {
	
	private int wreckid_;
	
	public RouteRequestEvent(){
		super("routerequest");
		
	}
	
	public RouteRequestEvent(int id){
		super("routerequest");
		wreckid_ = id;
	}
	
	public void setWreckId(int id){
		wreckid_ = id;
	}
	
	public int getWreckId(){
		return wreckid_;
	}
	
	

	

}
