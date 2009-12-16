package uk.ac.ox.oucs.vle;

import java.util.Collection;

import net.oauth.OAuthAccessor;


/**
 * Interface to manage OAuth tokens.
 * @author buckett
 *
 */
public interface TokenManager {

	String UPDATED = TokenManager.class.getName()+ "#UPDATED";
	String ID = "id";

	/**
	 * Gets the authorized consumers for the current user.
	 * @return A set of consumer tokens.
	 */
	public Collection<OAuthAccessor> getAuthorizedConsumers();
	
	public void removeToken(String id);
}
