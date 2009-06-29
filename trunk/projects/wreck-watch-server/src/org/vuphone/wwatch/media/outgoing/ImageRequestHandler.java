package org.vuphone.wwatch.media.outgoing;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.vuphone.wwatch.media.incoming.ImageHandler;
import org.vuphone.wwatch.notification.InvalidFormatException;
import org.vuphone.wwatch.notification.Notification;
import org.vuphone.wwatch.notification.NotificationHandler;

public class ImageRequestHandler implements NotificationHandler{
	
	// XML will instantiate these
	private ImageRequestParser parser_;
	private DataSource ds_;
	
	private static final Logger logger_ = Logger
	.getLogger(ImageRequestHandler.class.getName());
	
	private class IDFilePair {
		public Integer id;
		public File file;
		
		public IDFilePair(Integer i, File f) {
			id = i;
			file = f;
		}
	}
	
	private ArrayList<IDFilePair> getFileList(ImageRequestNotification irn, Connection db) {

		try {
			db.setAutoCommit(true);

		} catch (SQLException e) {
			e.printStackTrace();
			logger_.log(Level.WARNING,
					"SQLException when setting auto commit: ", e);
		}

		String selector;
		int id;
		
		if (irn.isFullImage()){
			selector = "ImageID";
			id = irn.getImageID();
		} else {
			selector = "WreckID";
			id = irn.getWreckID();
		}
			
		// Prepare the SQL statement
		PreparedStatement prep = null;
		ResultSet rs;
		ArrayList<IDFilePair> fileList = new ArrayList<IDFilePair>();
		
		try {
			prep = db.prepareStatement(
					"select * from WreckImages where " + selector + " = ?;");
			prep.setInt(1, id);

			rs = prep.executeQuery();
			
			String prefix = ImageHandler.MINI_PREFIX;
			if (irn.isFullImage())
				prefix = "";
			
			while (rs.next()) {
				String name = rs.getString("FileName");
				File file = new File(ImageHandler.getIMAGE_DIRECTORY(), prefix + name);
				Integer imgID = Integer.valueOf(rs.getString("ImageID"));
				IDFilePair pair = new IDFilePair(imgID, file);
				fileList.add(pair);
			}
			rs.close();

		}catch (SQLException e) {
			logger_.log(Level.SEVERE,
					"SQLException in the statement: ", e);
			closeDatabase(db);
			return null;
		}
		
		return fileList;
	}
	
	public Notification handle(Notification n) {
		// TODO - Make this degrade gracefully on IOExceptions.
		/*
		 * The format of the response is as follows:
		 * Headers
		 * 
		 * Content-Type		application/octet-stream
		 * Content-Length	number of bytes in the entity stream
		 * ImageCount		number of images in the response stream
		 * ImageXSize		size in bytes of image X
		 * ImageXID			ID of the image from the database. Used to fetch the
		 * 					full quality copy
		 * 
		 * The actual image data are held in the stream as an array of bytes, 
		 * back to back
		 */
		
		ImageRequestNotification irn = null;

		
		Connection db = null;
		try {
			irn = parser_.getImage(n.getRequest(), n.getResponse());
			db = ds_.getConnection();
		} catch (InvalidFormatException ife) {
			
			ife.printStackTrace();
			logger_.log(Level.SEVERE,
					"Unable to parse the notification, stopping");
			closeDatabase(db);
			return n;
			
		} catch (SQLException e) {

			e.printStackTrace();
			logger_.log(Level.SEVERE,
			" Unable to continue without database, stopping");
			closeDatabase(db);
			return n;
		}
		
		ArrayList<IDFilePair> fileList = getFileList(irn, db);
		if (fileList == null)
			return n;

		HttpServletResponse response = irn.getResponse();
		
		// Preprocess the files to get sizes and set some headers
		response.setIntHeader("ImageCount", fileList.size());
		
		int totalSize = 0;
		for (int i = 0; i < fileList.size(); ++i) {
			int size = (int) fileList.get(i).file.length();
			int imgID = fileList.get(i).id;
			response.setIntHeader("Image" + i + "Size", size);
			response.setIntHeader("Image" + i + "ID", imgID);
			totalSize += size;
		}
		
		response.setContentLength(totalSize);
		response.setContentType("application/octet-stream");
		
		ByteArrayOutputStream toSend = new ByteArrayOutputStream(totalSize);
		
		for (int i = 0; i < fileList.size(); ++i) {

			int sz = (int) fileList.get(i).file.length();
			byte[] array = new byte[sz];
			int offset = 0;
			int numRead = 0;

			InputStream is = null;

			try {
				is = new FileInputStream(fileList.get(i).file);
				
				while (offset < sz && (numRead = is.read(array, offset, sz - offset)) >= 0){
					offset += numRead;
				}
				
				toSend.write(array);
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
				return null;
			} catch (IOException e) {

				e.printStackTrace();
				return null;
			}

		}
		
		try {
			toSend.flush();
			response.getOutputStream().write(toSend.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	
		return irn;
	}
	
	private void closeDatabase(Connection db) {
		try {
			db.close();
		} catch (SQLException e) {
			e.printStackTrace();
			logger_.log(Level.WARNING, "Unable to close database");
		}
	}

	public ImageRequestParser getParser() {
		return parser_;
	}

	public void setParser(ImageRequestParser parser) {
		parser_ = parser;
	}

	public DataSource getDataConnection() {
		return ds_;
	}

	public void setDataConnection(DataSource ds) {
		ds_ = ds;
	}

}
