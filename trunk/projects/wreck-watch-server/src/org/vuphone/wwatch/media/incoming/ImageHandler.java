package org.vuphone.wwatch.media.incoming;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.vuphone.wwatch.notification.InvalidFormatException;
import org.vuphone.wwatch.notification.Notification;
import org.vuphone.wwatch.notification.NotificationHandler;

public class ImageHandler implements NotificationHandler {

	private static final Logger logger_ = Logger.getLogger(ImageHandler.class.getName());
	private DataSource ds_;
	private ImageParser parser_;
	public static final String FILE_EXTENSION = ".jpg";
	private static int FILE_NAME_PREFIX = -1;
	public static final String IMAGE_DIRECTORY = "images";
	public static final String MINI_PREFIX = "mini";

	public Notification handle(Notification n) {

		ImageNotification in;
		ImageHandledNotification ihn = null;
		
		//first get the ImageNotification by parsing the Request.
		try {
			in = parser_.getImageNotification(n);
		} catch (InvalidFormatException excp) {
			logger_.log(Level.SEVERE, "Got InvalidFormatException: " + excp.getMessage());
			return ihn;
		}
		
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
		if (ImageHandler.FILE_NAME_PREFIX == -1)
		{
			getInitialFileNamePrefix(db);
		}
						
		//create fileName
		String fileName;
		synchronized (ImageHandler.class)
		{
			ImageHandler.FILE_NAME_PREFIX++;
			fileName = Integer.toString(ImageHandler.FILE_NAME_PREFIX) + ImageHandler.FILE_EXTENSION;
		}

		//put data from ImageNotification into the database
		// Insert wreck into database
		try {
			// Prepare the SQL statement
			PreparedStatement prep = null; 
			prep = db
					.prepareStatement("INSERT INTO WreckImages (ImageId, WreckID, FileName, Time) VALUES (NULL, ?, ?, ?);");
			prep.setLong(1, in.getWreckId());
			prep.setString(2, fileName);
			prep.setLong(3, in.getTime());
			prep.execute();
		} catch (SQLException e) {
			logger_.log(Level.SEVERE, "Got SQLException when inserting into WreckImages table :" + e.getMessage());
			return ihn;
		}
		
		// Save the image from the POST.
		File imageFile = new File(ImageHandler.IMAGE_DIRECTORY, fileName);
		File miniFile = new File(IMAGE_DIRECTORY, MINI_PREFIX + fileName);
		try {
			FileOutputStream writer = new FileOutputStream(imageFile);
			// TODO - why -1
			writer.write(in.getBytes(), 0, in.getBytes().length - 1);
			writer.close();
			
			// Scale the image down and save it
			BufferedImage img = ImageManipulator.scaleDown(imageFile);
			ImageManipulator.saveImage(img, miniFile);
			
		} catch (IOException excp) {
			logger_.log(Level.SEVERE, "Got an IOException: " + excp.getMessage());
			return ihn;
		}
		
		//create and return a ImageHandledNotification showing the reques was handled.
		ihn = new ImageHandledNotification();
		ihn.setTime(new Date(in.getTime()));
		ihn.setWreckId(in.getWreckId());
		ihn.setFileName(fileName);
		return ihn;
	}
	
	
	public void setDataConnection(DataSource ds){
		ds_ = ds;
	}
	
	public DataSource getDataConnection(){
		return ds_;
	}
	
	public ImageParser getParser() {
		return parser_;
	}
	
	public void setParser(ImageParser p) {
		parser_ = p;
	}

	public static String getIMAGE_DIRECTORY() {
		return IMAGE_DIRECTORY;
	}
 
	private synchronized void getInitialFileNamePrefix(Connection dbConn)
	{
		//check to see if possibly someone set FILE_NAME_PREFIX while
		//we were waiting on the lock.
		if (ImageHandler.FILE_NAME_PREFIX == -1)
		{
            try {
            		ImageHandler.FILE_NAME_PREFIX = 0;
                    // Prepare the SQL statement
                    PreparedStatement prep = null; 
                    prep = dbConn.prepareStatement("SELECT max(imageId) AS imageId FROM WreckImages;");
                    ResultSet rs = prep.executeQuery();
                    if (rs.next())
                    {
                        ImageHandler.FILE_NAME_PREFIX = rs.getInt("imageId");
                    }
                	rs.close();
            } catch (SQLException e) {
                    logger_.log(Level.SEVERE, "Got SQLException when getting imageId :" + e.getMessage());
            }
		}
	}

}
