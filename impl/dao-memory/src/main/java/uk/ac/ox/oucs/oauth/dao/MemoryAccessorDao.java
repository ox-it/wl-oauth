package uk.ac.ox.oucs.oauth.dao;

import uk.ac.ox.oucs.oauth.domain.Accessor;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Colin Hebert
 */
public class MemoryAccessorDao implements AccessorDao {
    private final Map<String, Accessor> accessors;

    public MemoryAccessorDao(Map<String, Accessor> accessors) {
        this.accessors = accessors;
    }

    @Override
    public void create(Accessor accessor) {
        accessors.put(accessor.getToken(), accessor);
    }

    @Override
    public Accessor get(String accessorId) {
        return accessors.get(accessorId);
    }

    @Override
    public Collection<Accessor> getByUser(String userId) {
        Collection<Accessor> retreivedAccessors = new LinkedList<Accessor>();
        for (Accessor accessor : accessors.values()) {
            if (userId.equals(accessor.getUserId()))
                retreivedAccessors.add(accessor);
        }

        return retreivedAccessors;
    }

    @Override
    public Accessor update(Accessor accessor) {
        accessors.put(accessor.getToken(), accessor);
        return get(accessor.getToken());
    }

    @Override
    public void remove(Accessor accessor) {
        accessors.remove(accessor.getToken());
    }

    @Override
    public void markExpiredAccessors() {
        Collection<String> expiredIds = new LinkedList<String>();
        for (Accessor accessor : accessors.values()) {
            if (accessor.getExpirationDate().before(new Date()))
                expiredIds.add(accessor.getToken());
        }

        for (String expiredId : expiredIds) {
            accessors.remove(expiredId);
        }
    }
}
