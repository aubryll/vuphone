package org.vuphone.vandyupon.datamine.facebook;

import org.w3c.dom.Document;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.FacebookWebappHelper;
import com.google.code.facebookapi.FacebookXmlRestClient;
import com.google.code.facebookapi.IFacebookRestClient;

public class FaceBookLoginServlet extends HttpServlet {

	private static final Logger logger_ = Logger.getLogger(FaceBookLoginServlet.class.getName());
	private static String api_key = "a77e1b01f96b4c55f1a225cad255ea9d";
    private static String secret = "4c34288139dd30ed5bb8a65055beb46a";
    private static String FACEBOOK_USER_CLIENT = "facebook.user.client";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		logger_.log(Level.FINER, "Query was " + request.getQueryString());

        HttpSession session = request.getSession(true);
        
    	IFacebookRestClient<Document> userClient = (FacebookXmlRestClient)session.getAttribute(FACEBOOK_USER_CLIENT);
    	if (userClient == null) {
    		logger_.log(Level.SEVERE, "User session doesn't have a Facebook API client setup yet. Creating one and storing it in the user's session.");
    		userClient = new FacebookXmlRestClient(api_key, secret);
    		session.setAttribute(FACEBOOK_USER_CLIENT, userClient);
    	}

    	logger_.log(Level.SEVERE, "Creating a FacebookWebappHelper, which copies fb_ request param data into the userClient");
    	FacebookWebappHelper<Document> facebook = new FacebookWebappHelper<Document>(request, response, api_key, secret, userClient);
    	String nextPage = request.getRequestURI();
    	nextPage = nextPage.substring(nextPage.indexOf("/", 1) + 1); //cut out the first /, the context path and the 2nd /
    	logger_.log(Level.SEVERE, nextPage); 
		
    	boolean redirectOccurred = facebook.requireLogin(nextPage);
    	if(redirectOccurred) {
    		logger_.log(Level.SEVERE, "User needs to login");
    		return;
    	}
    	
    	try {
			String auth_token = userClient.auth_createToken();
			userClient.auth_getSession(auth_token);
		} catch (FacebookException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	/*redirectOccurred = facebook.requireFrame(nextPage);
    	if(redirectOccurred) {
    		logger_.log(Level.SEVERE, "Facebook requires frames so redirect occurred");
    		return;
    	}
*/
//    	long facebookUserID;
    	try {
    		logger_.log(Level.SEVERE, "got here");
    //		Document d = userClient.fql_query("SELECT name FROM event WHERE eid IN (SELECT eid FROM event WHERE event_type = 'Party')");
    		Document d = userClient.fql_query("SELECT name FROM event WHERE eid IN (SELECT eid from event_member WHERE uid IN (SELECT uid2 FROM friend WHERE uid1=" + facebook.getUser() + "))");
    		logger_.log(Level.SEVERE, d.toString());
    	} catch(FacebookException ex) {
    		try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while fetching user's facebook ID");
			} catch (IOException e) {
				e.printStackTrace();
			}
    		logger_.log(Level.SEVERE, "Error while getting cached (supplied by request params) value " +
    				"of the user's facebook ID or while fetching it from the Facebook service " +
    				"if the cached value was not present for some reason. Cached value = {}", userClient.getCacheUserId());
    		return;
    	}
    }
	
}
