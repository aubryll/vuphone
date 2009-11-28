package org.vuphone.grapher.android;

import org.vuphone.grapher.android.GraphView.ScrollMode;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;


public class GraphActivity extends Activity {
	
	private final static String TAG = "GraphActivity";
	
	private GraphView graph_; 
	private SineGenerator gen_;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, TAG + " onCreate()");

        
        GraphDataSource data = new GraphDataSource();
        
        //data.addData(1, 0);
        /*
        data.addData(5, 0);
        data.addData(9, 0);
        */
        
        graph_ = new GraphView(this);
        setContentView(graph_);
        
        graph_.addDataSource(data);
        
        graph_.setScrollMode(ScrollMode.MANUAL);
        //graph_.setScrollMode(ScrollMode.SYNC);
        //graph_.setScrollSync(id);
        
        
        gen_ = new SineGenerator(data);
        

    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	Log.v(TAG, TAG + " onDestroy()");
    	gen_.kill();
    	graph_.onDestroy();
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	Log.v(TAG, TAG + " onPause()");
    	graph_.onPause();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	Log.v(TAG, TAG + " onResume(): ");
    	graph_.onResume();
    }
}