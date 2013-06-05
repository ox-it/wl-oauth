package org.sakaiproject.oauth.dao;

import org.sakaiproject.oauth.domain.Consumer;

import java.util.Collection;

/**
 * Data access object for consumers (clients).
 *
 * @author Colin Hebert
 */
public interface ConsumerDao {
    void create(Consumer consumer);

    Consumer get(String consumerId);

    Consumer update(Consumer consumer);

    /**
     * Removes a consumer, making it impossible to connect through oAuth with its credentials.
     * <p>
     * A proper implementation of this method MUST also revoke every token associated with the consumer.
     * </p>
     *
     * @param consumer consumer to remove
     */
    void remove(Consumer consumer);

    Collection<Consumer> getAll();
}
