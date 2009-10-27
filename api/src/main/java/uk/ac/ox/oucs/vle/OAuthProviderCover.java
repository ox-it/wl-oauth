package uk.ac.ox.oucs.vle;

import org.sakaiproject.component.cover.ComponentManager;

/**
 * Easy way to get an instance of the provider without binding to the component manager.
 * @author buckett
 *
 */
public class OAuthProviderCover {

	private static OAuthProvider m_instance = null;

	/**
	 * Access the component instance: special cover only method.
	 * 
	 * @return the component instance.
	 */
	public static OAuthProvider getInstance() {
		if (ComponentManager.CACHE_COMPONENTS) {
			if (m_instance == null)
				m_instance = (OAuthProvider) ComponentManager
						.get(OAuthProvider.class);
			return m_instance;
		} else {
			return (OAuthProvider) ComponentManager.get(OAuthProvider.class);
		}
	}

}
