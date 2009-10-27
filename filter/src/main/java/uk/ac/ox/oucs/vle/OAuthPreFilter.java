package uk.ac.ox.oucs.vle;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;
import net.oauth.OAuthProblemException;
import net.oauth.http.HttpMessage;
import net.oauth.server.OAuthServlet;

/**
 * Class to setup the principal in the request so we base sessions off this.
 * 
 * @author buckett
 * 
 */
public class OAuthPreFilter implements Filter {

	private OAuthProvider provider;

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain chain) throws IOException, ServletException {
		if (servletRequest instanceof HttpServletRequest && servletResponse instanceof HttpServletResponse) {
			final HttpServletRequest request = (HttpServletRequest)servletRequest;
			final HttpServletResponse response = (HttpServletResponse)servletResponse;
            OAuthMessage requestMessage = OAuthServlet.getMessage(request, null);

            boolean hasOAuth = requestMessage.getToken() != null;
            if (hasOAuth) {
    		try {
            // TODO Need to check if we have any OAuth parameters in the request.
            OAuthAccessor accessor = provider.getAccessor(requestMessage);
            provider.validateMessage(requestMessage, accessor);
            final String userId = (String) accessor.getProperty("user");
            if (userId != null) {
            	HttpServletRequest wrappedRequest = new HttpServletRequestWrapper(request) {
            		public Principal getUserPrincipal() {
            			return new Principal() {							
							public String getName() {
								return userId;
							}
						}; 
            		}
            	};
            	chain.doFilter(wrappedRequest, servletResponse);
            } else {
            	OAuthProblemException problem =  new OAuthProblemException("No authenticated User");
            	problem.setParameter(HttpMessage.STATUS_CODE, 401);
            	throw problem;
            }
			} catch (OAuthException oae) {
				OAuthServlet.handleException(response, oae, null);
			} catch (URISyntaxException e) {
				OAuthServlet.handleException(response, e, null);
			}
            } else {
            	chain.doFilter(request, response);
            }
		
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		provider = OAuthProviderCover.getInstance();
	}

}
