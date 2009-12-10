package uk.ac.ox.oucs.vle;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.authz.api.SecurityAdvisor;
import org.sakaiproject.authz.cover.SecurityService;
import org.sakaiproject.component.cover.ServerConfigurationService;

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
	
	private static final Log log = LogFactory.getLog(OAuthPreFilter.class);

	private OAuthProvider provider;

	// Can share this advisor between threads.
	private SecurityAdvisor limitedPermissionAdvisor = null;

	private boolean debugPermissions = false;

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		if (servletRequest instanceof HttpServletRequest
				&& servletResponse instanceof HttpServletResponse) {
			final HttpServletRequest request = (HttpServletRequest) servletRequest;
			final HttpServletResponse response = (HttpServletResponse) servletResponse;
			OAuthMessage requestMessage = OAuthServlet
					.getMessage(request, null);

			boolean hasOAuth = requestMessage.getToken() != null;
			if (hasOAuth) {
				try {
					CollectingPermissionAdvisor collector = null;
					// TODO Need to check if we have any OAuth parameters in the
					// request.
					OAuthAccessor accessor = provider
							.getAccessor(requestMessage);
					provider.validateMessage(requestMessage, accessor);
					final String userId = (String) accessor.getProperty("user");
					if (userId != null) {
						HttpServletRequest wrappedRequest = new HttpServletRequestWrapper(
								request) {
							public Principal getUserPrincipal() {
								return new Principal() {
									public String getName() {
										return userId;
									}
								};
							}
						};

						if (limitedPermissionAdvisor != null) {
							SecurityService
									.pushAdvisor(limitedPermissionAdvisor);
						}
						if (debugPermissions) {
							collector = new CollectingPermissionAdvisor();
							SecurityService.pushAdvisor(collector);
						}
						chain.doFilter(wrappedRequest, servletResponse);
						
						if (collector != null) {
							log.info(collector);
						}
					} else {
						OAuthProblemException problem = new OAuthProblemException(
								"No authenticated User");
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
		String permissions = ServerConfigurationService.getString("oauth.permissions", null);
		if (permissions != null) {
			Set<String> permissionSet = new HashSet<String>(Arrays.asList(permissions.split(",")));
			limitedPermissionAdvisor = new LimitedPermissionsAdvisor(permissionSet);
			log.info(limitedPermissionAdvisor);
		}
		debugPermissions = ServerConfigurationService.getBoolean("oauth.permissions.debug", false);
	}

}
