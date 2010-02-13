
package org.vuphone.vandyupon.notification.diningrating;

import java.util.ArrayList;

import org.vuphone.vandyupon.datastructs.Rating;

public class DiningPostRating extends Rating {

	private ArrayList<DiningPostRatingContainer> ratings_;
	private long totalUp_ = 0;
	private long totalDown_ = 0;
	private double avg_ = 0;
	private boolean finalizeCalled_ = false;

	public DiningPostRating() {
		super("event");
		ratings_ = new ArrayList<DiningPostRatingContainer>();
	}

	public void addRating(long user, int value, String comment, long submissionDate){
		ratings_.add(new DiningPostRatingContainer(user, value, comment, submissionDate));
	}

	public void finalize(){
		if (!finalizeCalled_){
			for (DiningPostRatingContainer erc:ratings_){
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
