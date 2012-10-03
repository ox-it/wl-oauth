package uk.ac.ox.oucs.oauth.dao;

import org.sakaiproject.component.api.ServerConfigurationService;
import uk.ac.ox.oucs.oauth.domain.Consumer;

import java.util.*;

/**
 * @author Colin Hebert
 */
public class ServerConfigConsumerDao implements ConsumerDao {
    private Map<String, Consumer> consumers;

    public ServerConfigConsumerDao(ServerConfigurationService serverConfig) {
        Collection<String> consumerKeys = getStringsOrSplit("oauth.consumers", serverConfig);
        consumers = new HashMap<String, Consumer>(consumerKeys.size());
        for (String consumerKey : consumerKeys) {
            Consumer consumer = new Consumer();
            consumer.setId(consumerKey);
            consumer.setName(serverConfig.getString("oauth." + consumerKey + ".name", null));
            consumer.setDescription(serverConfig.getString("oauth." + consumerKey + ".description", null));
            consumer.setURL(serverConfig.getString("oauth." + consumerKey + ".url", null));
            consumer.setCallbackURL(serverConfig.getString("oauth." + consumerKey + ".callbackURL", null));
            consumer.setSecret(serverConfig.getString("oauth." + consumerKey + ".secret"));
            consumer.setAccessorSecret(serverConfig.getString("oauth." + consumerKey + ".accessorsecret", null));
            consumer.setDefaultValidity(serverConfig.getInt("oauth." + consumerKey + ".validity", 0));
            consumer.setRights(new HashSet<String>(getStringsOrSplit("oauth." + consumerKey + ".rights", serverConfig)));
            consumer.setRecordModeEnabled(serverConfig.getBoolean("oauth." + consumerKey + ".record", false));

            consumers.put(consumerKey, consumer);
        }
    }

    private static Collection<String> getStringsOrSplit(String name, ServerConfigurationService serverConfig) {
        String stringValue = serverConfig.getString(name, null);
        String[] values;
        if (stringValue != null)
            values = stringValue.split(",");
        else
            values = serverConfig.getStrings(name);

        return (values != null) ? Arrays.asList(values) : Collections.<String>emptyList();
    }

    @Override
    public void create(Consumer consumer) {
        throw new UnsupportedOperationException("Can't create a consumer in the server config");
    }

    @Override
    public Consumer get(String consumerId) {
        return consumers.get(consumerId);
    }

    @Override
    public Consumer update(Consumer consumer) {
        throw new UnsupportedOperationException("Can't update a consumer in the server config");
    }

    @Override
    public void remove(Consumer consumer) {
        throw new UnsupportedOperationException("Can't remove a consumer in the server config");
    }

    @Override
    public Collection<Consumer> getAll() {
        return consumers.values();
    }
}
