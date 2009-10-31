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
