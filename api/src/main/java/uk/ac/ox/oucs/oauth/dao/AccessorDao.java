package uk.ac.ox.oucs.oauth.dao;

import uk.ac.ox.oucs.oauth.domain.Accessor;

import java.util.Collection;

/**
 * Data access object for accessors (tokens).
 *
 * @author Colin Hebert
 */
public interface AccessorDao {
    void create(Accessor accessor);

    Accessor get(String accessorId);

    /**
     * Gets every accessor for a specific user.
     *
     * @param userId user associated with accessors
     * @return accessors for a user
     */
    Collection<Accessor> getByUser(String userId);

    /**
     * Gets every accessor created for a consumer.
     * @param consumerId consumer responsible of these accessors
     * @return accessors for a consumer
     */
    Collection<Accessor> getByConsumer(String consumerId);

    Accessor update(Accessor accessor);

    void remove(Accessor accessor);

    /**
     * Checks every accessor and set them as expired if the date of expiration has passed.
     */
    void markExpiredAccessors();
}
