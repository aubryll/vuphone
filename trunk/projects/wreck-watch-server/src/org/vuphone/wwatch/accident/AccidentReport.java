/******************************************************************************
 * Copyright (c) 2007 Jules White.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jules White - initial API and implementation 
 ****************************************************************************/
package org.vuphone.wwatch.accident;

import org.vuphone.wwatch.routing.Route;

/**
 * This class is a data structure used
 * to capture details relevant to an 
 * accident.
 * 
 * @author jules
 *
 */
public class AccidentReport {
	
	private double lat_;
	private double lon_;
	private long time_;
	private double speed_;
	private double acc_;
	private String person_;
	
	private Route route_;
	
	/**
	 * Default Ctor
	 */
	public AccidentReport(){
		
	}
	
	public AccidentReport(double lat, double lon, long time){
		lat_ = lat;
		lon_ = lon;
		time_ = time;
	}
	
	public static AccidentReport generateAccidentReport(AccidentNotification n){
		AccidentReport r = new AccidentReport();
		r.acc_ = n.getDeceleration();
		r.lat_ = n.getLatitude();
		r.lon_ = n.getLongitude();
		r.route_ = n.getRoute();
		r.time_ = n.getTime();
		
		return r;
	}
	
	public void setSpeed(double speed){
		speed_ = speed;
	}
	
	public void setAcceleration(double acc){
		acc_ = acc;
	}
	
	public long getTime(){
		return time_;
	}
	
	public void setParty(String person){
		person_ = person;
	}
	
	public double getSpeed(){
		return speed_;
	}
	
	public double getAcceleration(){
		return acc_;
	}
	
	public String getParty(){
		return person_;
	}
	
	public void setRoute(Route route){
		route_ = route;
	}
	
	public Route getRoute(){
		return route_;
	}
	
	public void setLatitude(double lat){
		lat_ = lat;
	}
	
	public void setLongitude(double lon){
		lon_ = lon;
	}
	
	public void setTime(long time){
		time_ = time;
	}
	
	public double getLatitude(){
		return lat_;
	}
	
	public double getLongitude(){
		return lon_;
	}

	
}
