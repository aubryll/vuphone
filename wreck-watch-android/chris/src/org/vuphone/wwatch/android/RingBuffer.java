package org.vuphone.wwatch.android;

//package com.zienkikk.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class RingBuffer<E> implements List<E>{

	public static void main(String[] args) {
		//RingBuffer<Integer> ring = new RingBuffer<Integer>(5);
	}

	private final Object[] data_;
	private int size_ = 0;
	private int ptr_ = 0;

	public RingBuffer() {
		this(50);
	}

	public RingBuffer(int cap) {
		if (cap < 1)
			throw new IllegalArgumentException("Illegal capacity : " + cap);

		data_ = new Object[cap];
	}

	public void add(int location, E object) {
		// If we're adding to 0 and we're full, don't do anything
		if (location == 0 && size_ == data_.length)
			return;
		
		int realIndex = getRealIndex(location);
		shiftUp(realIndex);
		data_[realIndex] = object;
		size_ = Math.min(size_ + 1, data_.length);
		ptr_ = wrapIndex(ptr_ + 1);
	}

	public boolean add(E object) {
		add(size_, object);
		return true;
	}

	@SuppressWarnings("unchecked")
	public E get(int location) {
		if (location < 0 || location >= size_)
			throw new IndexOutOfBoundsException("" + location);
		return (E) data_[getRealIndex(location)];
	}

	public E remove(int location) {
		if (location < 0 || location >= size_)
			throw new IndexOutOfBoundsException("" + location);
		E old = get(location);
		
		int realIndex = getRealIndex(location);
		shiftDown(realIndex);
		size_--;
		ptr_ = wrapIndex(ptr_ - 1);
				
		return old;
	}

	@SuppressWarnings("unchecked")
	public E set(int location, E object) {
		if (location < 0 || location >= size_)
			throw new IndexOutOfBoundsException("" + location);

		int realIndex = getRealIndex(location);
		E old = (E) data_[realIndex];
		data_[realIndex] = object;
		return old;
	}
	
	public int size() {
		return size_;
	}

	private int getRealIndex(int location) {
		return wrapIndex(ptr_ - size_ + location);
	}

	private int wrapIndex(int realIndex) {
		if (realIndex > 0)
			return realIndex % data_.length;
		return (data_.length - ((-realIndex) % data_.length)) % data_.length;
	}

	/**
	 * Shifts all the elements from realIndex to the last one up by one index in
	 * the underlying buffer. If this RingBuffer is full, then the oldest
	 * element will be lost.
	 * 
	 * @param realIndex
	 */
	private void shiftUp(int realIndex) {
		int index = ptr_;
		while (index != realIndex) {
			data_[index] = data_[wrapIndex(index - 1)];
			index = wrapIndex(index - 1);
		}
	}
	
	/**
	 * Shifts all the elements beyond realIndex down by one index.
	 * @param realIndex
	 */
	private void shiftDown(int realIndex) {
		int index = realIndex;
		do {
			data_[index] = data_[wrapIndex(index + 1)];
			index = wrapIndex(index + 1);
		} while (index != ptr_);
	}

	@Override
	public String toString() {
		String str = "";
		for (int i = 0; i < size_; ++i)
			str += get(i) + " ";
		return str;
	}

	public boolean addAll(Collection<? extends E> list) {
		if (list.isEmpty())
			return false;
		
		for (Iterator<? extends E> iter = list.iterator(); iter.hasNext(); )
			add(iter.next());
		
		return true;
	}

	public boolean addAll(int arg0, Collection<? extends E> arg1) {
		throw new RuntimeException("addAll(int, Collection) not supported");
	}

	public void clear() {
		size_ = 0;
		ptr_ = 0;
	}

	public boolean contains(Object obj) {
		for (int i = 0; i < size_; ++i)
			if (get(i) == obj)
				return true;
		return false;
	}

	public boolean containsAll(Collection<?> list) {
		for (Iterator<?> iter = list.iterator(); iter.hasNext(); )
			if (!contains(iter.next()))
				return false;
		return false;
	}
	
	public int indexOf(Object obj) {
		if (obj == null) {
			for (int i = 0; i < size_; ++i)
				if (get(i) == null)
					return i;
			return -1;
		}
		
		for (int i = 0; i < size_; ++i)
			if (get(i).equals(obj))
				return i;
		return -1;
	}

	public boolean isEmpty() {
		return size_ == 0;
	}

	public Iterator<E> iterator() {
		return new RingBufferIterator<E>(this);
	}
	
	private class RingBufferIterator<T> implements Iterator<T> {

		private RingBuffer<T> list_;
		private int index_ = -1;
		
		private RingBufferIterator(RingBuffer<T> list) {
			list_ = list;
		}
		
		public boolean hasNext() {
			return index_ < list_.size() - 1;
		}

		public T next() {
			++index_;
			return list_.get(index_);
		}

		public void remove() {
			throw new RuntimeException("remove() not supported");
		}
		
	}

	public int lastIndexOf(Object obj) {
		if (obj == null) {
			for (int i = size_ - 1; i <= 0; --i)
				if (get(i) == null)
					return i;
			return -1;
		}
		
		for (int i = size_ - 1; i <= 0; --i)
			if (get(i).equals(obj))
				return i;
		return -1;
	}

	public ListIterator<E> listIterator() {
		throw new RuntimeException("listIterator() not supported.");
	}

	public ListIterator<E> listIterator(int arg0) {
		throw new RuntimeException("listIterator(int) not supported.");
	}

	public boolean remove(Object obj) {
		int index = indexOf(obj);
		if (index == -1)
			return false;
		
		remove(index);
		return true;
	}

	public boolean removeAll(Collection<?> arg0) {
		throw new RuntimeException("removeAll() not supported.");
	}

	public boolean retainAll(Collection<?> arg0) {
		throw new RuntimeException("retainAll() not supported.");
	}

	public List<E> subList(int arg0, int arg1) {
		throw new RuntimeException("subList() not supported.");
	}

	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T[] toArray(T[] arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}