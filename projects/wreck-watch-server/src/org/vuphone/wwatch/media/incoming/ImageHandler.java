package org.vuphone.wwatch.media.incoming;

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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletInputStream;

import org.vuphone.wwatch.notification.Notification;
import org.vuphone.wwatch.notification.NotificationHandler;

public class ImageHandler implements NotificationHandler {

	private static final Logger logger_ = Logger.getLogger(ImageHandler.class.getName());
	private DataSource ds_;
	private ImageParser parser_;
	private static final String TIME = "time";
	private static final String WRECKID = "wreckid";
	private static final String CONTENT_TYPE = "image/jpeg";
	private static final String IMAGE_DIRECTORY = "images";
	private static final String FILE_EXTENSION = ".jpg";
	private static int FILE_NAME_PREFIX = -1;
	
	public Notification handle(Notification n) {

		Connection db = null;

		try {
			db = ds_.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			logger_.log(Level.SEVERE,
					"Unable to initialize org.sqlite.JDBC");
			logger_.log(Level.SEVERE,
					" Unable to continue without a database, stopping");
			return n;
		}
		
		//get the initial value to use from the database from the imageId column
		//but only get the first time.
		if (ImageHandler.FILE_NAME_PREFIX == -1)
		{
			getInitialFileNamePrefix(db);
		}
		
		//get data from request
		HttpServletRequest request = n.getRequest();
		if (!isRequestValid(request))
		{
			logger_.log(Level.SEVERE, "Request didn't have needed parameters");
			return n;
		}
		
		long time, wreckId;
		time = Long.parseLong(request.getParameter(ImageHandler.TIME));
		wreckId = Long.parseLong(request.getParameter(ImageHandler.WRECKID));
						
		String fileName;
		synchronized (ImageHandler.class)
		{
			ImageHandler.FILE_NAME_PREFIX++;
			fileName = Integer.toString(ImageHandler.FILE_NAME_PREFIX) + ImageHandler.FILE_EXTENSION;
		}

		//put other data from request into database
		// Insert wreck into database
		try {
			// Prepare the SQL statement
			PreparedStatement prep = null; 
			prep = db
					.prepareStatement("INSERT INTO WreckImages (ImageId, WreckID, FileName, Time) VALUES (NULL, ?, ?, ?);");
			prep.setLong(1, wreckId);
			prep.setString(2, fileName);
			prep.setDate(3, new Date(time));
			prep.execute();
		} catch (SQLException e) {
			logger_.log(Level.SEVERE, "Got SQLException when inserting into WreckImages table :" + e.getMessage());
			return n;
		}
		
		//put image as file on disk
		File imageFile = new File(ImageHandler.IMAGE_DIRECTORY, fileName);
		try {
			FileOutputStream writer = new FileOutputStream(imageFile);
			ServletInputStream input = request.getInputStream();
			byte[] bytes = new byte[1024];
			int read = -1;
			while ((read = input.read (bytes)) > 0)
			{
				writer.write(bytes, 0, read);
			}
			writer.close();
		} catch (IOException excp) {
			logger_.log(Level.SEVERE, "Got an IOException: " + excp.getMessage());
			return n;
		}
		
		// Parse the Notification and extract the AccidentReport
//		ImageHandledNotificaiton report;
//		try {
//			report = parser_.getAccident(n);
//		} catch (AccidentFormatException e1) {
//			e1.printStackTrace();
//			logger_.log(Level.SEVERE,
//					"Unable to parse the Notification:");
//			logger_.log(Level.SEVERE, " Notification was: ");
//			logger_.log(Level.SEVERE, " " + n.toString());
//			logger_.log(Level.SEVERE,
//					" Unable to continue without AccidentReport, stopping");
//			return n;
//		}
//		
		
		return n;
	}
	
	public boolean isRequestValid(HttpServletRequest request)
	{
		boolean isValid = false;

		if (request.getParameter(ImageHandler.TIME) == null)
		{
			logger_.log(Level.SEVERE, "Unable to get time from the request");
		}
		else if (request.getContentType() == null ||
				!request.getContentType().equalsIgnoreCase(ImageHandler.CONTENT_TYPE))
		{
			logger_.log(Level.SEVERE, "Expected Content Type to be " + ImageHandler.CONTENT_TYPE + " not " + request.getContentType());
		}
		else if (request.getParameter(ImageHandler.WRECKID) == null)
		{
			logger_.log(Level.SEVERE, "Unable to get wreckid from the request");
		}
		else
		{
			isValid = true;
		}
		return isValid;
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
