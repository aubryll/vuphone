package org.zach.AthleticsDataMiner;

public class example {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		NewsParser newsParser = new NewsParser("data.xml");
		GameParser gameParser = new GameParser("data.xml");
		TeamParser teamParser = new TeamParser("data.xml");
		newsParser.startDataFile();
		newsParser.parseNews("http://vucommodores.cstv.com/headline-rss.xml");
		gameParser
				.parseGames("http://calendar.vanderbilt.edu/calendar/rss/set/200?xtags=athletics");
		teamParser.parseTeams("static.txt");
		newsParser.endDataFile();
	}
}
