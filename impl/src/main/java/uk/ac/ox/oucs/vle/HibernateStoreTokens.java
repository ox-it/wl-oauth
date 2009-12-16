package uk.ac.ox.oucs.vle;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class HibernateStoreTokens extends HibernateDaoSupport implements StoredTokensDAO {

	private static final Log log = LogFactory.getLog(HibernateStoreTokens.class);

	public void save(final StoredTokens tokens) {
		getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
			throws HibernateException, SQLException {
				session.saveOrUpdate(tokens);
				if (tokens.getUser() != null && tokens.getAccessToken() != null) {
					// And remove any old stored tokens which are authorized and an access token has been created.
					int removed = session.createQuery(
							"DELETE from uk.ac.ox.oucs.vle.StoredTokens WHERE accessToken IS NOT NULL AND user = ? AND id != ?")
							.setString(0, tokens.getUser()).setString(1, tokens.getId()).executeUpdate();
					log.debug("Removed "+ removed+ " old tokens.");
				}
				return null;
			}

		});
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
		getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
			throws HibernateException, SQLException {
				// 1 hour ago.
				long oldest = System.currentTimeMillis() - (1000 * 60 * 60);
				int removed = session
				.createQuery(
						"DELETE from uk.ac.ox.oucs.vle.StoredTokens WHERE accessToken IS NULL AND user IS NOT NULL AND updated < ?")
						.setDate(0, new Date(oldest)).executeUpdate();
				log.debug("Removed " + removed + " old tokens.");
				return null;
			}
		});
	}

	public List<StoredTokens> loadByUser(String userId) {
		List<StoredTokens> tokens = (List<StoredTokens>)getHibernateTemplate().find(
				"from uk.ac.ox.oucs.vle.StoredTokens WHERE user = ? AND accessToken IS NOT NULL",
				new Object[]{userId}
		);
		return tokens;
	}

	public void remove(final String tokenId) {
		getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException,
			SQLException {
				StoredTokens token = (StoredTokens) session.load(StoredTokens.class, tokenId);
				session.delete(token);
				return token;
			}
		});
	}

}
