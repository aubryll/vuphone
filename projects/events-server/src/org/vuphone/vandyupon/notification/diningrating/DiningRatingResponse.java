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

import org.vuphone.vandyupon.notification.ResponseNotification;

/**
 * This type is intended to encapsulate the return status
 * of a rating event. Currently, it has only a boolean that
 * says if the rating succeeded or failed. For the future, it
 * can be expanded to encapsulate any relevant data to be returned. 
 * @see DiningRatingResponseHandler
 * @see ResponseNotification
 * @author Esubalew T. Bekele
 */

public class DiningRatingResponse extends ResponseNotification {
	// It inherits from ResponseNotification to confirm to the
	// Notification inheritance hierarchy and to be suitable for 
	// DiningRatingResponseHandler	
	 
	private boolean return_status_; /*<- return status for success or failure*/
	
	public DiningRatingResponse(String responseType, String callback){
		super("diningrating", responseType, callback);
	}
	
	public boolean getReturnStatus(){
		return return_status_;
	}
	
	public void setReturnStatus(boolean new_status){
		return_status_ = new_status;
	}	
}
