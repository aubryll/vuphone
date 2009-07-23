/*******************************************************************************
 * Copyright 2009 Krzysztof Zienkiewicz
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/

package org.vuphone.augmentedreality.android;

import android.location.Location;
import android.util.Log;

/**
 * A class containing static methods that handle the linear algebra required for
 * calculating the different parameters related to realworld-to-viewport
 * projections.
 * 
 * @author Krzysztof Zienkiewicz
 */

// TODO - This class is very inefficient and needs to be optimized.

public class ARCalculator {

	/**
	 * Returns the adjugate matrix to m.
	 * 
	 * @param m
	 *            A 3 by 3 matrix.
	 * @return The adjugate of m.
	 */
	public static float[][] getAdjugate(float[][] m) {
		float[][] trans = getTranspose(m);
		float[][] adj = new float[3][3];

		for (int c = 0; c < 3; c++) {
			for (int r = 0; r < 3; r++) {
				float signum = (c + r) % 2 == 0 ? 1 : -1;

				adj[c][r] = signum * getMinor(trans, r, c);
			}
		}

		return adj;
	}

	/**
	 * Calculates and returns the unit vectors that form an orthogonal basis
	 * (relative to the standard basis) using the compass heading as a
	 * parameter. The first vector is the "right" vector, then the "up" vector,
	 * and finally the "back" vector. So to pull out the x coordinate of the
	 * forward vector we would call getBasis(angle)[2][0];
	 * 
	 * @param azimuth
	 *            The compass heading where 0 is North, 90 is East, etc.
	 * 
	 * @return The calculated unit vectors.
	 */
	public static float[][] getBasis(float azimuth, float pitch) {
		//float pitch = -90;

		
		float[][] data = new float[3][3];

		// Convert to radians
		//azimuth = azimuth * (float) (Math.PI / 180.0);
		//pitch = pitch * (float) (Math.PI / 180.0);

		float[][] rotMatX = getRotationMatrixX(-90 - pitch);
		float[][] rotMatY = getRotationMatrixY(-azimuth);
		float[][] rotMat = multiply(rotMatX, rotMatY);
		
		// Right
		data[0] = multiply(rotMat, new float[]{1, 0, 0});
		// Up
		data[1] = multiply(rotMat, new float[]{0, 1, 0});
		// Back
		data[2] = multiply(rotMat, new float[]{0, 0, 1});
		
		return data;
	}

	/**
	 * Returns the determinant of the provided matrix.
	 * 
	 * @param m
	 *            A 3 by 3 matrix.
	 * @return The determinant of m.
	 */
	public static float getDeterminant(float m[][]) {
		// Cofactor expansion along first column.

		float det = 0;

		for (int r = 0; r < 3; r++) {
			float signum = (r % 2) == 0 ? 1 : -1;
			float cof = getMinor(m, r, 0);
			det += (signum * cof * m[0][r]);
		}

		return det;
	}

	/**
	 * Calculated and returns the inverse of the provided matrix.
	 * 
	 * @param m
	 *            A 3 by 3 matrix.
	 * @return The inverse.
	 */
	public static float[][] getInverse(float[][] m) {
		float scale = 1f / getDeterminant(m);
		float[][] adj = getAdjugate(m);

		for (int c = 0; c < 3; c++)
			for (int r = 0; r < 3; r++)
				adj[c][r] *= scale;

		return adj;
	}

	/**
	 * Breaks up the provided vectors into a linear combination of basis
	 * vectors.
	 * 
	 * @param basis
	 *            The 3 by 3 matrix whose column vectors form a basis.
	 * @param v
	 *            The 3D vector we wish to break up.
	 * @return A coefficient vector.
	 */
	public static float[] getLinearCombination(float[][] basis, float[] v) {
		float[][] inv = getInverse(basis);
		float[] coeffs = multiply(inv, v);
		return coeffs;
	}

	/**
	 * Returns the minor of matrix m at (row,col). That is, the determinant of
	 * the matrix formed by removing row row and column col from m.
	 * 
	 * @param m
	 *            A 3 by 3 matrix
	 * @param row
	 *            The row to remove
	 * @param col
	 *            The column to remove
	 * @return The minor.
	 */
	public static float getMinor(float[][] m, int row, int col) {
		float[][] sub = new float[2][2];
		int cSub = 0;
		int rSub = 0;

		for (int c = 0; c < 3; c++) {
			if (c == col)
				continue;
			rSub = 0;
			for (int r = 0; r < 3; r++) {
				if (r == row)
					continue;
				sub[cSub][rSub] = m[c][r];
				rSub++;
			}
			cSub++;
		}

		return (sub[0][0] * sub[1][1]) - (sub[1][0] * sub[0][1]);
	}

