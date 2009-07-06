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
package org.vuphone.wwatch.mapping.wrecklocationrequest;

import java.util.ArrayList;

import org.vuphone.wwatch.mapping.MapResponse;

public class WreckLocationResponse extends MapResponse {
	
	private ArrayList<Wreck> accidents_;

	
	public WreckLocationResponse(){
		super("locationresponse");
		accidents_ = new ArrayList<Wreck>();
	}


	public void addAccident(Wreck w) {
		accidents_.add(w);
	}

	public ArrayList<Wreck> getAccidents() {
		return accidents_;
	}

	@Override
	public byte[] getRespose() {
		return null;
	}
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		
		sb.append("Number of accidents: " + accidents_.size() + "\n");
		return sb.toString();
	}

}
