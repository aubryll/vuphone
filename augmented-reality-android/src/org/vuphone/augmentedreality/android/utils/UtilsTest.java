package org.vuphone.augmentedreality.android.utils;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class UtilsTest extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		RingBuffer<Float> buffer = new RingBuffer<Float>(10);
		Log.v("BufferTest", "Empty: " + buffer.toString());
		Log.v("BufferTest", "\tDump: " + buffer.toString());
		
		for (float i = 1.5f; !buffer.isFull(); i += 1)
			buffer.enqueue(i);

		Log.v("BufferTest", "Full: " + buffer.toString());
		Log.v("BufferTest", "\tDump: " + buffer.toString());

		buffer.enqueue(Float.valueOf(100));
			
		Log.v("BufferTest", "One more: " + buffer.toString());
		Log.v("BufferTest", "\tDump: " + buffer.toString());
	}
}
