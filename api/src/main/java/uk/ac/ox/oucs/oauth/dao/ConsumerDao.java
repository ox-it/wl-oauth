package uk.ac.ox.oucs.oauth.dao;

import uk.ac.ox.oucs.oauth.domain.Consumer;

import java.util.Collection;

/**
 * Data access object for consumers (clients)
 *
 * @author Colin Hebert
 */
public interface ConsumerDao {
    void create(Consumer consumer);

    Consumer get(String consumerId);

    Consumer update(Consumer consumer);

    void remove(Consumer consumer);

    Collection<Consumer> getAll();
}
