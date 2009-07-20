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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class IntelligentDrawer implements ARDrawer {

	private final Paint paint_;
	private final ARSensors sensor_;
	
	private final Point3D pt_;
	
	private float azimuth_ = 0;
	
	private class Point3D {
		public float x, y, z;
		
		public Point3D(float a, float b, float c) {
			x = a;	y = b;	z = c;
		}
		
		public float[] getVector() {
			return new float[] {x, y, z};
		}
	}

	public IntelligentDrawer(Context context) {
		pt_ = new Point3D(0, 0, -10);
		
		paint_ = new Paint();
		paint_.setColor(Color.WHITE);
		sensor_ = ARSensors.getInstance(context);
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

		azimuth_ = data[0];
		
		// Process the points.
		float[][] basis = ARCalculator.getBasis(azimuth_);
		float[] ptVector = ARCalculator.getLinearCombination(basis, pt_.getVector());
		
		float depth = ptVector[2];
		float right = ptVector[0];
		float up = ptVector[1];
		
		float maxRight = ARCalculator.getHorizontalSpan(15, depth);
		float maxUp = ARCalculator.getVerticalSpan(25, depth);
		
		if (Math.abs(up) <= maxUp && Math.abs(right) <= maxRight) {
			drawString(canvas, "Point in view", 250, 0);
			
			float offsetX = w * (right / maxRight);
			float offsetY = h * (up / maxUp);
			
			//canvas.drawCircle(w / 2 + offsetX, h / 2 + offsetY, 10, paint_);
		}
		
		String str = "Azimuth: " + azimuth_;
		drawString(canvas, str, 0, 0);
		drawString(canvas, "Point Vector", 0, 15);
		drawVector(canvas, ptVector, 0, 30);
		drawString(canvas, "Basis Matrix", 0, 45);
		drawMatrix(canvas, basis, 0, 60);
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
	
	private void drawVector(final Canvas canvas, final float[] v, int x, int y) {
		String str = "{";
		for (int i = 0; i < 3; i++) {
			float d = (int) (v[i] * 100) / 100f; 
			str += d + " ";
		}
		str = str.trim() + "}";
		
		drawString(canvas, str, x, y);
	}
	
	private void drawMatrix(final Canvas canvas, final float[][] m, int x, int y) {
		for (int i = 0; i < 3; i++)
			drawVector(canvas, m[i], x, y + (int) (paint_.getTextSize() * i));
	}

	@Override
	public void surfaceReady(int width, int height) {
	}
}
