package org.vuphone.wwatch.android;

/**
 * An interface containing signatures to callback methods that must be
 * implemented for an object listening to ImageUploader
 * 
 * @author Krzysztof Zienkiewicz
 * 
 */
public interface ImageUploadListener {

	/**
	 * Called when the server returns with a response code other than 200.
	 */
	public void badServerResponse();

	/**
	 * Called when the file to be uploaded cannot be found by ImageUploader.
	 * After calling this callback, ImageUploader stops performing the operation
	 * that caused this call.
	 */
	public void fileNotFound();

	/**
	 * Called when the server returns with a 200 response code.
	 */
	public void goodServerResponse();

	/**
	 * Called when the file upload fails. TODO - Put in a parameter that
	 * specifies the reason the upload failed.
	 */
	public void serverUploadFailed();

	/**
	 * Called from uploadImage() to fetch the meta information. Implementors
	 * should write all data directly to info.
	 * 
	 * @param info
	 *            An object that will store the meta information.
	 */
	public void setMetaInformation(final ImageUploadMetaInformation info);
}