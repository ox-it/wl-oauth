package uk.ac.ox.oucs.oauth.advisor;

import org.sakaiproject.authz.api.SecurityAdvisor;

import java.util.HashSet;
import java.util.Set;

/**
 * Advisor used during test phases to determine which permissions must be enabled for the service
 * <p>
 * Every permission used during one request will be collected to be read later.
 * </p>
 *
 * @author Colin Hebert
 */
public class CollectingPermissionsAdvisor implements SecurityAdvisor {
    private Set<String> permissions = new HashSet<String>();

    public SecurityAdvice isAllowed(String userId, String function, String reference) {
        permissions.add(function);
        return SecurityAdvice.PASS;
    }

    public String toString() {
        return "Collected permissions: " + permissions;
    }
}
