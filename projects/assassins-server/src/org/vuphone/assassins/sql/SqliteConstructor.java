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
package org.vuphone.assassins.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

public class SqliteConstructor implements DatabaseConstructor {

	public void construct(DataSource ds) throws SQLException {
		Connection db = ds.getConnection();
		db.setAutoCommit(false);

		String sql = "CREATE TABLE IF NOT EXISTS People ( "
				+ "id INTEGER PRIMARY KEY ,"
				+ "AndroidID VARCHAR(20)," + "PhoneNumber VARCHAR(15),"
				+ "FirstName VARCHAR(50)," + "LastName VARCHAR(50),"
				+ "Email VARCHAR(100));";
		PreparedStatement prep = db.prepareStatement(sql);
		prep.execute();
		
		sql = "CREATE TABLE IF NOT EXISTS LandMine ("
				+ "id INTEGER PRIMARY KEY, "
				+ "Lat DOUBLE NOT NULL," + "Lon DOUBLE NOT NULL,"
				+ "Radius DOUBLE NOT NULL);";
		prep = db.prepareStatement(sql);
		prep.execute();

		/*sql = "CREATE TABLE IF NOT EXISTS Wreck ( "
				+ "WreckID INTEGER PRIMARY KEY,"
				+ "Person INTEGER REFERENCES People(id),"
				+ "Lat DOUBLE NOT NULL," + "Lon DOUBLE NOT NULL,"
				+ "Date INT NOT NULL," + "LargestAccel DOUBLE NOT NULL);";
		prep = db.prepareStatement(sql);
		prep.execute();

		sql = "CREATE TABLE IF NOT EXISTS EmergencyContacts ( "
				+ "PersonId INTEGER REFERENCES People(id),"
				+ "ContactId VARCHAR(15) NOT NULL);";
		prep = db.prepareStatement(sql);
		prep.execute();

		sql = "CREATE TABLE IF NOT EXISTS Route ( "
				+ "CoordID INTEGER PRIMARY KEY,"
				+ "WreckID INTEGER REFERENCES Wreck(WreckID), "
				+ "Lat DOUBLE NOT NULL," + "Lon DOUBLE NOT NULL,"
				+ "Date INT NOT NULL)" ;
		prep = db.prepareStatement(sql);
		prep.execute();

		sql = "CREATE TABLE IF NOT EXISTS WreckImages ( "
				+ "ImageID INTEGER PRIMARY KEY,"
				+ "WreckID INTEGER REFERENCES Wreck(WreckID), FileName TEXT NOT NULL,"
				+ "Time DATE NOT NULL);";
		prep = db.prepareStatement(sql);
		prep.execute();*/
		
		
		db.commit();

		db.close();
		
	}

}
