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
package org.vuphone.vandyupon.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ImageDigestTester {

	public static void main(String[] args){
		try {

			File image = new File("2008-05-8 ChrisMikeNinaCampus.jpg");
			byte[] bytes = new byte[(int) image.length()];
			FileInputStream fis = new FileInputStream(image);

			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length
					&& (numRead=fis.read(bytes, offset, bytes.length-offset)) >= 0) {
				offset += numRead;
			}

			fis.close();
			MessageDigest md = MessageDigest.getInstance("SHA");
			long start = System.currentTimeMillis();
			md.update(bytes);
			byte[] output = md.digest();
			long finish = System.currentTimeMillis();
			System.out.print("Time to hash: ");
			System.out.print(((double)finish-start) / 1000);
			System.out.println();
			
			System.out.print( "Digest: " );
	        for ( byte b : output )
	            {
	            // print byte as 2 hex digits with lead 0. Separate pairs of digits with space
	            System.out.printf( "%02X ", b & 0xff );
	            }


		}catch (IOException e) {
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

}
