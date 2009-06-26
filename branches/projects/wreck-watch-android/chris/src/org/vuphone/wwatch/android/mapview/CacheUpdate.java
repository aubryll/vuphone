package org.vuphone.wwatch.android.mapview;

import java.util.List;

public class CacheUpdate {
	public static final int TYPE_EXPAND_LEFT = 0;
	public static final int TYPE_EXPAND_RIGHT = 1;
	public static final int TYPE_EXPAND_UP = 2;
	public static final int TYPE_EXPAND_DOWN = 3;
	
	private final List<Route> routes_;
	private final long latestTime_;
	private final int newValue_;
	private final int type_;
	
	public CacheUpdate(final List<Route> routes, final long latestTime, final int type, final int newValue) {
		routes_ = routes;
		latestTime_ = latestTime;
		newValue_ = newValue;
		type_ = type;
	}
	
	public List<Route> getRoutes() {
		return routes_;
	}
	
	public long getLatestTime() {
		return latestTime_;
	}
	
	public int getType() {
		return type_;
	}
	
	public int getNewValue() {
		return newValue_;
	}
}
