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

public class SensorStabilizer {
	
	private final int size_;
	private int index_;
	private final float[] buffer_;
	private boolean full_;
	
	public SensorStabilizer(int s) {
		size_ = s;
		buffer_ = new float[size_];
		index_ = 0;
		full_ = false;
	}
	
	public void addReading(float d) {
		buffer_[index_] = d;
		index_++;
		if (index_ >= size_) {
			index_ = 0;
			full_ = true;
		}
	}
	
	public float getAverage() {
		if (!full_)
			return -1;
		
		float sum = 0;
		for (int i = 0; i < size_; i++)
			sum += buffer_[i];
		
		return sum / size_;
	}
}
