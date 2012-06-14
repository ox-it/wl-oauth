package uk.ac.ox.oucs.oauth.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import uk.ac.ox.oucs.oauth.domain.Consumer;

import java.util.Collection;

public class HibernateConsumerDao extends HibernateDaoSupport implements ConsumerDao {
    public void create(final Consumer consumer) {
        getHibernateTemplate().save(consumer);
    }

    public Consumer get(String consumerId) {
        return (Consumer) getHibernateTemplate().get(Consumer.class, consumerId);
    }

    public Consumer update(Consumer consumer) {
        getHibernateTemplate().update(consumer);
        return consumer;
    }

    public void remove(Consumer consumer) {
        getHibernateTemplate().delete(consumer);
    }

    @Override
    public Collection<Consumer> getAll() {
        return getHibernateTemplate().loadAll(Consumer.class);
    }
}
