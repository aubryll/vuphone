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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
// TODO - Z ordering
public class IntelligentDrawer implements ARDrawer {

	private final Paint paint_;
	private ARSensors sensor_;

	private final ArrayList<ARObject> objList_ = new ArrayList<ARObject>();
	private Context context_;
	
	public float azimuth;
	

	public IntelligentDrawer(Context context) {
		context_ = context.getApplicationContext();
		paint_ = new Paint();
		paint_.setColor(Color.WHITE);
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
			float[] offset = ARCalculator.getPointVector(sensor_.getLocation(), pt);
			
			float[] ptVector = ARCalculator.getLinearCombination(basis, offset);

			float depth = ptVector[2];
			// Don't bother drawing if object is closer than 1 meter.
			if (depth > -1)
				continue;

			float right = ptVector[0];
			float up = ptVector[1];

			float maxRight = ARCalculator.getHorizontalSpan(15, depth);
			float maxUp = ARCalculator.getVerticalSpan(25, depth);

			if (Math.abs(up) <= maxUp && Math.abs(right) <= maxRight) {
				drawString(canvas, "Point in view", 250, 0);

				float offsetX = w / 2 * (right / maxRight);
				float offsetY = h / 2 * (up / maxUp);

				float x = w / 2 + offsetX;
				float y = h / 2 - offsetY;


				// Log.v("AndroidTests", "Creating bitmap " + imgW + ", " +
				// imgH);


				float scale;
				
				if (object.shouldScale()) {
					scale = object.getDefaultDistance() / -depth;	
				} else
					scale = 1;

				int imgW = (int) (object.getImageWidth() * scale);
				int imgH = (int) (object.getImageHeight() * scale);

				Bitmap bmp = Bitmap.createBitmap(imgW, imgH, Bitmap.Config.ARGB_8888);
				Canvas objectCanvas = new Canvas(bmp);
				objectCanvas.scale(scale, scale);					
				object.draw(objectCanvas);

				canvas.drawBitmap(bmp, x, y, paint_);
				canvas.drawText(object.getMetaData(), x, y + paint_.getTextSize(), paint_);

				// canvas.drawCircle(x, y, 10, paint_);
				// canvas.drawRect(x, y, x + 50, y + 20, paint_);
			}

			drawVector(canvas, ptVector, i);
		}

		String str = "Azimuth: " + azimuth;
		drawString(canvas, str, 0, 0);
		str = "Pitch: " + sensor_.getOrientation()[1];
		drawString(canvas, str, 0, 45);
		drawMatrix(canvas, basis);

		Location loc = sensor_.getLocation();
		if (loc == null)
			return;

		str = "Location: " + loc.getLatitude() + ", " + loc.getLongitude()
				+ ", " + loc.getAltitude() + " | " + loc.getAccuracy();
		drawString(canvas, str, 0, (int) paint_.getTextSize());
	}

	private void drawCrosshair(final Canvas canvas, int w, int h) {
		paint_.setStrokeWidth(3);
		paint_.setColor(Color.BLACK);
		canvas.drawLine(0, h / 2, w, h / 2, paint_);
		canvas.drawLine(w / 2, 0, w / 2, h, paint_);

		paint_.setStrokeWidth(1);
		paint_.setColor(Color.WHITE);
		canvas.drawLine(0, h / 2, w, h / 2, paint_);
		canvas.drawLine(w / 2, 0, w / 2, h, paint_);

		float centerX = w / 2f;
		float centerY = h / 2f;
		float startX = centerX % 50;
		float startY = centerY % 50;

		for (float x = startX; x < w; x += 50) {
			for (float y = startY; y < h; y += 50) {
				paint_.setColor(Color.BLACK);
				canvas.drawCircle(x, y, 3, paint_);
				paint_.setColor(Color.WHITE);
				canvas.drawCircle(x, y, 2, paint_);
			}
		}

	}

	private void drawString(final Canvas canvas, final String str, int x, int y) {
		Rect r = new Rect();
		paint_.setColor(Color.BLACK);
		paint_.getTextBounds(str, 0, str.length(), r);
		r.offsetTo(x, y);
		r.set(r.left, r.top, r.right, r.top + (int) paint_.getTextSize() + 2);
		canvas.drawRect(r, paint_);

		paint_.setColor(Color.WHITE);
		canvas.drawText(str, x, y + paint_.getTextSize(), paint_);
	}

	private void drawVector(final Canvas canvas, final float[] v, int c) {
		Rect bounds = new Rect();
		paint_.getTextBounds("0.123", 0, 5, bounds);
		int padding = 5;
		int width = Math.abs(bounds.width()) + padding;
		int height = (int) paint_.getTextSize() + padding;

		int startX = 0 + c * width;
		int startY = canvas.getWidth() - 2 * height;

		paint_.setColor(Color.BLACK);
		canvas.drawRect(startX - padding, startY - height, startX + width,
				canvas.getWidth(), paint_);
		paint_.setColor(Color.WHITE);

		for (int r = 0; r < 3; r++) {
			String value = "" + v[r];
			if (value.length() > 4)
				value = value.substring(0, 5);
			canvas.drawText(value, startX, startY + r * height, paint_);
		}
	}

	private void drawMatrix(final Canvas canvas, final float[][] m) {
		Rect bounds = new Rect();
		paint_.getTextBounds("0.123", 0, 5, bounds);
		int padding = 5;
		int width = Math.abs(bounds.width()) + padding;
		int height = (int) paint_.getTextSize() + padding;

		int startX = canvas.getHeight() - 3 * width + padding - 2;
		int startY = canvas.getWidth() - 2 * height;

		paint_.setColor(Color.BLACK);
		canvas.drawRect(startX - padding, startY - height, canvas.getHeight(),
				canvas.getWidth(), paint_);
		paint_.setColor(Color.WHITE);

		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				String value = "" + m[c][r];
				if (value.length() > 4)
					value = value.substring(0, 5);
				canvas.drawText(value, startX + c * width, startY + r * height,
						paint_);
			}
		}
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
