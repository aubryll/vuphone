 /**************************************************************************
 * Copyright 2009 Chris Thompson                                           *
 *                                                                         *
 * Licensed under the Apache License, Version 2.0 (the "License");         *
 * you may not use this file except in compliance with the License.        *
 * You may obtain a copy of the License at                                 *
 *                                                                         *
 * http://www.apache.org/licenses/LICENSE-2.0                              *
 *                                                                         *
 * Unless required by applicable law or agreed to in writing, software     *
 * distributed under the License is distributed on an "AS IS" BASIS,       *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.*
 * See the License for the specific language governing permissions and     *
 * limitations under the License.                                          *
 **************************************************************************/
package org.vuphone.vandyupon.media.incoming.event;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.codec.binary.Hex;
import org.vuphone.vandyupon.notification.HandlerFailedException;
import org.vuphone.vandyupon.notification.InvalidFormatException;
import org.vuphone.vandyupon.notification.Notification;
import org.vuphone.vandyupon.notification.NotificationHandler;
import org.vuphone.vandyupon.notification.ResponseNotification;

public class ImageHandler implements NotificationHandler {

	private static final Logger logger_ = Logger.getLogger(ImageHandler.class.getName());
	private DataSource ds_;
	public static final String FILE_EXTENSION = ".jpg";
	public static final String IMAGE_DIRECTORY = "html/images";
	public static final String MINI_PREFIX = "mini";
	
	/**
	 * This helper method asserts that the images directory exists and if it doesn't,
	 * creates it.
	 */
	private static boolean checkImagesDirectory(){
		File dir = new File(IMAGE_DIRECTORY);
		if (!dir.exists())
			return dir.mkdir();			
		else
			return true;
	}

	public ResponseNotification handle(Notification n) throws HandlerFailedException{
		
		if (!checkImagesDirectory()){
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(new Exception("Directory could not be created or found"));
			throw hfe;
		}
		
		if (!(n instanceof ImageNotification)){
			HandlerFailedException hfe = new HandlerFailedException();
			hfe.initCause(new InvalidFormatException());
			throw hfe;
		}
			

		ImageNotification in = (ImageNotification)n;
		ImageHandledNotification ihn = null;
		
		//first get the ImageNotification by parsing the Request.
		
		Connection db = null;

		try {
			db = ds_.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			logger_.log(Level.SEVERE,
					"Unable to initialize org.sqlite.JDBC");
			logger_.log(Level.SEVERE,
					" Unable to continue without a database, stopping");
			return ihn;
		}
		
		//get the initial value to use from the database from the imageId column
		//but only get the first time.
						
		//create fileName
		/*
		 * Filename is now a SHA-1 has of the content of the image.  This reduces 
		 * database traffic and guarantees no synchronization is necessary.
		 * 
		 * In testing it was able to compute the hash of a 4MB file in approx
		 * .1 seconds.
		 */
		String fileName;
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
			return ihn;
		}
		md.update(in.getBytes());
		fileName = new String(Hex.encodeHex(md.digest()));
		
	
		//put data from ImageNotification into the database
		try {
			// Prepare the SQL statement
			PreparedStatement prep = null; 
			prep = 
				db.prepareStatement("INSERT INTO eventmeta (eventid, value, submissiontime, metatype) " +
					"VALUES (?, ?, ?,(select typeid from metatypes where typename like 'IMAGE'))");
			prep.setLong(1, in.getEventId());
			prep.setString(2, fileName + FILE_EXTENSION);
			prep.setLong(3, System.currentTimeMillis());
			prep.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return ihn;
		}
		
		// Save the image from the POST.
		File imageFile = new File(ImageHandler.IMAGE_DIRECTORY, fileName + FILE_EXTENSION);
		File miniFile = new File(IMAGE_DIRECTORY, MINI_PREFIX + fileName + FILE_EXTENSION);
		try {
			if (!imageFile.exists())
				imageFile.createNewFile();
			if (!miniFile.exists())
				miniFile.createNewFile();
			
			FileOutputStream writer = new FileOutputStream(imageFile);
			
			writer.write(in.getBytes(), 0, in.getBytes().length - 1);
			writer.close();
			
			// Scale the image down and save it
			BufferedImage img = ImageManipulator.scaleDown(imageFile);
			ImageManipulator.saveImage(img, miniFile);
			
		} catch (IOException excp) {
			logger_.log(Level.SEVERE, "Got an IOException: " + excp.getMessage());
			excp.printStackTrace();
			return ihn;
		}
		
		//create and return a ImageHandledNotification showing the request was handled.
		ihn = new ImageHandledNotification(in.getResponseType(), in.getCallback());
		ihn.setTime(in.getTime());
		ihn.setEventId(in.getEventId());
		ihn.setFileName(fileName + FILE_EXTENSION);
		return ihn;
	}
	
	
	public void setDataConnection(DataSource ds){
		ds_ = ds;
	}
	
	public DataSource getDataConnection(){
		return ds_;
	}

}
