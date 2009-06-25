package org.vuphone.wwatch.android.mapview;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.vuphone.wwatch.android.VUphone;
import org.vuphone.wwatch.android.Waypoint;
import org.vuphone.wwatch.android.http.HTTPGetter;
import org.vuphone.wwatch.android.http.HttpOperationListener;
import org.vuphone.wwatch.android.mapview.pinoverlays.PinController;
import org.xml.sax.InputSource;

import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.Projection;

public class AccidentList implements HttpOperationListener {
	private List<Route> routes_ = Collections
			.synchronizedList(new ArrayList<Route>());
	private static String pre = "AccidentList: ";
	private static String tag = VUphone.tag;
	private AccidentMapView map_;
	private GeoPoint curCenter_ = null;
	private static AccidentXMLHandler aXml = new AccidentXMLHandler();

	private Timer t = new Timer("Accident View Delay");
	private final TimerTask task_ = new TimerTask() {

		@Override
		public void run() {

			if (map_.getZoomLevel() > 5) {
				if ((map_.getMapCenter().equals(curCenter_) == false)) {
					curCenter_ = map_.getMapCenter();
					int snHeight = map_.getHeight();
					int snWidth = map_.getWidth();
					Projection p = map_.getProjection();

					GeoPoint upperLeft = p.fromPixels(0, snHeight);
					GeoPoint lowerRight = p.fromPixels(snWidth, 0);

					long maxTime = getLatestTime();
					HTTPGetter.doAccidentGet(lowerRight, upperLeft,
							maxTime, AccidentList.this);
				}
			}

		}
	};

	public AccidentList(AccidentMapView map) {
		map_ = map;
		// TODO - we really need to cache this somehow, and use a cron-esq thing
		// to get it
		t.scheduleAtFixedRate(task_, 0, 9000);

	}

	// TODO - Notice that we will have to update _ALL_ the points, even though
	// we will likely only get one or two more at a time. It would be much
	// better to fix how we add pins so we can add them one at a time. _DONT_
	// put this fix in unless you understand the concurrency issues with the
	// PinOverlay!!! You will slow the entire map down drastically
	public void operationComplete(HttpResponse resp) {
		Log.i(tag, pre + "HTTP operation complete. Processing response.");
		ByteArrayOutputStream bao = new ByteArrayOutputStream();

		try {
			resp.getEntity().writeTo(bao);
			Log.d(tag, pre + "Http response: " + bao.toString());
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(tag, pre + "IOException processing HttpResponse object: "
					+ e.getMessage());
			return;
		}

		ArrayList<Route> newRoutes = new ArrayList<Route>();
		newRoutes = aXml.processXML(new InputSource(new ByteArrayInputStream(
				bao.toByteArray())));

		// Do we have any new routes to add
		if (newRoutes.size() == 0)
			return;

		// Do we care about actually pushing the updates to the map?
		boolean showingAllWrecks = (map_.getOverlayController().getState() == PinController.State.SHOW_WRECKS);

		ArrayList<Waypoint> points = new ArrayList<Waypoint>();
		synchronized (routes_) {
			routes_.addAll(newRoutes);

			if (showingAllWrecks) {
				Iterator<Route> i = routes_.iterator();
				while (i.hasNext())
					points.add(i.next().getWreck());
			}
		}

		if (showingAllWrecks) {
			Log.i(tag, pre + "Adding waypoints: " + points.toString());
			map_.getOverlayController().updateWrecks(points);
		}

	}

	public long getLatestTime() {
		long maxTime = 0;
		synchronized (routes_) {
			Iterator<Route> i = routes_.iterator();
			while (i.hasNext()) {
				long time = i.next().getWreck().getTime();
				if (time > maxTime)
					maxTime = time;
			}
		}

		return maxTime;
	}

	public Route getRoute(Waypoint wreckPoint) {
		Route r = null;
		synchronized (routes_) {
			Iterator<Route> i = routes_.iterator();
			while (i.hasNext()) {
				r = i.next();
				if (r.getWreck().equals(wreckPoint))
					break;
			}
		}

		if (r == null) {
			Log.w(tag, pre + "Returning null for route with wreck:");
			Log.w(tag, pre + " " + wreckPoint.toString());
		}
		
		return r;
	}

	public List<Waypoint> getWrecks() {
		ArrayList<Waypoint> list = new ArrayList<Waypoint>();
		synchronized (routes_) {
			Iterator<Route> i = routes_.iterator();
			while (i.hasNext())
				list.add(i.next().getWreck());
		}

		return list;
	}

	public void stopUpdates() {
		t.cancel();
		task_.cancel();
	}
}
