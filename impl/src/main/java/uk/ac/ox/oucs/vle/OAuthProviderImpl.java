package uk.ac.ox.oucs.vle;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.component.api.ServerConfigurationService;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;
import net.oauth.OAuthProblemException;
import net.oauth.OAuthValidator;

public class OAuthProviderImpl implements OAuthProvider {

	private static final Log log = LogFactory.getLog(OAuthProviderImpl.class);
	
	private HashMap<String, OAuthConsumer> consumers = new HashMap<String, OAuthConsumer>();
	
	private OAuthValidator validator;
	
	private ServerConfigurationService serverConfig;
	
	public void setValidator(OAuthValidator validator) {
		this.validator = validator;
	}

	public void setServerConfig(ServerConfigurationService serverConfig) {
		this.serverConfig = serverConfig;
	}

	public void setStore(StoredTokensDAO store) {
		this.store = store;
	}

	private StoredTokensDAO store;
	
	public void init() {
		String consumersConfig = serverConfig.getString("oauth.consumers", null);
		if (consumersConfig != null) {
			String consumerKeys[] = consumersConfig.split(",");
			for (String consumerKey : consumerKeys) {
				String consumerDescription = serverConfig.getString("oauth."+ consumerKey+ ".description", null);
				String consumerCallbackURL = serverConfig.getString("oauth."+ consumerKey+ ".callbackURL", null);
				String consumerSecret = serverConfig.getString("oauth."+ consumerKey+ ".secret", null);
				OAuthConsumer consumer = new OAuthConsumer(consumerCallbackURL, consumerKey, consumerSecret, null);
				consumer.setProperty("description", consumerDescription);
				consumers.put(consumerKey, consumer);
			}
		}
		if (consumers.size() == 0) {
			log.warn("No OAuth consumers configured.");
		} else {
			log.info("Loaded details of "+ consumers.size()+ " consumers.");
		}
	}
	
	public void generateAccessToken(OAuthAccessor accessor)
			throws OAuthException {
        // generate oauth_token and oauth_secret
        String consumer_key = (String) accessor.consumer.getProperty("name");
        // generate token and secret based on consumer_key
        
        // for now use md5 of name + current time as token
        String token_data = consumer_key + System.nanoTime();
        String token = DigestUtils.md5Hex(token_data);
        // first remove the accessor from cache
        
        accessor.requestToken = null;
        accessor.accessToken = token;
        
        // update token in database
        save(accessor);
	}

	public void generateRequestToken(OAuthAccessor accessor)
			throws OAuthException {

        // generate oauth_token and oauth_secret
        String consumer_key = (String) accessor.consumer.getProperty("name");
        // generate token and secret based on consumer_key
        
        // for now use md5 of name + current time as token
        String token_data = consumer_key + System.nanoTime();
        String token = DigestUtils.md5Hex(token_data);
        // for now use md5 of name + current time + token as secret
        String secret_data = consumer_key + System.nanoTime() + token;
        String secret = DigestUtils.md5Hex(secret_data);
        
        accessor.requestToken = token;
        accessor.tokenSecret = secret;
        accessor.accessToken = null;
        
        
        // add to the local cache
        save(accessor);
        
	}

	public OAuthAccessor getAccessor(OAuthMessage requestMessage)
			throws IOException, OAuthProblemException {
        // try to load from local cache if not throw exception
        String consumer_token = requestMessage.getToken();
        OAuthAccessor accessor = loadByToken(consumer_token);
        if(accessor == null){
            OAuthProblemException problem = new OAuthProblemException("token_expired");
            throw problem;
        }
        
        return accessor;
	}

	public OAuthConsumer getConsumer(OAuthMessage requestMessage)
			throws IOException, OAuthProblemException {
        
        OAuthConsumer consumer = null;
        // try to load from local cache if not throw exception
        String consumer_key = requestMessage.getConsumerKey();
        
        consumer = consumers.get(consumer_key);
        
        if(consumer == null) {
            OAuthProblemException problem = new OAuthProblemException("token_rejected");
            throw problem;
        }
        
        return consumer;
	}

	public void markAsAuthorized(OAuthAccessor accessor, String userId)
			throws OAuthException {
        
        accessor.setProperty("user", userId);
        
        // update token in DB
        save(accessor);

	}

	public void validateMessage(OAuthMessage message, OAuthAccessor accessor)
			throws OAuthException, IOException, URISyntaxException {
		validator.validateMessage(message, accessor);
	}

	StoredTokens convert(OAuthAccessor accessor) {
		StoredTokens token = new StoredTokens();
		token.setId((String) accessor.getProperty("id"));
		token.setAccessToken(accessor.accessToken);
		token.setConsumer(accessor.consumer.consumerKey);
		token.setRequestToken(accessor.requestToken);
		token.setTokenSecret(accessor.tokenSecret);
		token.setUser((String)accessor.getProperty("user"));
		return token;
	}
	
	OAuthAccessor convert(StoredTokens token) {
		String consumerKey = token.getConsumer();
		OAuthConsumer consumer = consumers.get(consumerKey);
		if (consumer == null) {
			throw new RuntimeException("Couldn't find consumer for: "+ consumerKey);
		}
		OAuthAccessor accessor = new OAuthAccessor(consumer);
		accessor.accessToken = token.getAccessToken();
		accessor.requestToken = token.getRequestToken();
		accessor.tokenSecret = token.getTokenSecret();
		accessor.setProperty("id", token.getId());
		accessor.setProperty("user", token.getUser());
		return accessor;
	}
	

	private void save(OAuthAccessor accessor) {
		StoredTokens token = convert(accessor);
		token.setUpdated(new Date());
		store.save(token);
	}
	
	private OAuthAccessor loadByToken(String token) {
		StoredTokens tokens = store.loadByToken(token);
		return (tokens != null)?convert(tokens): null;
	}

}