	/**
	 * Returns the transpose of the provided matrix
	 * 
	 * @param m
	 *            A 3 by 3 matrix.
	 * @return
	 */
	public static float[][] getTranspose(float[][] m) {
		float[][] t = new float[3][3];
		for (int r = 0; r < 3; r++)
			for (int c = 0; c < 3; c++)
				t[r][c] = m[c][r];
		return t;
	}

	/**
	 * Returns the "right" coordinate of the farthest point still visible based
	 * on the angle and depth provided.
	 * 
	 * @param angle
	 *            The horizontal lens angle as measured from the vertical normal
	 *            surface.
	 * @param depth
	 *            The depth as measured by the "forward" component of the
	 *            vector.
	 * @return
	 */
	public static float getHorizontalSpan(float angle, float depth) {
		depth *= depth < 0 ? -1 : 1;
		return depth * (float) Math.tan(Math.toRadians((angle)));
	}

	public static float[] getPointVector(Location origin, Location point) {
		float dist = origin.distanceTo(point);
		float angle = origin.bearingTo(point);
		
		float[] offsets = new float[3];
		offsets[0] = dist *(float) Math.cos(angle);
		offsets[1] = 0;	// For now. Later use altitude
		offsets[2] = -dist * (float) Math.sin(angle);
		
		return offsets;
	}
	
	/**
	 * Returns the "up" coordinate of the farthest point still visible based
	 * on the angle and depth provided.
	 * 
	 * @param angle
	 *            The vertical lens angle as measured from the horizontal normal
	 *            surface.
	 * @param depth
	 *            The depth as measured by the "forward" component of the
	 *            vector.
	 * @return
	 */
	public static float getVerticalSpan(float angle, float depth) {
		depth *= depth < 0 ? -1 : 1;
		return depth * (float) Math.tan(Math.toRadians((angle)));
	}
	
	/**
	 * Outputs the provided matrix to logcat. The matrix's column vectors are
	 * printed.
	 * 
	 * @param m
	 *            A 3 by 3 matrix
	 */
	public static void logMatrix(float[][] m) {
		String str = "";
		for (int c = 0; c < 3; c++) {
			str += "{";
			for (int r = 0; r < 3; r++) {
				str += m[c][r] + " ";
			}
			str = str.trim();
			str += "} ";
		}

		Log.v("AndroidTests", str.trim());
	}

	/**
	 * Outputs the provided vector to logcat.
	 * 
	 * @param v
	 *            A 3D vector.
	 */
	public static void logVector(float[] v) {
		String str = "{";
		for (int c = 0; c < 3; c++) {
			str += v[c] + " ";
		}

		str = str.trim() + "}";
		Log.v("AndroidTests", str);
	}

	/**
	 * Multiplies two 3x3 matrices and returns the result.
	 * 
	 * @param a
	 *            Left hand side matrix
	 * @param b
	 *            Right hand side matrix
	 * @return Product matrix
	 */
	public static float[][] multiply(float[][] a, float[][] b) {
		float[][] c = new float[3][3];

		for (int i = 0; i < 3; i++)
			// column of c
			for (int j = 0; j < 3; j++) { // row of c
				float p = 0;
				for (int z = 0; z < 3; z++)
					p += (a[z][j] * b[i][z]);
				c[i][j] = p;
			}

		return c;
	}

	/**
	 * Multiplies a matrix by a vector.
	 * 
	 * @param m
	 *            A 3 by 3 matrix.
	 * @param v
	 *            A 3D vector.
	 * @return Product vector.
	 */
	public static float[] multiply(float[][] m, float[] v) {
		float[] res = new float[3];

		for (int r = 0; r < 3; r++) {
			float d = 0;
			for (int c = 0; c < 3; c++) {
				d += (m[c][r] * v[c]);
			}
			res[r] = d;
		}

		return res;
	}
	
	public static float[][] getRotationMatrixX(float angle) {
		angle = angle * (float) Math.PI / 180f;
		float[][] m = {{1, 0, 0},						//First column 
						{0, (float) Math.cos(angle), (float) Math.sin(angle)}, 					//Second column
						{0, (float) -Math.sin(angle), (float) Math.cos(angle)}};
		
		return m;
	}

	public static float[][] getRotationMatrixY(float angle) {
		angle = angle * (float) Math.PI / 180f;
		float[][] m = {{(float) Math.cos(angle), 0, (float) -Math.sin(angle)},						//First column 
						{0, 1, 0}, 					//Second column
						{(float) Math.sin(angle), 0, (float) Math.cos(angle)}};
		
		return m;
	}
}
