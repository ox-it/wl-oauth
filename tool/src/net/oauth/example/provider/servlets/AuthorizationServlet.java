/*
 * Copyright 2007 AOL, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.oauth.example.provider.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sakaiproject.tool.api.ActiveTool;
import org.sakaiproject.tool.api.Session;
import org.sakaiproject.tool.api.Tool;
import org.sakaiproject.tool.cover.ActiveToolManager;
import org.sakaiproject.tool.cover.SessionManager;
import org.sakaiproject.util.Web;

import uk.ac.ox.oucs.vle.OAuthProvider;
import uk.ac.ox.oucs.vle.OAuthProviderCover;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthMessage;
import net.oauth.server.OAuthServlet;

/**
 * Autherization request handler.
 *
 * @author Praveen Alavilli
 */
public class AuthorizationServlet extends HttpServlet {
    
	private OAuthProvider provider;
	
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        provider = OAuthProviderCover.getInstance();
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
    	String currentUserId = SessionManager.getCurrentSessionUserId();
    	if ( currentUserId == null)
    	{
    		// TODO Lots of stuff comes in as request attributes which we need to stash.
    		// get the Sakai session
    		Session session = SessionManager.getCurrentSession();

    		// set the return path for after login if needed (Note: in session, not tool session, special for Login helper)
    		StringBuffer returnUrl = request.getRequestURL();
    		if (request.getQueryString() != null) {
    			returnUrl.append("?").append(request.getQueryString());
    		}
    		session.setAttribute(Tool.HELPER_DONE_URL,returnUrl.toString());

    		// check that we have a return path set; might have been done earlier
    		if (session.getAttribute(Tool.HELPER_DONE_URL) == null)
    		{
    			log("doLogin - proceeding with null HELPER_DONE_URL");
    		}

    		// map the request to the helper, leaving the path after ".../options" for the helper
    		ActiveTool tool = ActiveToolManager.getActiveTool("sakai.login");
    		String context = request.getContextPath() + request.getServletPath() + "/login";
    		tool.help(request, response, context, "/login");
    		return;
    	}
    	
    	try{
            OAuthMessage requestMessage = OAuthServlet.getMessage(request, null);
            
            OAuthAccessor accessor = provider.getAccessor(requestMessage);
            provider.markAsAuthorized(accessor, currentUserId);
            if (accessor.getProperty("user") != null) {
                // already authorized send the user back
                returnToConsumer(request, response, accessor);
            } else {
                sendToAuthorizePage(request, response, accessor);
            }
        
        } catch (Exception e){
            Utils.handleException(e, request, response, true);
        }
        
        
        
    }
    
    @Override 
    public void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException{
    	if (SessionManager.getCurrentSessionUserId() == null)
    	{

    		// get the Sakai session
    		Session session = SessionManager.getCurrentSession();

    		// set the return path for after login if needed (Note: in session, not tool session, special for Login helper)
    		// set the return path for after login if needed (Note: in session, not tool session, special for Login helper)
    		StringBuffer returnUrl = request.getRequestURL();
    		if (request.getQueryString() != null) {
    			returnUrl.append("?").append(request.getQueryString());
    		}
    		session.setAttribute(Tool.HELPER_DONE_URL, returnUrl.toString());

    		// check that we have a return path set; might have been done earlier
    		if (session.getAttribute(Tool.HELPER_DONE_URL) == null)
    		{
    			log("doLogin - proceeding with null HELPER_DONE_URL");
    		}

    		// map the request to the helper, leaving the path after ".../options" for the helper
    		ActiveTool tool = ActiveToolManager.getActiveTool("sakai.login");
    		String context = request.getContextPath() + request.getServletPath() + "/login";
    		tool.help(request, response, context, "/login");
    		return;
    	}
        try{
            OAuthMessage requestMessage = OAuthServlet.getMessage(request, null);
            
            OAuthAccessor accessor = provider.getAccessor(requestMessage);
            
            String userId = request.getParameter("userId");
            if(userId == null){
                sendToAuthorizePage(request, response, accessor);
            }
            // set userId in accessor and mark it as authorized
            provider.markAsAuthorized(accessor, userId);
            
            returnToConsumer(request, response, accessor);
            
        } catch (Exception e){
            Utils.handleException(e, request, response, true);
        }
    }
    
    private void sendToAuthorizePage(HttpServletRequest request, 
            HttpServletResponse response, OAuthAccessor accessor)
    throws IOException, ServletException{
        String callback = request.getParameter("oauth_callback");
        if(callback == null || callback.length() <=0) {
            callback = "none";
        }
        String consumer_description = (String)accessor.consumer.getProperty("description");
        request.setAttribute("CONS_DESC", consumer_description);
        request.setAttribute("CALLBACK", callback);
        request.setAttribute("TOKEN", accessor.requestToken);
        request.getRequestDispatcher //
                    ("/authorize.jsp").forward(request,
                        response);
        
    }
    
    private void returnToConsumer(HttpServletRequest request, 
            HttpServletResponse response, OAuthAccessor accessor)
    throws IOException, ServletException{
        // send the user back to site's callBackUrl
        String callback = request.getParameter("oauth_callback");
        if (callback == null) {
        	callback = (String) accessor.getProperty(OAuth.OAUTH_CALLBACK);
        }
        
        if(callback == null) 
            if (accessor.consumer.callbackURL != null 
                && accessor.consumer.callbackURL.length() > 0){
            // first check if we have something in our properties file
            callback = accessor.consumer.callbackURL;
        }
        
        if(callback == null || "oob".equals(callback)) {
            // no call back it must be a client
            response.setContentType("text/plain");
            PrintWriter out = response.getWriter();
            out.println("You have successfully authorized '" 
                    + accessor.consumer.getProperty("description") 
                    + "'. Please close this browser window and click continue"
                    + " in the client.");
            out.close();
        } else {
            String token = accessor.requestToken;
            // Must add a verifier code.
            if (token != null) {
                callback = OAuth.addParameters(callback, "oauth_token", token);
            }

            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
            response.setHeader("Location", callback);
        }
    }

    private static final long serialVersionUID = 1L;

}
