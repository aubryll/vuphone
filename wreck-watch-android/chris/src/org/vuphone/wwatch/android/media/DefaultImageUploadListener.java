package org.vuphone.wwatch.android.media;

import org.vuphone.wwatch.android.VUphone;

import android.util.Log;

/**
 * A default implementation of ImageUploadListener that takes minimal actions,
 * mostly logging its method calls.
 * 
 * @author Krzysztof Zienkiewicz
 * 
 */
public class DefaultImageUploadListener implements ImageUploadListener {

	public void badServerResponse() {
		Log.v(VUphone.tag, "DefaultImageUploadListener.badServerResponse()");
	}

	public void fileNotFound() {
		Log.v(VUphone.tag, "DefaultImageUploadListener.fileNotFound()");
	}

	public void goodServerResponse() {
		Log.v(VUphone.tag, "DefaultImageUploadListener.goodServerResponse()");
	}
	
	public void imageLoaded() {
		Log.v(VUphone.tag, "DefaultImageUploadListener.imageLoaded()");
	}

	public void serverUploadFailed() {
		Log.v(VUphone.tag, "DefaultImageUploadListener.serverUploadFailed()");
	}

	public void setMetaInformation(ImageUploadMetaInformation info) {
		Log.v(VUphone.tag, "DefaultImageUploadListener.setMetaInformation()");
	}

}