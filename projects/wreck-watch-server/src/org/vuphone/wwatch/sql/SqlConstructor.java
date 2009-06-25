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
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * This class is responsible for constructing the WreckWatch database if it does
 * not exits. If it does exist, no changes will be made.
 * 
 * @author Chris Thompson
 * 
 */

public class SqlConstructor {

	private static DataSource ds_;
	/**
	 * Static method responsible for creating the database, building the tables
	 * and establishing the relationships. After this method is called, the
	 * database will be fully operational. This method should be called at
	 * application startup.
	 * 
	 * It could, in theory, be called before every query although that would
	 * result in excessive overhead that is completely unnecessary.
	 * 
	 * @throws SQLException
	 */
	public static void prepareDatabase() throws SQLException {
		Connection db = ds_.getConnection();
		db.setAutoCommit(false);

		String sql = "CREATE TABLE IF NOT EXISTS People ( "
				+ "id INTEGER PRIMARY KEY AUTO_INCREMENT,"
				+ "AndroidID VARCHAR(20)," + "PhoneNumber VARCHAR(15),"
				+ "FirstName VARCHAR(50)," + "LastName VARCHAR(50),"
				+ "Email VARCHAR(100));";
		PreparedStatement prep = db.prepareStatement(sql);
		prep.execute();

		sql = "CREATE TABLE IF NOT EXISTS Wreck ( "
				+ "WreckID INTEGER PRIMARY KEY AUTO_INCREMENT,"
				+ "Person INTEGER REFERENCES People(id),"
				+ "Lat DOUBLE NOT NULL," + "Lon DOUBLE NOT NULL,"
				+ "Date DATE NOT NULL, "
				+ "Time TIME NOT NULL," + "LargestAccel DOUBLE NOT NULL);";
		prep = db.prepareStatement(sql);
		prep.execute();

		sql = "CREATE TABLE IF NOT EXISTS EmergencyContacts ( "
				+ "PersonId INTEGER REFERENCES People(id),"
				+ "ContactId VARCHAR(15) NOT NULL);";
		prep = db.prepareStatement(sql);
		prep.execute();

		sql = "CREATE TABLE IF NOT EXISTS Route ( "
				+ "CoordID INTEGER PRIMARY KEY AUTO_INCREMENT,"
				+ "WreckID INTEGER REFERENCES Wreck(WreckID), "
				+ "Lat DOUBLE NOT NULL," + "Lon DOUBLE NOT NULL,"
				+ "Date DATE NOT NULL, "
				+ "Time TIME NOT NULL)" ;
		prep = db.prepareStatement(sql);
		prep.execute();

		sql = "CREATE TABLE IF NOT EXISTS WreckImages ( "
				+ "ImageID INTEGER PRIMARY KEY AUTO_INCREMENT,"
				+ "WreckID INTEGER REFERENCES Wreck(WreckID), FileName TEXT NOT NULL,"
				+ "Time DATE NOT NULL);";
		prep = db.prepareStatement(sql);
		prep.execute();
		db.commit();

		db.close();
	}

	public void setDataConnection(DataSource ds){
		ds_ = ds;
	}
	
	public DataSource getDataConnection(){
		return ds_;
	}
}
