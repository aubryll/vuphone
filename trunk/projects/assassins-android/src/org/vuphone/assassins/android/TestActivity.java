package org.vuphone.assassins.android;

import java.io.IOException;
import java.net.ServerSocket;

import org.vuphone.assassins.android.network.ConnectionHandler;
import org.vuphone.assassins.android.network.GameMessage;

import android.app.Activity;
import android.util.Log;

public class TestActivity extends Activity {
	
	@Override
	public void onStart() {
		super.onStart();
		try {
			ServerSocket server = new ServerSocket(8080);
			Log.v("Assassins", "Server started at: " + server.getInetAddress());
			ConnectionHandler c = new ConnectionHandler(server.accept());
			c.start();
			
			c.sendMessage(new GameMessage());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
