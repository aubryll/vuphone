 /**************************************************************************
 * @author Hilmi Mustafah, Bailin Gao, Chris Thompson
 * @date 4/8/10
 * 
 * @section License                                                          
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
 * limitations under the License.
 * 
 * 
 * @section Description
 * @class DiningRatingServlet
 * @see HttpServlet
 * @brief Acts as the intermediary communicator between the server and the internet.
 **************************************************************************/
package org.vuphone.vandyupon.notification.diningrating;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vuphone.vandyupon.ServerUtils;
import org.vuphone.vandyupon.notification.HandlerFailedException;
import org.vuphone.vandyupon.notification.Notification;
import org.vuphone.vandyupon.notification.NotificationHandler;
import org.vuphone.vandyupon.notification.ResponseNotification;

public class DiningRatingServlet extends HttpServlet {

	//Used for serialization
	private static final long serialVersionUID = 1895167101514191256L;
	private static final Logger logger_ = Logger.getLogger(DiningRatingServlet.class.getName());

	@Override
	/**Handles the GET requests, not used/actually coded for
	 * @param req An HttpServletRequest that contains the input
	 * @param resp An HttpServletResponse that will contain the output
	 * 
	 * @throws ServletException A general servlet exception
	 * @throws IOException Indicates a failed/interrupted I/O operation
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	/**Handles the POST requests, which is used exclusively in our case
	 * @param req An HttpServletRequest that contains the input
	 * @param resp An HttpServletResponse that will contain the output
	 * 
	 * @throws ServletException A general servlet exception
	 * @throws IOException Indicates a failed/interrupted I/O operation
	 * @throws HandlerFailedException Generic exception for when issues arise with the handler
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		logger_.log(Level.FINER, "Query was " + req.getQueryString());
	
		DiningParser dp = new DiningParser();
		Notification p = dp.parse(req);
		
		//Test to see if the parser is correct
		if(p instanceof DiningRating)
		{
			System.out.print(((DiningRating)p).getLocation() + " ");
			System.out.print(((DiningRating)p).getDeviceID() + " ");
			System.out.print(((DiningRating)p).getRating() + " \n\n");
		}
		else if(p instanceof DiningRatingRequest)
		{
			System.out.print(((DiningRatingRequest)p).getLocation() + " ");
			System.out.print(((DiningRatingRequest)p).getDeviceID() + " \n\n");
		}
		else
		{
			System.out.print("Error\n");
			System.exit(0);
		}
		
		//Test Handler, and ResponseHandler, and everything else
		NotificationHandler dh = (DiningHandler)ServerUtils.get().getFactory().getBean("DiningHandler");
		
		ResponseNotification nr = null;
		
		try {
			nr = dh.handle(p);//p is declared before parser test.
		} catch (HandlerFailedException e1) {
			e1.printStackTrace();
		}
		
		try {
			((DiningRatingResponseHandler) ServerUtils.get().getFactory().getBean("DiningRatingResponder")).handle(resp, nr);
	
		} catch (HandlerFailedException e) {
			e.printStackTrace();
		}	
	}
}
