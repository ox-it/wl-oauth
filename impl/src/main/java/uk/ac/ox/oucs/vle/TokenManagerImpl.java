package uk.ac.ox.oucs.vle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.oauth.OAuthAccessor;

import org.sakaiproject.tool.api.SessionManager;

public class TokenManagerImpl implements TokenManager {

	private StoredTokensDAO dao;
	
	private SessionManager sessionManager;
	
	private OAuthProviderImpl oAuthProviderImpl;
	
	public void setDao(StoredTokensDAO dao) {
		this.dao = dao;
	}

	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public void setProvider(OAuthProviderImpl oAuthProviderImpl) {
		this.oAuthProviderImpl = oAuthProviderImpl;
	}

	public Collection<OAuthAccessor> getAuthorizedConsumers() {
		String userId = sessionManager.getCurrentSessionUserId();
		Collection<OAuthAccessor> consumers = null;
		if (userId != null) {
			List<StoredTokens> tokens = dao.loadByUser(userId);
			consumers = new ArrayList<OAuthAccessor>(tokens.size());
			for (StoredTokens token: tokens) {
				consumers.add(oAuthProviderImpl.convert(token));
			}
		}
		return consumers;
	}

	public void removeToken(String id) {
		// TODO Need todo permission check in here.
		String userId = sessionManager.getCurrentSessionUserId();
		if (userId != null) {
			dao.remove(id);
		}
	}

}
