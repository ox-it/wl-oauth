package uk.ac.ox.oucs.oauth.tool.admin;

import org.sakaiproject.authz.api.FunctionManager;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.component.cover.ComponentManager;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter users before displaying the oAuth administration page.
 *
 * @author Colin Hebert
 */
public class AdminFilter implements Filter {
    private static final String OAUTH_ADMIN_RIGHT = "oauth.admin";
    private SecurityService securityService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        FunctionManager functionManager = (FunctionManager) ComponentManager.get(FunctionManager.class);
        functionManager.registerFunction(OAUTH_ADMIN_RIGHT, false);
        securityService = (SecurityService) ComponentManager.get(SecurityService.class);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (securityService.unlock(OAUTH_ADMIN_RIGHT, "")) {
            chain.doFilter(request, response);
        } else {
            HttpServletResponse res = (HttpServletResponse) response;
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    public void destroy() {
    }
}
