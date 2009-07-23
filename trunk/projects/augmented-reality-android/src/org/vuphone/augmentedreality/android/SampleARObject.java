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
import android.graphics.Color;
import android.graphics.Paint;

public class SampleARObject implements ARObject {

	private final float latitude_, longitude_;
	private final boolean shouldScale_;
	
	public SampleARObject(float lat, float lon, boolean shouldScale) {
		latitude_ = lat;
		longitude_ = lon;
		shouldScale_ = shouldScale;
	}
	
	@Override
	public void draw(Canvas canvas) {
		Paint paint = new Paint();
		canvas.drawColor(Color.BLACK);
		paint.setColor(Color.WHITE);
		canvas.drawRect(1, 1, 149, 149, paint);
		paint.setColor(Color.BLACK);
		canvas.drawRect(2, 2, 148, 148, paint);
		paint.setColor(Color.WHITE);
		
		paint.setStrokeWidth(5);
		paint.setTextSize(50);
		canvas.rotate(45);
		canvas.drawText("Sample", 25, 15, paint);
	}

	@Override
	public int getImageHeight() {
		return 150;
	}

	@Override
	public int getImageWidth() {
		return 150;
	}

	@Override
	public float getLatitude() {
		return latitude_;
	}

	@Override
	public float getLongitude() {
		return longitude_;
	}

	@Override
	public int getCenterMethod() {
		return ARObject.CENTER_TOP_LEFT_CORNER;
	}

	@Override
	public float getDefaultDistance() {
		return 800;
	}

	@Override
	public String getMetaData() {
		return "Meta data";
	}

	@Override
	public boolean shouldScale() {
		return shouldScale_;
	}
}
