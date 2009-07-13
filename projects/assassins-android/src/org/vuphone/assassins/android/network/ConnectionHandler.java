package org.vuphone.assassins.android.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;

import android.util.Log;

/**
 * Class responsible for communication between devices. Once a socket connection
 * is established, an object of this class is instantiated with that socket to
 * handle the communication.
 * 
 * @author Krzysztof Zienkiewicz
 * 
 */
public class ConnectionHandler extends Thread {

	private static final String THREAD_NAME = "ConnectionHandler";
	private final Socket socket_;
	private InputStream iStream_;
	private OutputStream oStream_;
	private ArrayList<GameMessage> messageQueue_;
	private boolean shouldRun_ = true;
	
	private static final int BUFFER_CAPACITY = 200;
	
	private byte[] buffer_ = new byte[BUFFER_CAPACITY];
	private int bufferOffset_;

	public ConnectionHandler(Socket s) {
		super(THREAD_NAME + ":" + s.getInetAddress().getHostAddress());
		socket_ = s;
		messageQueue_ = new ArrayList<GameMessage>(3);
		
		try {
			iStream_ = socket_.getInputStream();
			oStream_ = socket_.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Log.v("Assassins", "ConnectionHandler started: "
				+ socket_.getRemoteSocketAddress());
		// socket_.getInetAddress());
	}

	private boolean isSocketAlive() {
		Log
				.v("Assassins", "isSocketAlive: "
						+ socket_.getRemoteSocketAddress());
		return socket_.isConnected();

	}

	private void performInput() {
		// Read all the data from the stream into the buffer. 
		try {
			while (iStream_.available() > 0) {
				int len = iStream_.available();
				iStream_.read(buffer_, bufferOffset_, len);
				bufferOffset_ += len;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// TODO - See if there is a more efficient algorithm
		// Scan the buffer and pick off the messages
		// Look for GameMessage.START
		for (int i = 0; i < bufferOffset_ - GameMessage.START.length; i++) {
			for (int j = 0; j < GameMessage.START.length; j++) {
				if (buffer_[i + j] != GameMessage.START[j]) {
					break;
				}
			}
			// If we're here then START is present at i.
			// See if we can get the length of the message. (+3 because of int)
			if (i + GameMessage.START.length + 3 >= bufferOffset_) {
				// Length info isn't in yet so quit.
				return;
			}
			
			int len = Array.getInt(buffer_, i + GameMessage.START.length + 3);
			// See if we can get the entire message
			if (i + GameMessage.START.length + 3 + len >= bufferOffset_)
				return;
			
			byte[] data = new byte[len];
			System.arraycopy(buffer_, i + GameMessage.START.length + 4, data, 0, len);
			
			
			// Update i to skip everything we read and mark the shift
			// TODO - FINISH THIS.
		}
	}

	private void performOutput() {
		// Check if we have messages queued up and send them through.
		GameMessage[] msgArray;
		synchronized (messageQueue_) {
			if (messageQueue_.isEmpty())
				return;

			// We don't want to send in a synchronized block so just retrieve
			msgArray = new GameMessage[messageQueue_.size()];
			for (int i = 0; i < messageQueue_.size(); i++) {
				msgArray[i] = messageQueue_.remove(i);
			}
		}

		// If we're here then we have data to send;
		try {
			Log.v("Assassins", "Sending " + msgArray.length + " messages.");
			for (int i = 0; i < msgArray.length; i++) {
				oStream_.write(msgArray[i].getData());
			}
			oStream_.flush();
		} catch (IOException e) {
			Log.v("Assassins", "IOException while sending message");
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (shouldRun_) {

			if (!isSocketAlive()) {
				Log.v("Assassins", "Socket disconnected. Killing Thread");
				shouldRun_ = false;
				break;
			}

			performInput();
			performOutput();
		}
	}

	public void sendMessage(GameMessage msg) {
		synchronized (messageQueue_) {
			messageQueue_.add(msg);
		}
	}

}
