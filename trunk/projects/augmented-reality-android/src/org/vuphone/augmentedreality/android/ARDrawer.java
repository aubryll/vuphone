package org.vuphone.augmentedreality.android;

import android.graphics.Canvas;

public interface ARDrawer {
	void draw(Canvas canvas);
	void surfaceReady(int width, int height);
}
