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
package org.vuphone.wwatch.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This class is responsible for constructing the WreckWatch database if it does not
 * exits.  If it does exist, no changes will be made.
 * @author Chris Thompson
 *
 */

public class SqlConstructor {

	/**
	 * Static method responsible for creating the database, building the tables
	 * and establishing the relationships.  After this method is called, the database
	 * will be fully operational.  This method should be called at application startup.
	 * 
	 * It could, in theory, be called before every query although that would result in
	 * excessive overhead that is completely unnecessary.
	 * @throws SQLException
	 */
	public static void prepareDatabase() throws SQLException{
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
		Connection db = DriverManager.getConnection("jdbc:sqlite:wreckwatch.db");

		db.setAutoCommit(false);
		String sql = 

			"create table if not exists People(id integer primary key asc autoincrement, PhoneNumber varchar(10) not null, " +
			"FirstName varchar(50), LastName varchar(50), Email varchar(100) not null);" +

			"create table if not exists Wreck(WreckID integer primary key asc autoincrement, Person integer references People(id), Lat double not null," +
			" Lon double not null, Time date not null, LargestAccel double not null);" +

			"create table if not exists EmergencyContacts(PersonId integer referecnes People(id), ContactId integer references People(id);" +

			"create table if not exists Route(CoordID int primary key asc autoincrement, WreckID integer references Wreck(WreckID), Lat double not null," +
			"Lon double not null, Time date not null);";
		PreparedStatement prep = db.prepareStatement(sql);

		prep.execute();
		db.commit();

	}

}
