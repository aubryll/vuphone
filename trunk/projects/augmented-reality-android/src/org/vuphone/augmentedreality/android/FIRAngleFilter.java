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

public class FIRAngleFilter {

	private final int capacity_;
	private int size_; // Number of elements (<= capacity_)
	private int index_; // Index of the oldest reading
	private final float[] buffer_;

	public FIRAngleFilter(int cap) {
		capacity_ = cap;
		size_ = index_ = 0;
		buffer_ = new float[capacity_];
	}

	public void add(float angle) {
		if (size_ == capacity_) {
			buffer_[index_] = angle;
			index_ = (index_ + 1) % capacity_;
			return;
		}

		buffer_[(index_ + size_) % capacity_] = angle;
		// TODO - Above offset evaluates to index_ if were full so we can
		// combine with the if above
		size_++;
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

		String str = "";
		for (int i = 0; i < size_; i++) {
			str += ", " + buffer_[(index_ + i) % capacity_];
		}

		return "{" + str.substring(2) + "}";
	}
}
