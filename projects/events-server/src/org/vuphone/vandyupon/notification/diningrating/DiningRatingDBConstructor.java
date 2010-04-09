 /**************************************************************************
 * @author Bailin Gao                                     
 * @date 4/8/10
 * 
 * @section License                                                        *
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
 * limitations under the License.
 * 
 * @section Description
 * @class DiningRatingDBConstructor
 * @see DatabaseConstructor
 * @brief Creates the initial diningrating database for use					*
 **************************************************************************/
package org.vuphone.vandyupon.notification.diningrating;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.vuphone.vandyupon.sql.DatabaseConstructor;

import javax.sql.DataSource;

public class DiningRatingDBConstructor implements DatabaseConstructor {

	/**The constructor called to create the Database
	 * @param ds The datasource that we're connected to, a MySQL database
	 * 
	 * @throws SQLException Something wrong with the generated SQL statement
	 */
	public void construct(DataSource ds) throws SQLException {
		Connection db = ds.getConnection();
		db.setAutoCommit(false);

		String sql = "CREATE TABLE IF NOT EXISTS DiningRatings ( "
				+ "deviceid varchar(16) NOT NULL,"
				+ "loc INT NOT NULL,"
				+ "rating INT NOT NULL,"
				+ "CONSTRAINT deviceLoc PRIMARY KEY (deviceid, loc))"
				+ "ENGINE=InnoDB DEFAULT CHARSET=utf8";
		PreparedStatement prep = db.prepareStatement(sql);
		prep.execute();

		db.commit();	
		db.setAutoCommit(true);
		db.close();
	}

}