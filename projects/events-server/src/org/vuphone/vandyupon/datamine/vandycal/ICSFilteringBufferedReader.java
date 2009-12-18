package org.vuphone.vandyupon.datamine.vandycal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class ICSFilteringBufferedReader extends BufferedReader {

	public ICSFilteringBufferedReader(Reader in) {
		super(in);
	}
	
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		int retval = super.read(cbuf, off, len);
		String str = new String(cbuf, off, len);
		if (str.indexOf("US-Central:") >= 0) {
			str = str.replaceFirst("/US-Central:/", "");
			str.getChars(0, str.length(), cbuf, off);
		}
		System.out.println("read called: " + str);
		
		return retval;
	}

	@Override
	public String readLine() throws IOException {
		String output = super.readLine();
		System.out.println("readLine called");
		if (output != null) {
			return output.replaceFirst("/US-Central:/", "");
		} else {
			return null;
		}
	}
}
