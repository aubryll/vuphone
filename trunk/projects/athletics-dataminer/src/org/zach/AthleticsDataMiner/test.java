package org.zach.AthleticsDataMiner;

public class test {

	public static void main(String args[]) {

		String title = "VU Baseball at Georgia";
		System.out.println(getHomeTeam(title));
		System.out.println(getAwayTeam(title));
		System.out.println(getSport(title));

	}

	private static String getHomeTeam(String title) {
		String[] array = title.split("[A-Za-z0-9]* vs\\. ", 2);
		if (array.length == 2) {
			return "Vanderbilt";
		} else {
			array = title.split("[A-Za-z0-9]* at? ", 2);
			if (array.length == 2) {
				return array[1].trim();
			} else {
				return "error";
			}
		}

	}

	private static String getAwayTeam(String title) {
		String[] array = title.split("[A-Za-z0-9]* vs\\. ", 2);
		if (array.length == 2) {
			return array[1].trim();
		} else {
			array = title.split("[A-Za-z0-9]* at? ", 2);
			if (array.length == 2) {
				return "Vanderbilt";
			} else {
				return "error";
			}
		}
	}

	private static String getSport(String title) {
		String[] array = title.split("[A-Za-z0-9]*(vs\\.|at)", 2);
		if (array.length == 2) {
			return array[0].substring(3).trim();
		} else {
			return "error";
		}

	}
}
