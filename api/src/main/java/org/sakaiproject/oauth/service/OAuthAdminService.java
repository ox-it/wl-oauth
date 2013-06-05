package org.sakaiproject.oauth.service;

import org.sakaiproject.oauth.domain.Consumer;

import java.util.Collection;

/**
 * Administration service handling every sensitive tasks.
 *
 * @author Colin Hebert
 */
public interface OAuthAdminService {
    /**
     * Get a raw consumer based on its id.
     *
     * @param consumerId Id of the desired consumer
     * @return consumer if it exists,
     */
    Consumer getConsumer(String consumerId);

    /**
     * Create a new consumer available for future oAuth communications.
     *
     * @param consumer consumer to create
     */
    void createConsumer(Consumer consumer);

    /**
     * Update a consumer with new settings.
     *
     * @param consumer consumer to update
     * @return the updated consumer
     */
    Consumer updateConsumer(Consumer consumer);

    /**
     * Delete a consumer and associated accessors.
     *
     * @param consumer consumer to delete
     */
    void deleteConsumer(Consumer consumer);

    /**
     * Get a list of every available consumer.
     *
     * @return every consumer
     */
    Collection<Consumer> getAllConsumers();

    /**
     * Change the record mode of a consumer.
     * <p>
     * A consumer with the record mode enabled will have every right and will keep every right it uses.
     * When the record mode is disabled, every right used before is still available, but others aren't anymore.<br />
     * This is useful to setup a new consumer and enable its rights without having to look for every necessary right.
     * </p>
     *
     * @param consumer consumer on which the record mode will be enabled or disabled.
     */
    void switchRecordMode(Consumer consumer);
}
