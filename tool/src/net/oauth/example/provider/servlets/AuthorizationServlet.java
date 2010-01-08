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

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthMessage;
import net.oauth.server.OAuthServlet;

import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.tool.api.ActiveTool;
import org.sakaiproject.tool.api.Session;
import org.sakaiproject.tool.api.Tool;
import org.sakaiproject.tool.api.ToolException;
import org.sakaiproject.tool.cover.ActiveToolManager;
import org.sakaiproject.tool.cover.SessionManager;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.cover.UserDirectoryService;

import uk.ac.ox.oucs.vle.OAuthProvider;
import uk.ac.ox.oucs.vle.OAuthProviderCover;

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
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		handleRequest(request, response);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		handleRequest(request, response);
	}

	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String pathInfo = request.getPathInfo();
		if (pathInfo != null && pathInfo.startsWith("/login")) {
			// Doing a login yeah..
			doLogin(request, response);
		} else {
			String currentUserId = SessionManager.getCurrentSessionUserId();
			if (currentUserId == null) {
				Session session = SessionManager.getCurrentSession();

				// set the return path for after login if needed (Note: in
				// session, not tool session, special for Login helper)
				StringBuffer returnUrl = request.getRequestURL();
				if (request.getQueryString() != null) {
					returnUrl.append("?").append(request.getQueryString());
				}
				session
						.setAttribute(Tool.HELPER_DONE_URL, returnUrl
								.toString());

				doLogin(request, response);
			} else {

				try {
					OAuthMessage requestMessage = OAuthServlet.getMessage(
							request, null);

					OAuthAccessor accessor = provider
							.getAccessor(requestMessage);
					if (request.getParameter("authorize") != null) {
						provider.markAsAuthorized(accessor, currentUserId);
					}
					
					// TODO Should make the session as denied rather than unauthorized.
					if (accessor.getProperty("user") != null || request.getParameter("deny") != null) {
						// already authorized send the user back
						returnToConsumer(request, response, accessor);
					} else {
						sendToAuthorizePage(request, response, accessor);
					}

				} catch (Exception e) {
					Utils.handleException(e, request, response, true);
				}
			}
		}

	}

	private void doLogin(HttpServletRequest request,
			HttpServletResponse response) throws ToolException {
		// map the request to the helper, leaving the path after ".../options"
		// for the helper
		ActiveTool tool = ActiveToolManager.getActiveTool("sakai.login");
		String context = request.getContextPath() + request.getServletPath()
				+ "/login";
		tool.help(request, response, context, "/login");
	}

	private void sendToAuthorizePage(HttpServletRequest request,
			HttpServletResponse response, OAuthAccessor accessor)
			throws IOException, ServletException {
		User user = UserDirectoryService.getCurrentUser();

		request.setAttribute("CONS_DESC", accessor.consumer.getProperty(OAuthProvider.DESCRIPTION));
		request.setAttribute("TOKEN", accessor.requestToken);
		request.setAttribute("USER_NAME", user.getDisplayName());
		request.setAttribute("USER_ID", user.getDisplayId());
		request.setAttribute("CONS_NAME", accessor.consumer.getProperty(OAuthProvider.NAME));
		request.setAttribute("SERV_NAME", ServerConfigurationService.getServerName());
		request.setAttribute("SKIN_PATH", ServerConfigurationService.getString("skin.repo", "/library/skin"));
		request.setAttribute("DEFAULT_SKIN", ServerConfigurationService.getString("skin.default", "defulat"));
		request.getRequestDispatcher //
				("/authorize.jsp").forward(request, response);

	}

	private void returnToConsumer(HttpServletRequest request,
			HttpServletResponse response, OAuthAccessor accessor)
			throws IOException, ServletException {
		// send the user back to site's callBackUrl
		String callback = request.getParameter("oauth_callback");
		if (callback == null) {
			callback = (String) accessor.getProperty(OAuth.OAUTH_CALLBACK);
		}

		if (callback == null)
			if (accessor.consumer.callbackURL != null
					&& accessor.consumer.callbackURL.length() > 0) {
				// first check if we have something in our properties file
				callback = accessor.consumer.callbackURL;
			}

		if (callback == null || "oob".equals(callback)) {
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
			if (token != null) {
				callback = OAuth.addParameters(callback, "oauth_token", token);
			}
			String verifier = (String) accessor
					.getProperty(OAuth.OAUTH_VERIFIER);
			if (verifier != null) {
				callback = OAuth.addParameters(callback, OAuth.OAUTH_VERIFIER,
						verifier);
			}

			response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
			response.setHeader("Location", callback);
		}
	}

	private static final long serialVersionUID = 1L;

}
