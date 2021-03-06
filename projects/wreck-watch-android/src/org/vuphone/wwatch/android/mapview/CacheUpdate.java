package org.vuphone.wwatch.android.mapview;

import java.util.List;

import org.vuphone.wwatch.android.Waypoint;

public class CacheUpdate {
	public static final int TYPE_EXPAND_LEFT = 0;
	public static final int TYPE_EXPAND_RIGHT = 1;
	public static final int TYPE_EXPAND_UP = 2;
	public static final int TYPE_EXPAND_DOWN = 3;
	public static final int TYPE_FULL_UPDATE = 4;

	private final List<Waypoint> points_;
	private final long latestTime_;
	private final int newValue_;
	private final int type_;

	public CacheUpdate(final List<Waypoint> wrecks, final long latestTime,
			final int type) {
		if (type != TYPE_FULL_UPDATE)
			throw new IllegalArgumentException(
					"Cannot use this constructor with anything but a full update");
		type_ = TYPE_FULL_UPDATE;
		latestTime_ = latestTime;
		points_ = wrecks;
		newValue_ = 0;
	}

	public CacheUpdate(final List<Waypoint> routes, final long latestTime,
			final int type, final int newValue) {
		points_ = routes;
		latestTime_ = latestTime;
		newValue_ = newValue;
		type_ = type;
	}

	public List<Waypoint> getWrecks() {
		return points_;
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
