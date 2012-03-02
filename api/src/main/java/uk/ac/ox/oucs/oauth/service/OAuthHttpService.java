package uk.ac.ox.oucs.oauth.service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Colin Hebert
 */
public interface OAuthHttpService {
    /**
     * Check the validity of a request toward protected resources
     *
     * Principally used in filters, this method checks the validity of the token provided by a consumer when accessing
     * protected resources on the behalf of a client.
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    boolean isValidOAuthRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;

    String getOAuthAccessToken(HttpServletRequest request) throws IOException;

    void handleRequestToken(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;

    void handleGetAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;

    void handleRequestAuthorisation(HttpServletRequest request, HttpServletResponse response, boolean authorised, String token, String verifier, String userId) throws IOException, ServletException;
}
