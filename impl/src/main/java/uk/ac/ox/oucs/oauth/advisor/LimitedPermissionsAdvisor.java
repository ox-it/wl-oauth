package uk.ac.ox.oucs.oauth.advisor;

import org.sakaiproject.authz.api.SecurityAdvisor;

import java.util.HashSet;
import java.util.Set;

/**
 * Advisor allowing only permissions authorised to a specific consumer.
 *
 * @author Colin Hebert
 */
public class LimitedPermissionsAdvisor implements SecurityAdvisor {

    private Set<String> allowedPermissions;

    public LimitedPermissionsAdvisor(Set<String> permissions) {
        allowedPermissions = new HashSet<String>(permissions);
    }

    public SecurityAdvice isAllowed(String userId, String function, String reference) {
        return (allowedPermissions.contains(function)) ? SecurityAdvice.PASS : SecurityAdvice.NOT_ALLOWED;
    }

    @Override
    public String toString() {
        return "Allowed permissions: " + allowedPermissions;
    }
}
