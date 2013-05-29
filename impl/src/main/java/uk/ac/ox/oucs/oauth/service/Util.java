package uk.ac.ox.oucs.oauth.service;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthProblemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ox.oucs.oauth.dao.ConsumerDao;
import uk.ac.ox.oucs.oauth.domain.Accessor;
import uk.ac.ox.oucs.oauth.domain.Consumer;

/**
 * @author Colin Hebert
 */
public final class Util {
    private static final Logger logger = LoggerFactory.getLogger(Util.class);

    private Util() {
    }

    public static OAuthAccessor convertToOAuthAccessor(Accessor accessor, OAuthConsumer oAuthConsumer)
            throws OAuthProblemException {
        if (accessor == null)
            return null;
        if (!oAuthConsumer.consumerKey.equals(accessor.getConsumerId()))
            throw new OAuthProblemException(OAuth.Problems.CONSUMER_KEY_REFUSED);
        OAuthAccessor oAuthAccessor = new OAuthAccessor(oAuthConsumer);
        if (accessor.getType() == Accessor.Type.ACCESS)
            oAuthAccessor.accessToken = accessor.getToken();
        else
            oAuthAccessor.requestToken = accessor.getToken();
        oAuthAccessor.tokenSecret = accessor.getSecret();
        // Support Variable Accessor Secret http://wiki.oauth.net/w/page/12238502/AccessorSecret
        if (accessor.getAccessorSecret() != null)
            oAuthConsumer.setProperty(OAuthConsumer.ACCESSOR_SECRET, accessor.getAccessorSecret());
        return oAuthAccessor;
    }

    public static OAuthConsumer convertToOAuthConsumer(Consumer consumer) {
        if (consumer == null)
            return null;
        OAuthConsumer oAuthConsumer = new OAuthConsumer(consumer.getCallbackUrl(), consumer.getId(),
                consumer.getSecret(), null);
        // Support Accessor Secret http://wiki.oauth.net/w/page/12238502/AccessorSecret
        oAuthConsumer.setProperty(OAuthConsumer.ACCESSOR_SECRET, consumer.getAccessorSecret());
        return oAuthConsumer;
    }

    public static void importConsumers(ConsumerDao source, ConsumerDao destination) {
        for (Consumer consumer : source.getAll()) {
            try {
                destination.create(consumer);
                logger.info("New consumer imported '{}'", consumer.getId());
            } catch (Exception e) {
                logger.warn("Impossible to import '{}' as a consumer.", consumer.getId(), e);
            }
        }
    }
}
