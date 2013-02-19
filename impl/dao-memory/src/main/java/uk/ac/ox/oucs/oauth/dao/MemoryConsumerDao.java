package uk.ac.ox.oucs.oauth.dao;

import uk.ac.ox.oucs.oauth.domain.Consumer;

import java.util.Collection;
import java.util.Map;

/**
 * @author Colin Hebert
 */
public class MemoryConsumerDao implements ConsumerDao {
    private final Map<String, Consumer> consumers;

    public MemoryConsumerDao(Map<String, Consumer> consumers) {
        this.consumers = consumers;
    }

    @Override
    public void create(Consumer consumer) {
        // TODO: Throw an exception if the consumer already exists?
        consumers.put(consumer.getId(), consumer);
    }

    @Override
    public Consumer get(String consumerId) {
        return consumers.get(consumerId);
    }

    @Override
    public Consumer update(Consumer consumer) {
        consumers.put(consumer.getId(), consumer);
        // Two steps, to be sure, because we don't know the Map implementation
        return get(consumer.getId());
    }

    @Override
    public void remove(Consumer consumer) {
        consumers.remove(consumer.getId());
    }

    @Override
    public Collection<Consumer> getAll() {
        return consumers.values();
    }
}
