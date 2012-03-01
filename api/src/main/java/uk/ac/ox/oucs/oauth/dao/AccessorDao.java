package uk.ac.ox.oucs.oauth.dao;

import uk.ac.ox.oucs.oauth.domain.Accessor;

import java.util.Collection;

/**
 * @author Colin Hebert
 */
interface AccessorDao {
    void create(Accessor accessor);

    Accessor get(String accessorId);

    Collection<Accessor> getByUser(String userId);

    Accessor update(Accessor accessor);

    void remove(Accessor accessor);

    void markExpiredAccessors();
}
