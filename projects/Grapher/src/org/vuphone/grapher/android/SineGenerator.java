package org.vuphone.grapher.android;

/**
 * This is a test class that concurrently generates data points to populate a GraphDataSource 
 * object. This implementation produces a sine wave given a series of parameters:
 * amp - The amplitude of the wave
 * per - The period (in seconds) of the wave
 * freq - The frequency of the generator. How many points to produce per second. 
 *  
 * @author Krzysztof Zienkiewicz
 *
 */

public class SineGenerator extends Thread {

	public static final String TAG = "SineGenerator";
	
	GraphDataSource data_;
	
	private float amplitude_;
	private float period_;
	private long delay_;
	
	private final double conversion_;
	
	private long startTime_;
	private boolean isAlive_;
	
	public SineGenerator(GraphDataSource src) {
		this(src, 1, 1, 20);
	}
	
	public SineGenerator(GraphDataSource src, float amp, float per, float freq) {
		super(TAG);
		
		data_ = src;
		amplitude_ = amp;
		period_ = per;
		delay_ = (long) (1000f / freq);
		
		conversion_ = (2.0 * Math.PI / period_);
		
		isAlive_ = false;
		startTime_ = 0;
	}
	
	private float generate(long time) {
		// time is in milliseconds
		double angle = conversion_ * (time / 1000.0);
		return (float) (amplitude_ * Math.sin(angle));
	}
	
	public void kill() {
		isAlive_ = false;
	}
	
	@Override
	public void run() {
		while (isAlive_) {
			long time = System.currentTimeMillis() - startTime_;
			float y = generate(time);
			float x = time / 1000f;
			data_.addData(x, y);
			
			try {
				Thread.sleep(delay_);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public synchronized void start() {
		isAlive_ = true;
		startTime_ = System.currentTimeMillis();
		
		super.start();
	}
	
}
