package org.vuphone.wwatch.android;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

public class ImageUploader {

	private static final String SERVER = "http://129.59.135.144:8080";
	private static final String PATH = "/wreckwatch/notifications";

	private byte[] data_ = null;
	private ImageUploadListener listener_;

	private final Uri uri_;
	private final ContentResolver resolver_;

	private final HttpPost post_;
	private final ImageUploadMetaInformation meta_ = new ImageUploadMetaInformation();

	private Thread loaderThread_, uploaderThread_;

	public ImageUploader(Uri uri, Context c) {
		uri_ = uri;
		resolver_ = c.getContentResolver();
		
		listener_ = new DefaultImageUploadListener();
		
		post_ = new HttpPost();
		post_.addHeader("Content-Type", resolver_.getType(uri_));
	}

	/**
	 * Returns the size of the image pointed to by the URI passed into the
	 * constructor or -1 if the file does not exist. All exceptions are
	 * swallowed so use the -1 return value to take appropriate actions.
	 * 
	 * @return Size of the image or -1 if the file does not exist.
	 */
	private int getImageSize() {
		try {
			ParcelFileDescriptor desc = resolver_.openFileDescriptor(uri_, "r");
			long size = desc.getStatSize();
			desc.close();
			return (int) size;
		} catch (FileNotFoundException e) {
			Log.v(VUphone.tag, "FileNotFoundException in ImageUploader."
					+ "getImageSize(). Uri: " + uri_.toString());
		} catch (IOException e) {
			Log.v(VUphone.tag, "IOException in ImageUploader.getImageSize(). "
					+ "Uri: " + uri_.toString());
		}

		return -1;
	}

	/**
	 * Returns a byte array containing the raw image data or null if an
	 * exception occurred.
	 * 
	 * @return
	 */
	private byte[] getImageByteData() {
		int sz = getImageSize();
		if (sz == -1) {
			return null;
		}
		byte[] array = new byte[sz];

		try {
			InputStream is = resolver_.openInputStream(uri_);
			int offset = 0;
			int numRead = 0;

			while (offset < sz
					&& (numRead = is.read(array, offset, sz - offset)) >= 0) {
				offset += numRead;
			}
			is.close();
			return array;

		} catch (FileNotFoundException e) {
			Log.v(VUphone.tag, "FileNotFoundException in ImageUploader."
					+ "getImageByteData(). Uri: " + uri_.toString());
		} catch (IOException e) {
			Log.v(VUphone.tag,
					"IOException in ImageUploader.getImageByteData(). "
							+ "Uri: " + uri_.toString());
		}

		return null;
	}

	/**
	 * Loads the image data in a separate thread so this method returns
	 * immediately. If this method was unable to load the data, the listener's
	 * fileNotFound() method gets called.
	 */
	public void loadImage() {
		loaderThread_ = new Thread(new Runnable() {
			public void run() {
				Log.v(VUphone.tag, "ImageUploader.loadImage() starting...");
				data_ = getImageByteData();

				if (data_ == null) {
					// Some exception occurred so execute listener callback and
					// quit
					listener_.fileNotFound();
					return;
				}
				// Prepare the entity for the POST.
				ByteArrayEntity ent = new ByteArrayEntity(data_);
				post_.setEntity(ent);

				Log.v(VUphone.tag,
						"ImageUploader.loadImage() loaded successfully");
			}
		}, "LoadImageThread");
		loaderThread_.start();
	}

	/**
	 * Uploads the image data to the server. This method runs in its own Thread
	 * so it returns immediately. This method calls the listener's
	 * setMetaInformation() method to obtain the necessary meta data and so
	 * should only be called once the listener is ready to provide this
	 * information.
	 */
	public void uploadImage() {
		uploaderThread_ = new Thread(new Runnable() {
			public void run() {
				try {
					loaderThread_.join();
				} catch (InterruptedException e){}
				
				if (data_ == null){
					listener_.fileNotFound();
					return;
				}
				
				listener_.setMetaInformation(meta_);
				String uriStr = SERVER + PATH + "?type=image&" + meta_;
				post_.setURI(URI.create(uriStr));
				HttpClient c = new DefaultHttpClient();
				
				try {
					HttpResponse r = c.execute(post_);
					int code = r.getStatusLine().getStatusCode();
					if (code == 200)
						listener_.goodServerResponse();
					else
						listener_.badServerResponse();
				} catch (IOException e) {
					listener_.serverUploadFailed();
				}
			}
		}, "UploadImageThread");
		
		uploaderThread_.start();

	}
}
