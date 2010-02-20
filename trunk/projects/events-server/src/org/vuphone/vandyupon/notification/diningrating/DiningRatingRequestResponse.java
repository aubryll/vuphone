/**************************************************************************
 * Copyright 2010 Esubalew T. Bekele                                           *
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

package org.vuphone.vandyupon.notification.diningrating;

/**
 * This response type holds the rating and the return status 
 * for a dining rating request for a particular location. For 
 * the future, it can be expanded to encapsulate any relevant
 * data to be returned.
 * @see DiningRatingRequestResponse
 * @see DiningRatingResponseHandler
 * @author Esubalew T. Bekele 
 */
public class DiningRatingRequestResponse extends DiningRatingResponse {
	// I let this class to inherit from DiningRatingResponse
	// because both have a return status boolean
	private long rating_; /*<- the retrieved rating from database */
	
	public DiningRatingRequestResponse(String responseType,String callback,long rating,boolean status){
		super(responseType,callback,status);
		rating_ = rating;
	}
	
	public long getRating(){
		return rating_;
	}
	
	public void setRating(long new_rating){
		rating_ = new_rating;
	}
}
