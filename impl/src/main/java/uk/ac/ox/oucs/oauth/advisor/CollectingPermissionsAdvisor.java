package uk.ac.ox.oucs.oauth.advisor;

import org.sakaiproject.authz.api.SecurityAdvisor;
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
    private ConsumerDao consumerDao;
    private Consumer consumer;

    public CollectingPermissionsAdvisor(ConsumerDao consumerDao, Consumer consumer) {
        this.consumerDao = consumerDao;
        this.consumer = consumer;
    }

    public SecurityAdvice isAllowed(String userId, String function, String reference) {
        if (!consumer.getRights().contains(function)) {
            try {
                consumer.getRights().add(function);
                consumerDao.update(consumer);
            } catch (Exception e) {
                //If the update doesn't work, carry on
                //TODO: Log this exception
            }
        }
        return SecurityAdvice.PASS;
    }

    public String toString() {
        return "Permission collector";
    }
}
