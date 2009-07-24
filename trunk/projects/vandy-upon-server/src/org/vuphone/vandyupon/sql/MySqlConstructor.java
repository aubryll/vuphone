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
				+ "deviceid VARCHAR(20) unique)";
		PreparedStatement prep = db.prepareStatement(sql);
		prep.execute();

		sql = "CREATE TABLE IF NOT EXISTS locations ( "
				+ "locationid INTEGER PRIMARY KEY AUTO_INCREMENT,"
				+ "name varchar(100) not null,"
				+ "Lat DOUBLE NOT NULL," + "Lon DOUBLE NOT NULL,"
				+ "Date BIGINT NOT NULL," + "userid int not null references people(userid)," +
						"lastupdate bigint not null)";
		prep = db.prepareStatement(sql);
		prep.execute();

		sql = "CREATE TABLE IF NOT EXISTS events ( "
				+ "eventid INTEGER PRIMARY KEY AUTO_INCREMENT,"
				+ "name varchar(100) not null,"
				+ "locationid integer not null references locations(locationid),"
				+ "userid integer not null references people(userid)," 
				+ "starttime bigint not null," 
				+ "endtime bigint not null," +
						"lastupdate bigint not null)";
		prep = db.prepareStatement(sql);
		prep.execute();

		sql = "CREATE TABLE IF NOT EXISTS eventrating ( "
				+ "ratingid INTEGER PRIMARY KEY AUTO_INCREMENT,"
				+ "value tinyint not null,"
				+ "eventid INTEGER REFERENCES events(eventid), "
				+ "comment text," 
				+ "userid integer not null references people (userid),"
				+ "submissiondate BIGINT NOT NULL)" ;
		prep = db.prepareStatement(sql);
		prep.execute();

		sql = "CREATE TABLE IF NOT EXISTS locationrating ( "
				+ "ratingid INTEGER PRIMARY KEY AUTO_INCREMENT,"
				+ "value tinyint, "
				+ "locationid integer not null references locations(locationid),"
				+ "userid integer not null references people(userid),"
				+ "comment text, "
				+ "submissiondate bigint not null)";
		prep = db.prepareStatement(sql);
		prep.execute();
		
		sql = "create table if not exists facebookconnector ("
			+ "facebookid integer primary key auto_increment,"
			+ "userid integer not null, "
			+ "login varchar(100) not null,"
			+ "password varchar(100) not null)";
		
		prep = db.prepareStatement(sql);
		prep.execute();
		
		sql = "create table if not exists metatypes ("
			+ "typeid integer not null primary key auto_increment,"
			+ "typename varchar(100) not null)";
		prep = db.prepareStatement(sql);
		prep.execute();
		
		sql = "create table if not exists eventmeta ("
			+ "metaid integer not null primary key auto_increment,"
			+ "eventid integer not null references events(eventid),"
			+ "value text not null,"
			+ "metatype integer references metatypes(typeid)," +
					"submissiontime bigint not null)";
		prep = db.prepareStatement(sql);
		prep.execute();
		
		sql = "create table if not exists locationmeta ("
			+ "metaid integer not null primary key auto_increment,"
			+ "locationid integer not null references locations(locationid),"
			+ "value text not null,"
			+ "metatype integer references metatypes(typeid)," +
					"submissiontime bigint not null)";
		prep = db.prepareStatement(sql);
		prep.execute();
		
		db.commit();
		
		db.setAutoCommit(true);
		sql = "select count(*) from metatypes where typename like 'DESCRIPTION'";
		prep = db.prepareStatement(sql);
		ResultSet rs = prep.executeQuery();
		rs.next();
		if (rs.getInt(1) == 0){
			sql = "insert into metatypes(typename) values ('DESCRIPTION'), ('IMAGE'), " +
			"('HOSTING_ORG'), ('LOCATION_MODIFIER')";
			prep = db.prepareStatement(sql);
			prep.execute();
		}
		
		db.close();
	}

}