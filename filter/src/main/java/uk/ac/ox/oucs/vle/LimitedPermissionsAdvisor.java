package uk.ac.ox.oucs.vle;

import java.util.HashSet;
import java.util.Set;

import org.sakaiproject.authz.api.SecurityAdvisor;
import org.sakaiproject.util.StringUtil;

/**
 * Security advisor that only allows some permissions checks to succeed.
 * We are using this for when we OAuth requests which we don't want to trust
 * as much as a normal user login.
 * 
 * This advisor never allows permissions checks, but either denies or passes.
 * @author buckett
 *
 */
public class LimitedPermissionsAdvisor implements SecurityAdvisor {

	private Set<String> allowedPermissions;
	
	public LimitedPermissionsAdvisor (Set<String> permissions) {
		allowedPermissions = new HashSet<String>(permissions);
	}
	
	public SecurityAdvice isAllowed(String userId, String function, String reference) {
		return (allowedPermissions.contains(function))?SecurityAdvice.PASS:SecurityAdvice.NOT_ALLOWED;
	}
	
	@Override
	public String toString() {
		return "Allowed permissions: "+ StringUtil.unsplit(allowedPermissions.toArray(new String[0]), ",");
	}

}
