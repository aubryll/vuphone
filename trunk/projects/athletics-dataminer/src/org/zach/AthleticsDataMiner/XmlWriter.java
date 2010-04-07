package org.zach.AthleticsDataMiner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class XmlWriter {

	public final String TAG_DATA = "data";
	public final String TAG_NEWS = "news";
	public final String TAG_GAME = "game";
	public final String TAG_TEAM = "team";

	File dataFile;

	XmlWriter(String filename) {
		dataFile = new File(filename);
	}

	public boolean startDataFile() {
		try {
			// easy way to clear the file
			dataFile.delete();
			dataFile.createNewFile();

			BufferedWriter printer = new BufferedWriter(new FileWriter(
					dataFile, true));
			printer.write("<?xml version=\"1.0\"?>");
			printer.newLine();
			printer.write("<" + TAG_DATA + ">");
			printer.newLine();
			printer.close();
			return true;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	public boolean endDataFile() {
		try {
			BufferedWriter printer = new BufferedWriter(new FileWriter(
					dataFile, true));
			printer.write("</" + TAG_DATA + ">");
			printer.newLine();
			printer.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Writes a news item to the XML file being generated
	 * 
	 * @param title
	 * @param body
	 * @param link
	 * @return
	 */
	public boolean writeNews(String title, String body, String link, String date) {
		BufferedWriter printer = null;
		try {
			printer = new BufferedWriter(new FileWriter(dataFile, true));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		try {
			printer.write("<" + TAG_NEWS + " ");
			printer.write("title=\"");
			printer.write(title);
			printer.write("\" body=\"");
			printer.write(body);
			printer.write("\" link=\"");
			printer.write(link);
			printer.write("\" date=\"");
			printer.write(date);
			printer.write("\" />");
			printer.newLine();
			printer.close();
			return true;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * Writes a game to the XML file being generated.
	 * 
	 * @param hometeam
	 * @param awayteam
	 * @param sport
	 * @param type
	 * @param time
	 * @param homescore
	 * @param awayscore
	 * @return true if successful, false if exception occurs
	 */
	public boolean writeGame(String hometeam, String awayteam, String sport,
			String type, String time, String homescore, String awayscore) {
		BufferedWriter printer = null;
		try {
			printer = new BufferedWriter(new FileWriter(dataFile, true));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		try {
			printer.write("<" + TAG_GAME + " ");
			printer.write("hometeam=\"");
			printer.write(hometeam);
			printer.write("\" awayteam=\"");
			printer.write(awayteam);
			printer.write("\" sport=\"");
			printer.write(sport);
			printer.write("\" type=\"");
			printer.write(type);
			printer.write("\" time=\"");
			printer.write(time);
			printer.write("\" homescore=\"");
			printer.write(homescore);
			printer.write("\" awayscore=\"");
			printer.write(awayscore);
			printer.write("\" />");
			printer.newLine();
			printer.close();
			return true;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * Writes a team to the XML file being generated.
	 * 
	 * @param school
	 * @param name
	 * @param location
	 * @param conference
	 * @param rank
	 * @return true if successful, false if exception occurs
	 */
	public boolean writeTeam(String school, String name, String location,
			String conference, int rank) {
		BufferedWriter printer = null;
		try {
			printer = new BufferedWriter(new FileWriter(dataFile, true));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		try {
			printer.write("<" + TAG_TEAM + " ");
			printer.write("school=\"");
			printer.write(school);
			printer.write("\" name=\"");
			printer.write(name);
			printer.write("\" location=\"");
			printer.write(location);
			printer.write("\" conference=\"");
			printer.write(conference);
			printer.write("\" rank=\"");
			printer.write(rank);
			printer.write("\" />");
			printer.newLine();
			printer.close();
			return true;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}
}
