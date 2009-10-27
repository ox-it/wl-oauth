package uk.ac.ox.oucs.vle;

import java.util.Set;

import net.oauth.OAuthConsumer;


/**
 * Interface to manage OAuth tokens.
 * @author buckett
 *
 */
public interface TokenManager {

	public Set<OAuthConsumer> getAuthorizedConsumers(String userId);
	
	
}
