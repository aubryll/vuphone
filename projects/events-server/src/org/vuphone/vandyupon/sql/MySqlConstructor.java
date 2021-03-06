/**************************************************************************
 * Copyright 2009 Chris Thompson                                           *
 *                                                                         *
 * Licensed under the Apache License, Version 2.0 (the "License");         *
 * you may not use this file except in compliance with the License.        *
 * You may obtain a copy of the License at                                 *
 *                                                                         *
 * http://www.apache.org/licenses/LICENSE-2.0                              *
 *                                                                         *
 * Unless required by applicable law or agreed to in writing, software     *
 * distributed under the License is distributed on an "AS IS" BASIS,       *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.*
 * See the License for the specific language governing permissions and     *
 * limitations under the License.                                          *
 **************************************************************************/
package org.vuphone.vandyupon.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

public class MySqlConstructor implements DatabaseConstructor {

	public void construct(DataSource ds) throws SQLException {
		Connection db = ds.getConnection();
		db.setAutoCommit(false);

		String sql = "CREATE TABLE IF NOT EXISTS people ( "
				+ "userid INTEGER PRIMARY KEY AUTO_INCREMENT,"
				+ "deviceid VARCHAR(20) unique)"
				+ "ENGINE=InnoDB DEFAULT CHARSET=utf8";
		PreparedStatement prep = db.prepareStatement(sql);
		prep.execute();

		sql = "CREATE TABLE IF NOT EXISTS locations ( "
				+ "locationid INTEGER PRIMARY KEY AUTO_INCREMENT,"
				+ "name varchar(200) NOT NULL DEFAULT 'No Location'," 
				+ "Lat DOUBLE," 
				+ "Lon DOUBLE,"
				+ "Date BIGINT NOT NULL,"
				+ "userid int not null references people(userid),"
				+ "lastupdate bigint not null)"
				+ "ENGINE=InnoDB DEFAULT CHARSET=utf8";
		prep = db.prepareStatement(sql);
		prep.execute();

		sql = "CREATE TABLE IF NOT EXISTS events ( "
				+ "eventid INTEGER PRIMARY KEY AUTO_INCREMENT,"
				+ "sourceuid varchar(255) UNIQUE,"
				+ "name varchar(100) not null,"
				+ "locationid integer not null references locations(locationid),"
				+ "userid integer not null references people(userid),"
				+ "starttime bigint not null," + "endtime bigint not null,"
				+ "lastupdate bigint not null"
				+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8";
		prep = db.prepareStatement(sql);
		prep.execute();
		/*
		 * Isn't working for some reason. Gives MySQL errno 150. sql =
		 * "ALTER TABLE events ADD FOREIGN KEY (locationid) " +
		 * "REFERENCES locations (locationid) ON DELETE SET NULL"; prep =
		 * db.prepareStatement(sql); prep.execute();
		 */
		sql = "CREATE TABLE IF NOT EXISTS eventrating ( "
				+ "ratingid INTEGER PRIMARY KEY AUTO_INCREMENT,"
				+ "value tinyint not null,"
				+ "eventid INTEGER REFERENCES events(eventid), "
				+ "comment text,"
				+ "userid integer not null references people (userid),"
				+ "submissiondate BIGINT NOT NULL, "
				+ "FOREIGN KEY events_eventid (eventid) REFERENCES events (eventid) ON DELETE CASCADE) "
				+ "ENGINE=InnoDB DEFAULT CHARSET=utf8";
		prep = db.prepareStatement(sql);
		prep.execute();

		sql = "CREATE TABLE IF NOT EXISTS locationrating ( "
				+ "ratingid INTEGER PRIMARY KEY AUTO_INCREMENT,"
				+ "value tinyint, "
				+ "locationid integer not null references locations(locationid),"
				+ "userid integer not null references people(userid),"
				+ "comment text, " + "submissiondate bigint not null)"
				+ "ENGINE=InnoDB DEFAULT CHARSET=utf8";
		prep = db.prepareStatement(sql);
		prep.execute();

		/*
		 * Also not working sql =
		 * "ALTER TABLE locationrating ADD FOREIGN KEY (locationid) " +
		 * "REFERENCES locations (locationid) ON DELETE SET NULL"; prep =
		 * db.prepareStatement(sql); prep.execute();
		 */
		sql = "create table if not exists facebookconnector ("
				+ "facebookid integer primary key auto_increment,"
				+ "userid integer not null, " + "login varchar(100) not null,"
				+ "password varchar(100) not null)"
				+ "ENGINE=InnoDB DEFAULT CHARSET=utf8";

		prep = db.prepareStatement(sql);
		prep.execute();

		sql = "create table if not exists metatypes ("
				+ "typeid integer not null primary key auto_increment,"
				+ "typename varchar(100) not null)"
				+ "ENGINE=InnoDB DEFAULT CHARSET=utf8";
		prep = db.prepareStatement(sql);
		prep.execute();

		sql = "create table if not exists eventmeta ("
				+ "metaid integer not null primary key auto_increment,"
				+ "eventid integer not null references events(eventid),"
				+ "value text not null,"
				+ "metatype integer not null,"
				+ "submissiontime bigint not null, "
				+ "FOREIGN KEY events_eventid (eventid) REFERENCES events (eventid) ON DELETE CASCADE, "
				+ "FOREIGN KEY meta_metatype (metatype) REFERENCES metatypes (typeid) ) "
				+ "ENGINE=InnoDB DEFAULT CHARSET=utf8";
		prep = db.prepareStatement(sql);
		prep.execute();

		sql = "create table if not exists locationmeta ("
				+ "metaid integer not null primary key auto_increment,"
				+ "locationid integer not null references locations(locationid),"
				+ "value text not null,"
				+ "metatype integer references metatypes(typeid),"
				+ "submissiontime bigint not null,"
				+ " FOREIGN KEY locations_locationid (locationid) "
				+ "REFERENCES locations (locationid) ON DELETE CASCADE) "
				+ "ENGINE=InnoDB DEFAULT CHARSET=utf8";
		prep = db.prepareStatement(sql);
		prep.execute();

		db.commit();

		db.setAutoCommit(true);
		sql = "select count(*) from metatypes where typename like 'DESCRIPTION'";
		prep = db.prepareStatement(sql);
		ResultSet rs = prep.executeQuery();
		rs.next();
		if (rs.getInt(1) == 0) {
			sql = "insert into metatypes(typename) values ('DESCRIPTION'), ('IMAGE'), "
					+ "('HOSTING_ORG'), ('LOCATION_MODIFIER'), ('TAG')";
			prep = db.prepareStatement(sql);
			prep.execute();
		}

		db.close();
	}
}