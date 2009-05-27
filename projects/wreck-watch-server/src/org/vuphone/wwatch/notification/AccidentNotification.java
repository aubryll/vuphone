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
package org.vuphone.wwatch.notification;


/**
 * This class represents a container for information
 * related to an accident notification.
 * @author Chris Thompson
 *
 */
public class AccidentNotification extends Notification {
	
	private double speed_;
	private double dec_;
	private long time_;
	
	
	public AccidentNotification() {
		super("accident");
		
	}

	
	public double getSpeed() {
		return speed_;
	}


	public void setSpeed(double speed) {
		speed_ = speed;
	}


	public double getDeceleration() {
		return dec_;
	}


	public void setDeceleration(double dec) {
		dec_ = dec;
	}


	public long getTime() {
		return time_;
	}


	public void setTime(long time) {
		time_ = time;
	}
	
	

}
