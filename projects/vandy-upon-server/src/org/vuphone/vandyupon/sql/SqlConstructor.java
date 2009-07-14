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

	private DataSource ds_;
	private DatabaseConstructor dc_;
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
	public void prepareDatabase() throws SQLException {
		dc_.construct(ds_);
	}

	public void setDataConnection(DataSource ds){
		ds_ = ds;
	}
	
	public DataSource getDataConnection(){
		return ds_;
	}
	
	public void setDatabaseConstructor(DatabaseConstructor dc){
		dc_ = dc;
	}
	
	public DatabaseConstructor getDatabaseConstructor(){
		return dc_;
	}
}
