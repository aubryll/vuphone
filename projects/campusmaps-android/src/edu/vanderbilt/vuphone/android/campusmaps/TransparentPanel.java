package edu.vanderbilt.vuphone.android.campusmaps;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class TransparentPanel extends LinearLayout {
	private Paint innerPaint, borderPaint;
	private boolean init = false;

	public TransparentPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TransparentPanel(Context context) {
		super(context);
		init();
	}

	private void init() {
		innerPaint = new Paint();
		innerPaint.setARGB(225, 75, 75, 75); // gray
		innerPaint.setAntiAlias(true);

		borderPaint = new Paint();
		borderPaint.setARGB(255, 255, 255, 255);
		borderPaint.setAntiAlias(true);
		borderPaint.setStyle(Style.STROKE);
		borderPaint.setStrokeWidth(2);
	}

	public void setupButtons() {
		init = true;

		ImageButton zoomIn = (ImageButton) findViewById(R.id.button_zoom_in);
		ImageButton zoomOut = (ImageButton) findViewById(R.id.button_zoom_out);
		ImageButton centergps = (ImageButton) findViewById(R.id.button_center_gps);

		zoomIn.setImageResource(android.R.drawable.btn_plus);
		zoomIn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Main.mapView_.getController().zoomIn();
			}
		});

		zoomOut.setImageResource(android.R.drawable.btn_minus);
		zoomOut.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Main.mapView_.getController().zoomOut();
			}
		});

		centergps.setImageResource(R.drawable.centergps);
		centergps.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Main.gps_.centerOnGPS(!Main.gps_.centerOnGPS_);
			}
		});
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		if (!init)
			setupButtons();

		RectF drawRect = new RectF();
		drawRect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());

		canvas.drawRoundRect(drawRect, 5, 5, innerPaint);
		canvas.drawRoundRect(drawRect, 5, 5, borderPaint);

		super.dispatchDraw(canvas);
	}
}
