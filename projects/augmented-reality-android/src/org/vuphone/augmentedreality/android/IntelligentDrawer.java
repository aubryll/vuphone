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

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

// TODO - Z ordering
public class IntelligentDrawer implements ARDrawer {

	private final Paint whitePaint_;
	private final Paint blackPaint_;
	
	private ARSensors sensor_;

	private final ArrayList<ARObject> objList_ = new ArrayList<ARObject>();
	private Context context_;

	public float azimuth;

	public IntelligentDrawer(Context context) {
		context_ = context.getApplicationContext();
		
		whitePaint_ = new Paint();
		whitePaint_.setColor(Color.WHITE);
		whitePaint_.setTextSize(12);
		
		blackPaint_ = new Paint();
		blackPaint_.setColor(Color.BLACK);
		blackPaint_.setTextSize(12);
		blackPaint_.setStrokeWidth(3);
		
		sensor_ = ARSensors.getInstance(context_);

		Location isis = new Location(LocationManager.GPS_PROVIDER);
		isis.setLatitude(36.149181);
		isis.setLongitude(-86.799634);
		sensor_.setMockLocation(isis);
	}

	public void addObject(ARObject obj) {
		objList_.add(obj);
	}

	@Override
	public void draw(Canvas canvas) {
		int w = canvas.getWidth();
		int h = canvas.getHeight();

		// Remap the canvas from landscape to portrait
		canvas.translate(0, 320);
		canvas.rotate(270);

		w = canvas.getHeight();
		h = canvas.getWidth();

		// Draw the crosshair
		drawCrosshair(canvas, w, h);

		float[] data = sensor_.getOrientation();

		if (data == null) {
			Log.v("AndroidTests", "Returning");
			return;
		}

		azimuth = data[0];

		// Process the points.
		float[][] basis = ARCalculator.getBasis(azimuth, data[1]);
		Location pt = new Location(LocationManager.GPS_PROVIDER);

		for (int i = 0; i < objList_.size(); i++) {
			ARObject object = objList_.get(i);

			pt.setLatitude(object.getLatitude());
			pt.setLongitude(object.getLongitude());
			float[] offset = ARCalculator.getPointVector(sensor_.getLocation(),
					pt);

			float[] ptVector = ARCalculator.getLinearCombination(basis, offset);
			drawVector(canvas, ptVector, i);

			float depth = ptVector[2];
			// Don't bother drawing if object is closer than 1 meter.
			if (depth > -1)
				continue;

			float right = ptVector[0];
			float up = ptVector[1];

			float maxRight = ARCalculator.getHorizontalSpan(15, depth);
			float maxUp = ARCalculator.getVerticalSpan(25, depth);

			if (Math.abs(up) <= maxUp && Math.abs(right) <= maxRight) {
				//drawString(canvas, "Point in view", 250, 0);

				float offsetX = w / 2 * (right / maxRight);
				float offsetY = h / 2 * (up / maxUp);

				float x = w / 2 + offsetX;
				float y = h / 2 - offsetY;

				canvas.drawCircle(x, y, 10, whitePaint_);
			}
		}

		drawBasisMatrix(canvas, basis);

		Location loc = sensor_.getLocation();
		if (loc == null)
			return;
		
		drawStatus(canvas, new float[]{0, 0, 0}, loc);
	}

	private void drawCrosshair(final Canvas canvas, int w, int h) {
		canvas.drawLine(0, h / 2, w, h / 2, blackPaint_);
		canvas.drawLine(w / 2, 0, w / 2, h, blackPaint_);

		canvas.drawLine(0, h / 2, w, h / 2, whitePaint_);
		canvas.drawLine(w / 2, 0, w / 2, h, whitePaint_);

		float centerX = w / 2f;
		float centerY = h / 2f;
		float startX = centerX % 50;
		float startY = centerY % 50;

		for (float x = startX; x < w; x += 50) {
			for (float y = startY; y < h; y += 50) {
				canvas.drawCircle(x, y, 3, blackPaint_);
				canvas.drawCircle(x, y, 2, whitePaint_);
			}
		}

	}

	private void drawStatus(final Canvas canvas, final float[] o, final Location l) {
		String loc = l.getLatitude() + ", " + l.getLongitude() + ", " + l.getAltitude();
		String or = o[0] + ", " + o[1] + ", " + o[2];
		
		String noLoc = "Acquiring location";
		String noOr = "Acquiring orientation";
		Rect bounds = new Rect();
		
		boolean isLoc = true, isOr = true;
		
		int sWidth = canvas.getHeight();
		int sHeight = canvas.getWidth();
		
		int locWidth, orWidth;
		String str;
		str = isLoc ? loc : noLoc;
		
		whitePaint_.getTextBounds(str, 0, str.length(), bounds);
		locWidth = bounds.width();

		str = isOr ? or : noOr;
		whitePaint_.getTextBounds(str, 0, str.length(), bounds);
		orWidth = bounds.width();

		int width = Math.max(orWidth, locWidth);
		int height = (int) whitePaint_.getTextSize() * 2;
		canvas.drawRect(sWidth - width, sHeight - height, sWidth, sHeight, blackPaint_);
		
		canvas.drawText(isLoc ? loc : noLoc, sWidth - locWidth, sHeight - whitePaint_.getTextSize(), whitePaint_);
		canvas.drawText(isOr ? or : noOr, sWidth - orWidth, sHeight, whitePaint_);
	}

	private void drawBasisMatrix(final Canvas canvas, final float[][] m) {
		Rect bounds = new Rect();
		whitePaint_.getTextBounds("0.123", 0, 5, bounds);
		int padding = 5;
		int width = Math.abs(bounds.width()) + padding;
		int height = (int) whitePaint_.getTextSize() + padding;

		int startX = padding;
		int startY = height;

		canvas.drawRect(startX, startY - width, startX + 3 * width, startY + 2 * height, blackPaint_);
		
		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				float val = ((int) (m[c][r] * 1000) / 1000f);
				String value = "" + val;
				if (value.length() > 4)
					value = value.substring(0, 5);
				canvas.drawText(value, startX + c * width, startY + r * height,
						whitePaint_);
			}
		}
	}

	private void drawVector(final Canvas canvas, final float[] v, int i) {
  		String str = "{" + ((int) (v[0] * 100)) / 100f + ", " + ((int) (v[1] * 100)) / 100f + ", " + ((int) (v[2] * 100)) / 100f + "}";
		Rect bounds = new Rect();
		whitePaint_.getTextBounds(str, 0, str.length(), bounds);
		
		int bottom = canvas.getWidth() - (int) whitePaint_.getTextSize() * i;
		
		canvas.drawRect(0, bottom - whitePaint_.getTextSize(), bounds.width(), bottom, blackPaint_);
		canvas.drawText(str, 0, bottom, whitePaint_); 
	}
	
	@Override
	public void surfaceReady(int width, int height) {
	}

	@Override
	public void pause() {
		sensor_.finish();
	}

	@Override
	public void resume() {
		sensor_ = ARSensors.getInstance(context_);
	}
}
