package uk.ac.ox.oucs.oauth.advisor;

import org.sakaiproject.authz.api.SecurityAdvisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ox.oucs.oauth.dao.ConsumerDao;
import uk.ac.ox.oucs.oauth.domain.Consumer;

/**
 * Advisor used during record phase to determine which permissions must be enabled for the service
 * <p>
 * Every permission used during one request will be enabled automatically.
 * </p>
 *
 * @author Colin Hebert
 */
public class CollectingPermissionsAdvisor implements SecurityAdvisor {
    private static final Logger logger = LoggerFactory.getLogger(CollectingPermissionsAdvisor.class);
    private ConsumerDao consumerDao;
    private Consumer consumer;

    public CollectingPermissionsAdvisor(ConsumerDao consumerDao, Consumer consumer) {
        this.consumerDao = consumerDao;
        this.consumer = consumer;
    }

    @Override
    public SecurityAdvice isAllowed(String userId, String function, String reference) {
        if (!consumer.getRights().contains(function)) {
            logger.info("'{}' requires '{}' right in order to work, enable it.", consumer.getId(), function);
            try {
                consumer.getRights().add(function);
                consumerDao.update(consumer);
            } catch (Exception e) {
                // If the update doesn't work, carry on
                logger.warn("Activation of the '{}' right on '{}' failed.", function, consumer.getId(), e);
            }
        }
        return SecurityAdvice.PASS;
    }

    @Override
    public String toString() {
        return "Permission collector";
    }
}
