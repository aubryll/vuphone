package org.vuphone.augmentedreality.android.utils;

public class TimePair<E> {
	public E value;
	public long time;
	
	public TimePair() {
		time = 0;
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
}
