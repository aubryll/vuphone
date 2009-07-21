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
package org.vuphone.vandyupon.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

public class EmitterFactory {
	
	public enum ResponseType{
		JSON, XML
	}
	
	public static XStream createXStream(ResponseType type){
		
		XStream temp;
		
		switch (type){
			case JSON:{
				temp = new XStream (new JettisonMappedXmlDriver());
				temp.setMode(XStream.NO_REFERENCES);
				break;
			}
			case XML:{
				temp = new XStream();
				break;
			}
			default:{
				temp = new XStream();
			}
			
		}
		
		return temp;
		
	}

}
