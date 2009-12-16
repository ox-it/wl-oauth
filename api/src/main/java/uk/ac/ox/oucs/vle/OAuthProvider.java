package uk.ac.ox.oucs.vle;

import java.io.IOException;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;
import net.oauth.OAuthProblemException;
import net.oauth.OAuthValidator;

/**
 * Class for doing the bulk of the OAuth work (storing tokens) that allows.
 * OAuth to work in a cluster. Ideally we don't want to put the OAuth library
 * into shared so want to wrap everything, but I don't think it's worth the
 * effort.
 * 
 * @author buckett
 * 
 */
public interface OAuthProvider extends OAuthValidator {

	public static final String URL = "URL";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";

	public OAuthConsumer getConsumer(OAuthMessage requestMessage)
			throws IOException, OAuthProblemException;

    /**
     * Get the access token and token secret for the given oauth_token. 
     */
	public OAuthAccessor getAccessor(OAuthMessage requestMessage)
			throws IOException, OAuthProblemException;

    /**
     * Set the access token 
     */
	public void markAsAuthorized(OAuthAccessor accessor, String userId)
			throws OAuthException;

    /**
     * Generate a fresh request token and secret for a consumer.
     * 
     * @throws OAuthException
     */
	public void generateRequestToken(OAuthAccessor accessor)
			throws OAuthException;

	/**
	 * Generate a fresh request token and secret for a consumer.
	 * 
	 * @throws OAuthException
	 */
	public void generateAccessToken(OAuthAccessor accessor)
			throws OAuthException;
}
