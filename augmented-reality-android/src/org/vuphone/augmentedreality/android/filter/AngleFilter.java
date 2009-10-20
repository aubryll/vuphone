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

// An attempt to create a non blocking filter
package org.vuphone.augmentedreality.android.filter;

import org.vuphone.augmentedreality.android.utils.RingBuffer;

import android.util.Log;

public class AngleFilter extends Thread {
	
	private boolean shouldDie_;
	private RingBuffer<Float> data_;
	
	private static final int BUFFER_SIZE = 50;
	
	public AngleFilter() {
		super("AngleFilter Thread");
		
		shouldDie_ = false;
		data_ = new RingBuffer<Float>(BUFFER_SIZE);
	}
	
	public void addAngle(float angle) {

	}
	
	public float getFilteredAngle() {
		return 0;
	}
	
	public float getRawAngle() {
		return 0;
	}
	
	@Override
	public void run() {
		while (!shouldDie_) {

		}
	}

	@Override
	public synchronized void start() {
		super.start();
	}
}
