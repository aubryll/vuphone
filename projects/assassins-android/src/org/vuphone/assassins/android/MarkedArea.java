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

import android.content.Context;

/**
 * This interface should be implemented by classes that want to define
 * a specific area on the map by a central point and radius about it.
 * 
 * @author Scott Campbell
 */
public interface MarkedArea {

	/**
	 * The activate method is where the proximity alert(s) for the marked
	 * area will be created on the phone.  After this method is called, every
	 * time the user enters or leaves the marked area will fire a specific
	 * intent.  Subsequent calls to activate should have no effect.
	 * 
	 * @param c - The Context in which the marked area will be activated.
	 */
	public void activate(Context c);
	
	/**
	 * The deactivate method will remove all proximity alerts for the
	 * marked area.  After this method is called, the user can enter and
	 * leave the marked area without causing any intents to be fired.
	 * Subsequent calls to deactivate should have no effect.
	 * 
	 * @param c - The Context in which the marked area will be deactivated.
	 */
	public void deactivate(Context c);
	
	public double getLatitude();
	
	public double getLongitude();
	
	public float getRadius();
}
