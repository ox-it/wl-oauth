package net.oauth.example.provider.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oauth.server.OAuthServlet;

public class Utils {

	public static void handleException(Exception e, HttpServletRequest request,
	        HttpServletResponse response, boolean sendBody)
	        throws IOException, ServletException {
	    String realm = (request.isSecure())?"https://":"http://";
	    realm += request.getLocalName();
	    OAuthServlet.handleException(response, e, realm, sendBody); 
	}

}
