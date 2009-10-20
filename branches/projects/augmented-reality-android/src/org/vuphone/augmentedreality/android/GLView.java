package org.vuphone.augmentedreality.android;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

public class GLView extends GLSurfaceView implements Renderer {

	private FloatBuffer data_;
	private long time_; 
	private float angle_ = 0;
	
	public GLView(Context context) {
		super(context);
		
		
		//setEGLConfigChooser(redSize, greenSize, blueSize, alphaSize, depthSize, stencilSize)
		setEGLConfigChooser(5, 6, 5, 8, 16, 0);
		setRenderer(this);
		
        // The following line makes things be draw twice at weird offsets
		getHolder().setFormat(PixelFormat.TRANSLUCENT);
		
		
		data_ = ByteBuffer.allocateDirect(12 * 4).order(
				ByteOrder.nativeOrder()).asFloatBuffer();
		data_.put(-1).put(1).put(0)
			.put(1).put(1).put(0)
			.put(-1).put(-1).put(0)
			.put(1).put(-1).put(0);
		data_.position(0);
		
		time_ = System.currentTimeMillis();
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		gl.glLoadIdentity();
		GLU.gluLookAt(gl, 0, 0, 10, 0, 0, 0, 0, 1, 0);
		
		gl.glColor4f(1, 1, 1, 1);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, data_);
		gl.glPointSize(5);
		

		float dt = (System.currentTimeMillis() - time_) / 1000f;
		angle_ += 100 * dt;
		angle_ %= 360;
		
		//Log.v("GLView", "Angle: " + angle_);
		
		gl.glPushMatrix();
			gl.glTranslatef(0, -5, 0);
			gl.glRotatef(angle_, 0, 0, 1);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		gl.glPopMatrix();
		gl.glPushMatrix();
			gl.glTranslatef(0, 5, 0);
			gl.glRotatef(angle_, 1, 0, 0);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		gl.glPopMatrix();
		gl.glPushMatrix();
			gl.glTranslatef(-5, 0, 0);
			gl.glRotatef(angle_, 0, 1, 0);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		gl.glPopMatrix();

		gl.glPushMatrix();
			gl.glTranslatef(5, 0, 0);
			gl.glRotatef(angle_, 1, 1, 0);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		gl.glPopMatrix();

		
		time_ = System.currentTimeMillis();
}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		
		float ratio = (float) width / height;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 100);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		
		gl.glClearColor(0, 0, 0, 0);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	}

}
