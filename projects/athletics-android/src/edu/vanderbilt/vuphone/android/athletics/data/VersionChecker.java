package edu.vanderbilt.vuphone.android.athletics.data;


import java.net.*;
import java.io.*;
// TODO: work on this class, make it robust, and comment it
public class VersionChecker {

	public static boolean isDataUpToDate () throws Exception { // not thoroughly tested
		URL XmlVersion = new URL("http://people.vanderbilt.edu/~zach.mccormick/version.xml");
		URLConnection VersionConnection = XmlVersion.openConnection();
		FileInputStream FileReader = new FileInputStream("version.vu");
		BufferedReader XmlOnline = new BufferedReader(new InputStreamReader(VersionConnection.getInputStream()));
		BufferedReader XmlOffline = new BufferedReader(new InputStreamReader(FileReader));
		if (XmlOnline.readLine().equalsIgnoreCase(XmlOffline.readLine()) ) {
			return true;
		} else {
			return false;
		}
		
	}
	public static void getNewData(String input) throws Exception { // untested
		URL XmlData = new URL("http://people.vanderbilt.edu/~zach.mccormick/" + input + ".xml");
		URLConnection VersionConnection = XmlData.openConnection();
		BufferedReader XmlOnline = new BufferedReader(new InputStreamReader(VersionConnection.getInputStream()));

		FileOutputStream output = new FileOutputStream(input + ".xml");
		PrintStream printer = new PrintStream(output);
		while (true) {
			String NextLine = new String(XmlOnline.readLine());
			if(NextLine.equalsIgnoreCase("</xml>")) {
				printer.println(NextLine);
				break;
			} else {
				printer.println(NextLine);
			}
		}
		
	}

	
}
