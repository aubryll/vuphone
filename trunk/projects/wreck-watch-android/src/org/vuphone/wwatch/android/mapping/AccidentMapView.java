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
package org.vuphone.wwatch.android.mapping;

import org.vuphone.wwatch.android.R;
import org.vuphone.wwatch.android.Waypoint;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;

import com.google.android.maps.MapView;

public class AccidentMapView extends MapView {
	
	private PinGroup pinGroup_;

	public AccidentMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
		super.setBuiltInZoomControls(true);
		pinGroup_ = new PinGroup(BitmapFactory.decodeResource(context.getResources(), R.drawable.unhapppy), context);
		getOverlays().add(pinGroup_);
		
		
	}
	
	public void addPin(Waypoint point, int id){
		pinGroup_.addPin(point, id);
		postInvalidate();

	}
	


}
