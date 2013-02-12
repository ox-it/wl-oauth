package uk.ac.ox.oucs.oauth.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import uk.ac.ox.oucs.oauth.domain.Consumer;

import java.util.Collection;

public class HibernateConsumerDao extends HibernateDaoSupport implements ConsumerDao {
    @Override
    public void create(final Consumer consumer) {
        getHibernateTemplate().save(consumer);
    }

    @Override
    public Consumer get(String consumerId) {
        return (Consumer) getHibernateTemplate().get(Consumer.class, consumerId);
    }

    @Override
    public Consumer update(Consumer consumer) {
        getHibernateTemplate().update(consumer);
        return consumer;
    }

    @Override
    public void remove(Consumer consumer) {
        getHibernateTemplate().delete(consumer);
    }

    @Override
    public Collection<Consumer> getAll() {
        return getHibernateTemplate().loadAll(Consumer.class);
    }
}
