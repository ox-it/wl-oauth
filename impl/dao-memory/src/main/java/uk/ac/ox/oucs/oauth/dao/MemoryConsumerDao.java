package uk.ac.ox.oucs.oauth.dao;

import uk.ac.ox.oucs.oauth.domain.Consumer;

import java.util.Map;

/**
 * @author Colin Hebert
 */
public class MemoryConsumerDao implements ConsumerDao {
    private final Map<String, Consumer> consumers;

    public MemoryConsumerDao(Map<String, Consumer> consumers) {
        this.consumers = consumers;
    }

    public void create(Consumer consumer) {
        //TODO: Throw an exception if the consumer already exists?
        consumers.put(consumer.getId(), consumer);
    }

    public Consumer get(String consumerId) {
        return consumers.get(consumerId);
    }

    public Consumer update(Consumer consumer) {
        consumers.put(consumer.getId(), consumer);
        //Two steps, to be sure, because we don't know the Map implementation
        return get(consumer.getId());
    }

    public void remove(String consumerId) {
        consumers.remove(consumerId);
    }
}
