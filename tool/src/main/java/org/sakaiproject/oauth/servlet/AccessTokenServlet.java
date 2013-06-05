package org.sakaiproject.oauth.servlet;

import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.oauth.service.OAuthHttpService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Colin Hebert
 */
public class AccessTokenServlet extends HttpServlet {
    private OAuthHttpService oAuthService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        oAuthService = (OAuthHttpService) ComponentManager.getInstance().get(OAuthHttpService.class.getCanonicalName());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        oAuthService.handleGetAccessToken(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        oAuthService.handleGetAccessToken(request, response);
    }
}
