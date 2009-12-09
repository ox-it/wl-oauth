package uk.ac.ox.oucs.vle;

import java.util.HashSet;
import java.util.Set;

import org.sakaiproject.authz.api.SecurityAdvisor;
import org.sakaiproject.util.StringUtil;

/**
 * Small security advisor that just collects all the permissions requests that have been
 * asked for during a request. This can be used for building list of permissions that should be
 * allowed for some requests.
 * @author buckett
 *
 */
public class CollectingPermissionAdvisor implements SecurityAdvisor {

	private Set<String> permissions = new HashSet<String>();
	
	public SecurityAdvice isAllowed(String userId, String function, String reference) {
		permissions.add(function);
		return SecurityAdvice.PASS;
	}
	
	public Set<String> getRequestedPermissions() {
		return permissions;
	}
	
	public String toString() {
		return "Collected permisions: "+ StringUtil.unsplit(permissions.toArray(new String[0]), ",");
	}

}
