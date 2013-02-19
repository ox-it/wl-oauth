package uk.ac.ox.oucs.oauth.filter;

import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.component.cover.ComponentManager;
import uk.ac.ox.oucs.oauth.domain.Accessor;
import uk.ac.ox.oucs.oauth.exception.InvalidAccessorException;
import uk.ac.ox.oucs.oauth.service.OAuthHttpService;
import uk.ac.ox.oucs.oauth.service.OAuthService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

/**
 * First filter to apply with OAuth protocol
 * <p>
 * Checks the validity of the current request and OAuth parameters.
 * Sets a security advisor for the request.
 * </p>
 *
 * @author Colin Hebert
 */
public class OAuthPreFilter implements Filter {
    private OAuthHttpService oAuthHttpService;
    private OAuthService oAuthService;
    private SecurityService securityService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.oAuthService = (OAuthService) ComponentManager.getInstance().get(OAuthService.class);
        this.oAuthHttpService = (OAuthHttpService) ComponentManager.getInstance().get(OAuthHttpService.class);
        this.securityService = (SecurityService) ComponentManager.getInstance().get(SecurityService.class);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Only apply filter if there is an OAuth implementation and a valid OAuth request
        if (oAuthHttpService == null || !oAuthHttpService.isEnabled()
                || !oAuthHttpService.isValidOAuthRequest(req, res)) {
            chain.doFilter(req, response);
            return;
        }

        try {
            String oAuthToken = oAuthHttpService.getOAuthAccessToken(req);
            final Accessor accessor = oAuthService.getAccessor(oAuthToken, Accessor.Type.ACCESS);

            HttpServletRequest wrappedRequest = new HttpServletRequestWrapper(req) {
                @Override
                public Principal getUserPrincipal() {
                    return new Principal() {
                        public String getName() {
                            return accessor.getUserId();
                        }
                    };
                }
            };

            securityService.pushAdvisor(oAuthService.getSecurityAdvisor(accessor.getToken()));

            chain.doFilter(wrappedRequest, response);
        } catch (InvalidAccessorException e) {
            // TODO: Redirect to an error page
        }
    }

    @Override
    public void destroy() {
    }
}
