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
package org.vuphone.vandyupon.notification.ratingrequest.event;

import java.util.ArrayList;

import org.vuphone.vandyupon.datastructs.Rating;

public class EventRating extends Rating {

	private ArrayList<EventRatingContainer> ratings_;
	private long totalUp_ = 0;
	private long totalDown_ = 0;
	private double avg_ = 0;
	private boolean finalizeCalled_ = false;

	public EventRating() {
		super("event");
		ratings_ = new ArrayList<EventRatingContainer>();
	}

	public void addRating(long user, int value, String comment, long submissionDate){
		ratings_.add(new EventRatingContainer(user, value, comment, submissionDate));
	}

	public void finalize(){
		if (!finalizeCalled_){
			for (EventRatingContainer erc:ratings_){
				if (erc.getValue() == 1)
					++totalUp_;
				else
					++totalDown_;
			}
			avg_ = (totalUp_ + totalDown_)/(double)ratings_.size();
		}
	}
	
	public boolean finalizeCalled(){
		return finalizeCalled_;
	}
	
	public double getAverage(){
		return avg_;
	}
	
	public long getUp(){
		return totalUp_;
	}
	
	public long getDown(){
		return totalDown_;
	}

	@Override
	public Object getRating() {
		return ratings_;
	}

}
