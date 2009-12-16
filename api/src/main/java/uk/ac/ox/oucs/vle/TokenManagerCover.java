package uk.ac.ox.oucs.vle;

import org.sakaiproject.component.cover.ComponentManager;

/**
 * Easy way to get an instance of the provider without binding to the component manager.
 * @author buckett
 *
 */
public class TokenManagerCover {

	private static TokenManager m_instance = null;

	/**
	 * Access the component instance: special cover only method.
	 * 
	 * @return the component instance.
	 */
	public static TokenManager getInstance() {
		if (ComponentManager.CACHE_COMPONENTS) {
			if (m_instance == null)
				m_instance = (TokenManager) ComponentManager
						.get(TokenManager.class);
			return m_instance;
		} else {
			return (TokenManager) ComponentManager.get(TokenManager.class);
		}
	}

}
