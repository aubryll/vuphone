package com.zienkikk.android.grapher;

import android.graphics.Color;

/**
 * A class that defines the configurations used by GraphRenderer to draw a particular data series.
 * 
 * @author Krzysztof Zienkiewicz
 *
 */
public class GraphDrawConfig {
	
	public static final int CIRCLE = 0;
	public static final int SQUARE = 1;
	
	public int color;	// The color of the paint
	public float size;	// The size of the mark to make
	public int style;	// The style to use. One of the constants defined in this class.
	
	public GraphDrawConfig() {
		color = Color.WHITE;
		size = 1;
		style = SQUARE;
	}
}
