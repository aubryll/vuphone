package org.vuphone.assassins.android.network;

public class GameMessage {

	public static byte[] START = {1, 2, 3, 5, 8, 13, 21, 34};
	
	public byte[] getData() {
		return new byte[] {'H', 'e', 'l', 'l', 'o', '.'};
	}
	
}
