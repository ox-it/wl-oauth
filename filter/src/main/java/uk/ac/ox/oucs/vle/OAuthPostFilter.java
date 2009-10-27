package uk.ac.ox.oucs.vle;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.sakaiproject.event.cover.UsageSessionService;
import org.sakaiproject.tool.cover.SessionManager;
import org.sakaiproject.user.api.Authentication;
import org.sakaiproject.user.api.UserNotDefinedException;
import org.sakaiproject.user.cover.UserDirectoryService;

public class OAuthPostFilter implements Filter {

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest servletRequest, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (servletRequest instanceof HttpServletRequest) {
			HttpServletRequest request = (HttpServletRequest) servletRequest;
			Principal principal = request.getUserPrincipal();
			if (principal != null) {
				if (SessionManager.getCurrentSessionUserId() == null) {
					// Need to do login then :-)
					String userId = principal.getName();
					try {
						String userEid = UserDirectoryService.getUserEid(userId);
						// TODO This is a hack and we should go through the AuthenticationManager API.
						Authentication authentication = new org.sakaiproject.util.Authentication(userId, userEid);
						UsageSessionService.login(authentication, request);
					} catch (UserNotDefinedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		chain.doFilter(servletRequest, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

}
