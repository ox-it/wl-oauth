package uk.ac.ox.oucs.oauth.service;

import org.sakaiproject.authz.api.SecurityAdvisor;
import uk.ac.ox.oucs.oauth.domain.Accessor;
import uk.ac.ox.oucs.oauth.domain.Consumer;

import java.util.Collection;

/**
 * @author Colin Hebert
 */
public interface OAuthService {
    public static final String OUT_OF_BAND_CALLBACK = "oob";
    
    Accessor getAccessor(String oAuthToken, Accessor.Type expectedType);

    SecurityAdvisor getSecurityAdvisor(String accessorId);

    Consumer getConsumer(String consumerKey);

    Accessor createRequestAccessor(String consumerId, String secret, String callback);

    Accessor startAuthorisation(String accessorId);

    Accessor authoriseAccessor(String accessorId, String verifier, String userId);

    Accessor createAccessAccessor(String requestAccessorId);
    
    Collection<Accessor> getAccessAccessorForUser(String userId);
    
    void revokeAccessor(String accessorId);

    void denyRequestAccessor(String token);
}
