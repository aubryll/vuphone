package org.vuphone.vandyupon.media.incoming.event;

import java.io.File;
import java.text.DateFormat;

import org.vuphone.vandyupon.notification.ResponseNotification;

public class ImageHandledNotification extends ResponseNotification {

	private long eventId_;
	private String fileName_;
	private long time_;
	
	public ImageHandledNotification(String response, String callback) {
		super("eventimagepost", response, callback);
	}
	
	public long getEventId()
	{
		return eventId_;
	}
	
	public void setEventId(long id)
	{
		eventId_ = id;
	}
	
	public String getFileName()
	{
		return fileName_;
	}
	
	public void setFileName(String name)
	{
		fileName_ = name;
	}
	
	public long getTime()
	{
		return time_;
	}
	
	public void setTime(long time)
	{
		time_ = time;
	}
	
	@Override
	public String getResponseString() {
		StringBuilder sb = new StringBuilder();
		sb.append("An image was uploaded to ");
		sb.append(ImageHandler.IMAGE_DIRECTORY);
		sb.append(File.separatorChar);
		sb.append(fileName_);
		sb.append(" for event #");
		sb.append(eventId_);
		sb.append(" at ");
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
		sb.append(df.format(time_));
		return sb.toString();
	}
}
