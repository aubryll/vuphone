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

public class MySqlConstructor implements DatabaseConstructor {

	public void construct(DataSource ds) throws SQLException {
		Connection db = ds.getConnection();
		db.setAutoCommit(false);

		String sql = "CREATE TABLE IF NOT EXISTS people ( "
				+ "id INTEGER PRIMARY KEY AUTO_INCREMENT,"
				+ "AndroidID VARCHAR(20)," + "PhoneNumber VARCHAR(15),"
				+ "FirstName VARCHAR(50)," + "LastName VARCHAR(50),"
				+ "Email VARCHAR(100));";
		PreparedStatement prep = db.prepareStatement(sql);
		prep.execute();

		sql = "CREATE TABLE IF NOT EXISTS wreck ( "
				+ "WreckID INTEGER PRIMARY KEY AUTO_INCREMENT,"
				+ "Person INTEGER REFERENCES people(id),"
				+ "Lat DOUBLE NOT NULL," + "Lon DOUBLE NOT NULL,"
				+ "Date BIGINT NOT NULL," + "LargestAccel DOUBLE NOT NULL);";
		prep = db.prepareStatement(sql);
		prep.execute();

		sql = "CREATE TABLE IF NOT EXISTS emergencycontacts ( "
				+ "PersonId INTEGER REFERENCES people(id),"
				+ "ContactId VARCHAR(15) NOT NULL);";
		prep = db.prepareStatement(sql);
		prep.execute();

		sql = "CREATE TABLE IF NOT EXISTS route ( "
				+ "CoordID INTEGER PRIMARY KEY AUTO_INCREMENT,"
				+ "WreckID INTEGER REFERENCES wreck(WreckID), "
				+ "Lat DOUBLE NOT NULL," + "Lon DOUBLE NOT NULL,"
				+ "Date BIGINT NOT NULL)" ;
		prep = db.prepareStatement(sql);
		prep.execute();

		sql = "CREATE TABLE IF NOT EXISTS wreckimages ( "
				+ "ImageID INTEGER PRIMARY KEY AUTO_INCREMENT,"
				+ "WreckID INTEGER REFERENCES wreck(WreckID), FileName TEXT NOT NULL,"
				+ "Time BIGINT NOT NULL)";
		prep = db.prepareStatement(sql);
		prep.execute();
		db.commit();

		db.close();
	}

}
