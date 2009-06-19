package org.vuphone.wwatch.media.incoming;

import org.vuphone.wwatch.notification.Notification;

import java.io.File;
import java.util.Date;
import java.text.DateFormat;

public class ImageHandledNotification extends Notification {

	private long wreckId;
	private String fileName;
	private Date time;
	
	public ImageHandledNotification() {
		super("imagehandled");
		// TODO Auto-generated constructor stub
	}
	
	public long getWreckId()
	{
		return wreckId;
	}
	
	public void setWreckId(long id)
	{
		wreckId = id;
	}
	
	public String getFileName()
	{
		return fileName;
	}
	
	public void setFileName(String name)
	{
		fileName = name;
	}
	
	public Date getTime()
	{
		return time;
	}
	
	public void setTime(Date time)
	{
		this.time = time;
	}
	
	@Override
	public String getResponseString() {
		StringBuilder sb = new StringBuilder();
		sb.append("An image was uploaded to ");
		sb.append(ImageHandler.IMAGE_DIRECTORY);
		sb.append(File.separatorChar);
		sb.append(fileName);
		sb.append(" for wreck #");
		sb.append(wreckId);
		sb.append(" at ");
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
		sb.append(df.format(time));
		return sb.toString();
	}
}
