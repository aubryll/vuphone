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
package org.vuphone.vandyupon.datastructs;


/**
 * This class is the abstract base class for all
 * rating objects.  It provides the base functionality
 * to allow for rating information to be submitted to
 * the server.  Subclass implementations will determine
 * how that rating is implemented by overriding the
 * getRating() method;
 * @author Chris Thompson
 *
 */
public abstract class Rating {
	
	/*
	 * The type of the rating.  Used to select the appropriate 
	 * handlers.
	 */
	private String type_;
	/*
	 * The unique identifier associated with the object being rated
	 * in the database.
	 */
	private long item_;
	
	/**
	 * Default constructor
	 * @param type - the type of the rating used to select
	 * 					appropriate handlers
	 */
	public Rating (String type){
		type_ = type;
	}
	
	/**
	 * Returns the id of the item this rating is for.
	 * @return long
	 */
	public long getItem(){
		return item_;
	}
	
	/**
	 * Method that returns a rating value for this rating object.
	 * The specific implementation of what a "rating value" is, depends
	 * on subclass implementation.
	 * @return Object
	 */
	public abstract Object getRating();
	
	/**
	 * Returns the type of the rating object.
	 * @return
	 */
	public String getType(){
		return type_;
	}
	
	/**
	 * Setter method to set the property keeping track of the association
	 * with database objects.
	 * @param item
	 */
	public void setItem(long item){
		item_ = item;
	}
	
}
