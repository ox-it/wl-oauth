package uk.ac.ox.oucs.oauth.service;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthProblemException;
import uk.ac.ox.oucs.oauth.domain.Accessor;
import uk.ac.ox.oucs.oauth.domain.Consumer;

/**
 * @author Colin Hebert
 */
public class Util {
    public static OAuthAccessor convertToOAuthAccessor(Accessor accessor, OAuthConsumer consumer) throws OAuthProblemException {
        if (accessor == null)
            return null;
        if (!consumer.consumerKey.equals(accessor.getConsumerId()))
            throw new OAuthProblemException(OAuth.Problems.CONSUMER_KEY_REFUSED);
        OAuthAccessor oAuthAccessor = new OAuthAccessor(consumer);
        if (accessor.getType() == Accessor.Type.ACCESS)
            oAuthAccessor.accessToken = accessor.getToken();
        else
            oAuthAccessor.requestToken = accessor.getToken();
        oAuthAccessor.tokenSecret = accessor.getSecret();
        return oAuthAccessor;
    }

    public static OAuthConsumer convertToOAuthConsumer(Consumer consumer) {
        if (consumer == null)
            return null;
        return new OAuthConsumer(consumer.getCallbackURL(), consumer.getId(), consumer.getSecret(), null);
    }
}
