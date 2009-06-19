package org.vuphone.wwatch.media.outgoing;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.vuphone.wwatch.notification.InvalidFormatException;
import org.vuphone.wwatch.notification.Notification;
import org.vuphone.wwatch.notification.NotificationHandler;

public class ImageRequestHandler implements NotificationHandler{
	
	// XML will instantiate these
	private ImageRequestParser parser_;
	
	private static final Logger logger_ = Logger
	.getLogger(ImageRequestHandler.class.getName());
	
	public Notification handle(Notification n) {

		ImageRequestNotification irn = null;
		try {
			irn = parser_.getImage(n.getRequest(), n.getResponse());
		} catch (InvalidFormatException ife) {
			
			ife.printStackTrace();
			logger_.log(Level.SEVERE,
					"Unable to parse the notification, stopping");
			return n;
		}
		
		/*try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
		try {
			ImageRequestNotification irn = (ImageRequestNotification)n;
			Connection db = null;

			try {
				db = DriverManager.getConnection("jdbc:sqlite:wreckwatch.db");
				db.setAutoCommit(true);
			} catch (SQLException e) {
				db.close();
				logger_.log(Level.SEVERE,
						"SQLException: ", e);
			}
			if (db != null){

				int id = 0;
				try {
					PreparedStatement prep = db.prepareStatement("select WreckID from Wreck where Lat like ? and Lon like ?;");
					prep.setDouble(1, irn.getLat());
					prep.setDouble(2, irn.getLon());

					ResultSet rs  = prep.executeQuery();

					rs.next();
					id = rs.getInt("WreckID");
					rs.close();

					prep = db.prepareStatement("select * from Images where WreckID like ?;");
					prep.setInt(1, id);
					
					rs = prep.executeQuery();
					
					while (rs.next()) {
						//TODO - extract either the images themselves or their
						//locations in the file system, depending on how they are 
						//stored in the database.
					}
					rs.close();
					
					//TODO - might have to add an extra step here to get the images
					//out of the file system.
				
				}catch (SQLException e) {
					logger_.log(Level.SEVERE,
							"SQLException: ", e);
					db.close();
					return null;
				}	
			}
		}catch (Exception sqle) {
			return null;
		}*/
		
		//TODO - change this from hard coded image to the one you found above.
		HttpServletResponse response = irn.getResponse();
		int count = 0;
		String header = "HeaderLength=?,";
		ByteArrayOutputStream toSend = new ByteArrayOutputStream();
		for (int i = 0; i < 5; i++) {
			File file = new File("C:\\ww_icon2.png");
			File file2 = new File("C:\\unhapppy.bmp");
			try {
				// This works assuming file only contains the binary data
				// for the image, no headers.
				int sz = (int) file.length();
				byte[] array = new byte[sz];
				int offset = 0;
				int numRead = 0;
				
				InputStream is = new FileInputStream(file);
				
				while (offset < sz && (numRead = is.read(array, offset, sz - offset)) >= 0){
					offset += numRead;
				}

				int sz2 = (int) file2.length();
				byte[] array2 = new byte[sz2];
				int offset2 = 0;
				int numRead2 = 0;
				
				InputStream is2 = new FileInputStream(file2);
				
				while (offset2 < sz2 && (numRead2 = is2.read(array2, offset2, sz2 - offset2)) >= 0){
					offset2 += numRead2;
				}
				
				response.addIntHeader("Image"+count+" length",array.length);
				header += "Image"+count+"length="+array.length+",";
				count++;
				response.addIntHeader("Image"+count+" length",array2.length);
				header += "Image"+count+"length="+array2.length+",";
				count++;

				
				toSend.write(array);
				toSend.write(array2);
			
			} catch (FileNotFoundException e) {

				e.printStackTrace();
				return null;
			} catch (IOException e) {

				e.printStackTrace();
				return null;
			}
		}
		response.addIntHeader("Number of Images", count);
		header += "NumImages="+count+",";
		
		int headerLength = header.length();
		// find order of magnitude of headerLength
		int temp = headerLength;
		int orderOfMag = 0;
		while (temp >= 1) {
			temp = temp / 10;
			orderOfMag++;
		}
		header = header.replace("?", ""+(headerLength+orderOfMag-1));
		
		try {
			response.getOutputStream().write(header.getBytes());
			response.getOutputStream().write(toSend.toByteArray());
		} catch (IOException e) {

			e.printStackTrace();
			return null;
		}
	
		return irn;
	}

	public ImageRequestParser getParser() {
		return parser_;
	}

	public void setParser(ImageRequestParser parser) {
		parser_ = parser;
	}

}
