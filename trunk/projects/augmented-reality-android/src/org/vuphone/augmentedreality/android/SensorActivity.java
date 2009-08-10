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

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class SensorActivity extends Activity {
	private SensorManager sensorManager_;
	private GraphView graph_;

	private class GraphView extends View implements SensorEventListener {
		private static final float FONT_SIZE = 12;

		private FIRAngleFilter filter_ = new FIRAngleFilter(75);
		
		private Paint paint_ = new Paint();
		private Canvas canvas_ = new Canvas();
		private Bitmap bitmap_;
		
		private Rect graphArea_ = new Rect(30, 30, -1, -1);
		
		private int topAngle_ = 90;
		private int range_ = 180;
		private int domain_ = 100;
		
		private final int axisPoints_ = 10;
		
		private float scaleX_, scaleY_;
		
		private int[] color_ = {Color.WHITE, Color.RED};
		private int[] stroke_ = {1, 3};
		private float[][] data_ = new float[2][domain_];
		private int dataIndex_ = 0;
		
		private float rawAngle_;
		
		private Rect selector_ = new Rect(-1, 30, -1, 1);
		
		public GraphView(Context context) {
			super(context);
			
			paint_.setTextSize(FONT_SIZE);
			paint_.setColor(Color.BLACK);
		}

		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			super.onSizeChanged(w, h, oldw, oldh);
			
			bitmap_ = Bitmap.createBitmap(w, h, Config.RGB_565);
			canvas_.setBitmap(bitmap_);
			graphArea_.right = w - 20;
			graphArea_.bottom = h - 20;
			
			selector_.left = w - 10;
			selector_.right = w - 5;
			selector_.bottom = graphArea_.bottom;
			
			redraw();
			
		}
		
		private void redraw() {
			synchronized (this) {
				canvas_.drawColor(Color.WHITE);
				calculateScales();
				paint_.setColor(Color.BLACK);
				canvas_.drawRect(graphArea_, paint_);
				
				dataIndex_ = 0;
				
				invalidate();
			}
		}
		
		private void mark(int x, float high, float low) {
			float ptX = graphArea_.left + x * scaleX_;
			float ptY1 = graphArea_.top + (high - topAngle_) * scaleY_;
			float ptY2 = graphArea_.top + (low - topAngle_) * scaleY_;
			
			paint_.setStrokeWidth(2);
			paint_.setColor(Color.BLUE);
			canvas_.drawPoint(ptX, ptY1, paint_);
			canvas_.drawPoint(ptX, ptY2, paint_);
		}
		
		private void calculateScales() {
			scaleY_ = (float) graphArea_.height() / range_;
			scaleX_ = (float) graphArea_.width() / domain_;
		}
		
		private void drawScales() {
			float inc = (float) graphArea_.height() / axisPoints_;
			int angDelta = range_ / axisPoints_;
			for (int i = 0; i <= axisPoints_; i++) {
				float y = graphArea_.top + i * inc;
				
				int angle = topAngle_ + i * angDelta;
				String str = "" + angle;
				
				canvas_.drawCircle(graphArea_.left - 1, y, 2, paint_);
				canvas_.drawText(str, 0, y + FONT_SIZE / 2 , paint_);
			}
		}
		
		private void drawAngle(int x) {
			synchronized (this) {
				for (int i = 0; i < 2; i++) {
					float stopX = graphArea_.left + x * scaleX_;
					float stopY = graphArea_.top + (data_[i][x] - topAngle_) * scaleY_;
					
					
					paint_.setColor(color_[i]);
					paint_.setStrokeWidth(stroke_[i]);
					
					canvas_.drawPoint(stopX, stopY, paint_);
					
					if (x != 0) {
						float startX = graphArea_.left + (x - 1) * scaleX_;
						float startY = graphArea_.top + (data_[i][x - 1] - topAngle_) * scaleY_;	
						canvas_.drawLine(startX, startY, stopX, stopY, paint_);
					}
				}
			}
		}

		@Override
		protected void onDraw(Canvas canvas) {
			synchronized (this) {
				canvas.drawBitmap(bitmap_, 0, 0, null);
				paint_.setColor(Color.BLACK);
				
				drawScales();
				
				canvas.drawText("Raw: " + rawAngle_, 0, FONT_SIZE, paint_);
				
				canvas.drawText("TopAngle: " + topAngle_, 70, FONT_SIZE, paint_);
				canvas.drawText("Range: " + range_, 160, FONT_SIZE, paint_);
				
				canvas.drawText("Outliers: " + filter_.getOutlierSize(), 240, FONT_SIZE, paint_);
				
				canvas.drawRect(selector_, paint_);
				
			}
		}
		
		public void resetView() {
			topAngle_ = 0;
			range_ = 360;
			redraw();
		}
		
		@Override
		public boolean onTrackballEvent(MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_UP:
				int newAngle = topAngle_ + (int) ((selector_.top - graphArea_.top) / scaleY_);
				int newRange = (int) (selector_.height() / scaleY_);
				
				topAngle_ = newAngle;
				range_ = newRange;
				redraw();
				
				break;
			default:
				int dy = (int) (event.getY() * 20);
				int dx = (int) (event.getX() * 20);
				selector_.offset(0, dy);
				selector_.bottom += dx;
				break;
			}
			
			return true;
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			if (event.accuracy != SensorManager.SENSOR_STATUS_ACCURACY_HIGH)
				return;
			
			synchronized (this) {
				if (dataIndex_ >= domain_) {
					dataIndex_ = 0;
					paint_.setColor(Color.BLACK);
					canvas_.drawRect(graphArea_, paint_);
					paint_.setColor(Color.WHITE);
				}
				
				data_[0][dataIndex_] = rawAngle_ = event.values[0];
				filter_.add(rawAngle_);
				float mean;
				data_[1][dataIndex_] = mean = filter_.getAngle();
				
				float dev = FIRAngleFilter.THRESHOLD * filter_.getStdDev();
				drawAngle(dataIndex_);
				mark(dataIndex_, mean + dev, mean - dev);
				dataIndex_++;
				
				invalidate();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sensorManager_ = (SensorManager) getSystemService(SENSOR_SERVICE);
		graph_ = new GraphView(this);
		setContentView(graph_);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Sensor or = sensorManager_.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		sensorManager_.registerListener(graph_, or,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onStop() {
		sensorManager_.unregisterListener(graph_);
		super.onStop();
	}
	
	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		graph_.onTrackballEvent(event);
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
			graph_.resetView();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
