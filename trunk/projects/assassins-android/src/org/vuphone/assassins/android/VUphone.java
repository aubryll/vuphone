/*******************************************************************************
 * Copyright 2009 Scott Campbell
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package org.vuphone.assassins.android;
/**
 * A simple utility class that contains things that need to be
 * accessed from many different points in the application.
 * 
 * @author Scott Campbell
 *
 */
public class VUphone {

	/**
	 * Tag used for logging purposes.
	 */
	public static final String tag = "VUPHONE:ASSASSINS";
	
	public static final String SERVER = "http://129.59.135.163:8080";
	
	/**
	 * This flag will tell whether the user has entered the playing area,
	 * as defined in the GameArea class, or not.
	 */
	public static boolean IN_GAME_AREA = false;
}
