/**
 * Android Campus Maps
 *  http://code.google.com/p/vuphone/
 * 
 * @author Adam Albright
 * @date Oct 30, 2009
 * 
 * Copyright 2009 VUPhone Team
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License. 
 *  You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software 
 *  distributed under the License is distributed on an "AS IS" BASIS, 
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 *  implied. See the License for the specific language governing 
 *  permissions and limitations under the License. 
 */

package edu.vanderbilt.vuphone.android.campusmaps;

import java.io.BufferedInputStream;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class BuildingInfo extends Activity {
	private Building building_ = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.buildinginfo);

		Bundle extras = getIntent().getExtras();
		int id = -1;
		if (extras == null || (id = extras.getInt("building_id")) < 0)
			return;

		Building b = SharedData.getInstance().getBuildingList().get(id);
		if (b == null)
			finish();

		building_ = b;

		TextView tv = (TextView) findViewById(R.id.buildingName);
		tv.setText(b.getName());

		String img = null;
		if ((img = b.getImageURL()) != null) {
			ImageView iv = (ImageView) findViewById(R.id.buildingImage);

			Bitmap bm = null;

			try {
				BufferedInputStream bis = new BufferedInputStream(new URL(img).openStream(), 1024);
				bm = BitmapFactory.decodeStream(bis);
				bis.close();
			} catch (Exception e) {
			}

			iv.setImageBitmap(bm);
		}
		
		TextView tv2 = (TextView) findViewById(R.id.buildingDesc);
		tv2.setText(b.getDescription());

	}

}
