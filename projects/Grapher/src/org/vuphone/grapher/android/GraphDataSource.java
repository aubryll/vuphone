package org.vuphone.grapher.android;

import java.util.ArrayList;

import android.graphics.PointF;

// TODO - All the "new"s here cause a huge gc activity. Refactor to use a ring buffer.

public class GraphDataSource {
	private ArrayList<PointF> list_;
	
	public GraphDataSource() {
		list_ = new ArrayList<PointF>(100);
	}
	
	public void addData(float x, float y) {
		list_.add(new PointF(x, y));
	}
	
	public void clear() {
		list_.clear();
	}
	
	public PointF get(int index) {
		return list_.get(index);
	}
	
	public int size() {
		return list_.size();
	}
	
	@Override
	public String toString() {
		return list_.toString();
	}
}