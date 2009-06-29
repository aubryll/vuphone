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
package org.vuphone.wwatch.media.incoming;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageManipulator {

	private static final int MAX_WIDTH = 140;
	private static final int MAX_HEIGHT = 140;
	
	public static BufferedImage scaleDown(File file) {

		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
			.getDefaultScreenDevice().getDefaultConfiguration();
		
		// Load the image
		BufferedImage img = null;
		try {
			img = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
			// TODO - Change this.
			throw new RuntimeException("File not found");
		}
		
		int width = img.getWidth();
		int height = img.getHeight();
		int transparency = img.getColorModel().getTransparency();

		// If current image is within acceptable bounds then return it
		if (width <= MAX_WIDTH && height <= MAX_HEIGHT)
			return img;
		
		// Else, figure out the smallest factor to scale the image to acceptable bounds
		double multX = (double) width / MAX_WIDTH;
		double multY = (double) height / MAX_HEIGHT;
		
		double scale = 1.0 / Math.max(multX, multY);
		AffineTransform trans = AffineTransform.getScaleInstance(scale, scale);
		
		// Create the mini image surface and draw to it the scaled image
		int miniWidth = (int) (width * scale);
		int miniHeight = (int) (height * scale);
		BufferedImage mini = gc.createCompatibleImage(miniWidth, miniHeight, transparency);
		Graphics2D g2 = mini.createGraphics();
		g2.drawRenderedImage(img, trans);
		g2.dispose();
		
		return mini;
	}
	
	public static void saveImage(BufferedImage img, File file) {
		try {
			// TODO - do not hard code this
			ImageIO.write(img, "jpeg", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
