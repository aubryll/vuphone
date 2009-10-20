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

// The most recent element is at the tail. The oldest is at head

package org.vuphone.augmentedreality.android.utils;

import java.util.NoSuchElementException;

public class RingBuffer<E> {

	private final TimePair<E>[] buffer_;
	private int head_, tail_, count_;
	
	@SuppressWarnings("unchecked")
	public RingBuffer(int size) {
		buffer_ = new TimePair[size];
		for (int i = 0; i < size; i++) {
			buffer_[i] = new TimePair<E>();
		}
		head_ = tail_ = 0;
	}
	
	public int decrement(int index) {
		return (index + buffer_.length - 1) % buffer_.length; 
	}
	
	public E dequeue() {
		E val = front();
		
		count_--;
		head_ = decrement(head_);
		
		return val;
	}
	
	public String dump() {
		StringBuilder str = new StringBuilder(buffer_.length * 4);
		str.append("Buffer Dump: {");

		for (int i = 0; i < buffer_.length; i++) {
			str.append(buffer_[i]).append(", ");
		}
		
		str.setLength(str.lastIndexOf(",") + 1);
		str.append("}");
		return str.toString();
	}
	
	public void enqueue(E value) {
		buffer_[tail_].value = value;
		tail_ = increment(tail_);

		// If we're full. Simply overwrite the old head
		if (isFull()) {
			head_ = increment(head_);
		} else {
			count_++;			
		}
	}
	
	public E front() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		
		return buffer_[head_].value;
	}
	
	public E get(int index) {
		if (index < 0 || index >= count_)
			throw new NoSuchElementException();
		
		// 0 gives the value at head
		int i = (head_ + index) % buffer_.length;
		return buffer_[i].value;
	}
	
	private int increment(int index) {
		return (index + 1) % buffer_.length;
	}
	
	public boolean isEmpty() {
		return count_ == 0;
	}
	
	public boolean isFull() {
		return count_ == buffer_.length;
	}
	
	public int size() {
		return count_;
	}
	
	public String toString() {
		if (isEmpty())
			return "{}";
		
		StringBuilder str = new StringBuilder(count_ * 4);
		str.append("{");
		
		int i = head_;
		
		do {
			str.append(buffer_[i]).append(", ");
			i = increment(i);
		} while (i != tail_);
		
		str.setLength(str.lastIndexOf(",") + 1);
		str.append("}");
		return str.toString();
	}
	
	public static void main() {
		RingBuffer<Integer> buf = new RingBuffer<Integer>(10);
		System.out.print(buf);
	}
}
