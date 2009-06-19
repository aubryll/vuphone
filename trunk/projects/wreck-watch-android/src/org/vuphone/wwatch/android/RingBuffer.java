package org.vuphone.wwatch.android;

import java.util.ArrayList;
import java.util.List;

/**
 * A quick hack to make WaypointTracker more efficient. Only supports addition
 * right now.
 * 
 * @author Krzysztof Zienkiewicz
 * 
 * @param <T>
 */

public class RingBuffer<T> {

	private final int capacity_;
	private T[] data_;

	private int size_ = 0;
	private int last_ = 0;

	public RingBuffer() {
		this(50);
	}

	@SuppressWarnings("unchecked")
	public RingBuffer(int cap) {
		capacity_ = cap;
		data_ = (T[]) new Object[capacity_];
	}

	public int size() {
		return size_;
	}

	public boolean add(T obj) {
		data_[last_] = obj;
		last_ = (last_ + 1) % capacity_;
		++size_;
		if (size_ > capacity_)
			size_ = capacity_;
		
		return true;
	}

	public T get(int index) {
		int realIndex;
		if (size_ == capacity_)
			realIndex = (last_ + index) % capacity_;
		else
			realIndex = index;
		
		return data_[realIndex];
	}

	public List<T> getList() {
		ArrayList<T> list = new ArrayList<T>(size_);
		for (int i = 0; i < size_; ++i)
			list.add(get(i));
		
		return list;
	}
	
	public String toString() {
		return getList().toString();
	}


	public static void main(String[] args) {
		RingBuffer<Integer> ring = new RingBuffer<Integer>(5);
		for (int i = 0; i < 11; ++i) {
			ring.add(i);
			System.out.println(ring);
		}
	}
	
}