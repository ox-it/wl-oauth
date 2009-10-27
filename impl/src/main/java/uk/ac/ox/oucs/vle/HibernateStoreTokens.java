package uk.ac.ox.oucs.vle;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class HibernateStoreTokens extends HibernateDaoSupport implements StoredTokensDAO {
	
	private static final Log log = LogFactory.getLog(HibernateStoreTokens.class);
	
	public void save(StoredTokens tokens) {
		getHibernateTemplate().saveOrUpdate(tokens);
	}
	
	public StoredTokens loadByToken(String token) {
		List<StoredTokens> tokens = (List<StoredTokens>)getHibernateTemplate().find(
				"from uk.ac.ox.oucs.vle.StoredTokens WHERE requestToken = ? OR accessToken = ?",
				new Object[]{token,token}
				);
		if (tokens.size() == 1) {
			return tokens.get(0);
		} else if (tokens.size() > 1) {
			log.error("More than one token matches: "+ token);
		}
		return null;
	}
	
	public void removeOldTokens() {
		// Should remove anything older than 10 minutes that isn't fully setup.
	}
	
}
