/*******************************************************************************
 * Copyright 2009 Krzysztof Zienkiewicz
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

package org.vuphone.augmentedreality.android;

import android.graphics.Canvas;

public interface ARObject {

	public static final int CENTER_TOP_LEFT_CORNER = 1;
	public static final int CENTER_CENTER = 2;

	/**
	 * The call responsible for drawing the object's image. Implementations of
	 * this method should draw the unscaled image (as if the object were exactly
	 * getDefaultDistance() meters away from the viewer). Any necessary
	 * transformations should be applied by the drawer.
	 * 
	 * @param canvas
	 */
	public void draw(Canvas canvas);

	/**
	 * Returns the constant representing the method to use to center this
	 * object's image.
	 * 
	 * @return
	 */
	public int getCenterMethod();
	
	
	public float getDefaultDistance();

	/**
	 * Return the height of this image. This is the height that will be used
	 * when the object is getDefaultDistance() meter away.
	 * 
	 * @return
	 */
	public int getImageHeight();

	/**
	 * Return the width of this image. This is the width that will be used when
	 * the object is getDefaultDistance() meter away.
	 * 
	 * @return
	 */
	public int getImageWidth();

	/**
	 * Returns the latitude of this object in degrees.
	 * 
	 * @return
	 */
	public float getLatitude();

	/**
	 * Returns the longitude of this object in degrees.
	 * 
	 * @return
	 */
	public float getLongitude();
	
	
	public String getMetaData();
	
	public boolean shouldScale();
}