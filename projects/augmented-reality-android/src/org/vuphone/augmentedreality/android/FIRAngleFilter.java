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

import android.util.Log;

public class FIRAngleFilter {

	private final int capacity_;
	public int size_; 				// Number of elements (<= capacity_)
	private int index_; 			// Index of the oldest reading
	private final float[] buffer_;	// Holds the angle data
	private float unfiltered_;
	
	private final float[] outliers_;
	private int outIndex_;

	public static float THRESHOLD = 2.5f;
	
	public FIRAngleFilter(int cap) {
		capacity_ = cap;
		size_ = index_ = 0;
		buffer_ = new float[capacity_];
		
		outliers_ = new float[15];
		outIndex_ = 0;
	}

	public void add(float angle) {
		unfiltered_ = angle;
		// Calculate the coefficient for this value
		
		float mean = getMean();
		float dev = getStdDev();
		float z = (angle - mean) / dev;
		//Log.v("AndroidTests", "Z: " + z);
		
		
		if (size_ == capacity_ && Math.abs(z) > THRESHOLD) {
			outIndex_++;
			//Log.v("AndroidTests", "Outlier");
			if (outIndex_ >= outliers_.length) {
				//Log.v("AndroidTests", "Outlier Limit: Resetting buffer");
				outIndex_ = 0;
				size_ = 0;
			}
			
			return;
		}
		
		

		//float c = 1 / z;
		
		if (size_ == capacity_) {
			buffer_[index_] = angle;
			//coeffs_[index_] = c;
			index_ = (index_ + 1) % capacity_;
			return;
		}

		buffer_[(index_ + size_) % capacity_] = angle;
		//coeffs_[(index_ + size_) % capacity_] = c;
		// TODO - Above offset evaluates to index_ if were full so we can
		// combine with the if above
		size_++;
	}
	
	public float getAngle() {
		return getMean();
	}
	
	public float getUnfiltered() {
		return unfiltered_;
	}
	
	private float getMean() {
		if (size_ == 0)
			return 0;
		
		float sum = 0;
		for (int i = 0; i < size_; i++) {
			sum += buffer_[(index_ + i) % capacity_];
		}

		return sum / size_;
	}
	
	public int getOutlierSize() {
		return outIndex_;
	}
	
	public float getStdDev() {
		if (size_ == 0)
			return 0;
		
		float sumSqrd = 0;
		float mean = getMean();
		
		for (int i = 0; i < size_; i++) {
			float angle = buffer_[(index_ + i) % capacity_]; 
			sumSqrd += (angle - mean) * (angle - mean);
		}
		
		return (float) Math.sqrt(sumSqrd / size_);
	}

	public String dump() {
		String str = "";
		for (int i = 0; i < capacity_; i++)
			str += ", " + buffer_[i];
		return "{" + str.substring(2) + "}";
	}

	@Override
	public String toString() {
		if (size_ == 0)
			return "Empty";

		float mean = getMean();
		float dev = getStdDev();
		
		String str = "";
		for (int i = 0; i < size_; i++) {
			float angle = buffer_[(index_ + i) % capacity_];
			float z = (angle - mean) / dev;
			str += ", " + angle + ": " + z;
		}

		return "{" + str.substring(2) + "} u = " + mean + " s = " + dev;
	}
}
