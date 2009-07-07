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
package org.vuphone.wwatch.android.mapview.pinoverlays;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.vuphone.wwatch.android.VUphone;
import org.vuphone.wwatch.android.Waypoint;
import org.vuphone.wwatch.android.mapview.AccidentActivity;
import org.vuphone.wwatch.android.mapview.Cache;
import org.vuphone.wwatch.android.mapview.Route;
import org.vuphone.wwatch.android.mapview.pinoverlays.markers.RouteMarker;
import org.vuphone.wwatch.android.mapview.pinoverlays.markers.WreckMarker;

import android.content.Context;
import android.util.Log;

import com.google.android.maps.MapView;

/**
 * This class will act as the Controller for the Wreck/Route overlay
 * MVC implementation.  It contains two lists that serve as the models
 * and controls which points are visible on the map.  This allows the user
 * to have both overlays active at once, but only show the points that
 * the user wants.  Additionally, this class encapsulates all overlay
 * control to a couple of methods making it easy to dictate which points 
 * are shown without touching the overlay objects.
 * @author Chris Thompson
 *
 */
public class PinController {

	//TODO: Change this so that we can somehow associate the wrecks with the routes and they
	//don't necessarily have to be in the same order
	private List<Waypoint> wrecks_ = new CopyOnWriteArrayList<Waypoint>();
	
	private static final String TAG = VUphone.tag;
	private static final String PREFIX = "PinController: ";

	private WreckOverlay wo_;
	private RouteOverlay ro_;


	public static enum State {
		SHOW_WRECKS, SHOW_ONE_WRECK, SHOW_ROUTE
	}

	private State curState_ = State.SHOW_WRECKS;
	private MapView mv_;
	private Context c_;
	private int wreckToShow_ = 0;
	private Waypoint lastTappedPoint_ = new Waypoint(0, 0, System.currentTimeMillis());
	private Route currentRoute_;
	private Cache cache_;



	public PinController(MapView mv, Context c){
		mv_ = mv;
		c_ = c;
		wo_ = new WreckOverlay(c, this);
		ro_ = new RouteOverlay(this);
	}
	
	public void changeState(State s){
		curState_ = s;
	}
	
	public Waypoint getRouteItem(int i){
		return new RouteMarker(currentRoute_.getPoint(i));
	}
	
	public RouteOverlay getRouteOverlay(){
		return ro_;
	}
	
	public int getRouteSize(){
		int size;
		if (curState_ == State.SHOW_ROUTE && currentRoute_ != null){
			size = currentRoute_.getSize();
		}else{
			size = 0; 
		}
		return size;
	}
	
	public State getState(){
		return curState_;
	}
	
	public Waypoint getWreck(int i){
		if (curState_ == State.SHOW_ONE_WRECK){
			return new WreckMarker(wrecks_.get(wreckToShow_));
		}else{
			return wrecks_.get(i);
		}
	}
	
	public WreckOverlay getWreckOverlay(){
		return wo_;
	}
	
	public int getWreckSize(){
		if (curState_ == State.SHOW_ONE_WRECK){
			return 1;
		}else{
			return wrecks_.size();
		}
	}
	
	public boolean onWreckTap(int i){
		Waypoint point = wrecks_.get(i);
		Log.d(TAG, PREFIX + "onTap waypoint: " + point);

		mv_.getController().animateTo(point.getPoint());
		
		AccidentActivity aa = (AccidentActivity) c_;
		if (!aa.isLookingForWreckId()) {
			// If this is the second tap, open dialog
			if (point.equals(lastTappedPoint_)) {
				final int id = point.getAccidentId();
				wo_.showGalleryDialog(id);
				return true;
			}

			// If not, show route
			currentRoute_ = cache_.getRoute(point);
			curState_ = State.SHOW_ROUTE;
			ro_.populatePins();


			lastTappedPoint_ = point;
		}
		else {
			aa.setWreckId(point.getAccidentId());
		}
		
		return true;
	}
	
	public void setCache(Cache c){
		cache_ = c;
	}
	
	public void setWreckToShow(int i){
		wreckToShow_ = i;
	}
	
	
	/**
	 * Replaces the List of Wrecks already displayed on the map with the new
	 * list. Note that updating the list of pins on the map is expensive, so it
	 * will always be better to perform larger pin updates, less frequently,
	 * than smaller, more frequent updates
	 * 
	 * @param wrecks
	 *            the new list of Pins to be displayed
	 * 
	 */
	public void updateWrecks(final List<Waypoint> wrecks){

		for(Waypoint wp:wrecks)
			wp.setContext(c_);

		wrecks_.clear();
		wrecks_.addAll(wrecks);
		wo_.populatePins();
	}
	


}
