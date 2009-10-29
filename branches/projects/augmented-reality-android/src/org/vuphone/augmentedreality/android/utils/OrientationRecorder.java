package org.vuphone.augmentedreality.android.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.util.Log;

public class OrientationRecorder {

	private FileOutputStream os_;
	//private StringBuilder str_;
	
	public OrientationRecorder() {
		os_ = null;
		//str_ = new StringBuilder(40);
	}
	
	public void close() {
		if (os_ != null) {
			try {
				os_.flush();
				os_.close();
				os_ = null;
			} catch (IOException e) {
				Log.v("OrientationRecorder", "Exception during close");
				e.printStackTrace();
			}
		}
	}
	
	public void open(String file, Context context) {
		if (os_ != null) {
			Log.v("OrientationRecorder", "Open called on an active recorder. " +
					"Closing previous session.");
			close();
		}
		
		try {
			os_ = context.openFileOutput(file, Context.MODE_WORLD_READABLE);
		} catch (FileNotFoundException e) {
			Log.v("OrientationRecorder", "FileNotFound Exception");
			e.printStackTrace();
			System.exit(2);
		}
	}
	
	public void write(float value) {
		write(value, System.currentTimeMillis());
	}
	
	public void write(float value, long time) {
		// TODO - This is not very efficient. Use StringBuilder
		String str = time + " - " + value;
		try {
			os_.write(str.getBytes());
		} catch (IOException e) {
			Log.v("OrientationRecorder", "IOException during write");
			e.printStackTrace();
			System.exit(2);
		}
	}
	
}
