package org.sakaiproject.oauth.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.sakaiproject.oauth.domain.Accessor;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class HibernateAccessorDao extends HibernateDaoSupport implements AccessorDao {
    @Override
    public void create(final Accessor accessor) {
        getHibernateTemplate().save(accessor);
    }

    @Override
    public Accessor get(String accessorId) {
        return (Accessor) getHibernateTemplate().get(Accessor.class, accessorId);
    }

    @Override
    public List<Accessor> getByUser(String userId) {
        return (List<Accessor>) getHibernateTemplate().find(
                "FROM Accessor a WHERE a.userId = ?",
                new Object[]{userId});
    }

    @Override
    public Collection<Accessor> getByConsumer(String consumerId) {
        return (List<Accessor>) getHibernateTemplate().find(
                "FROM Accessor a WHERE a.consumerId = ?",
                new Object[]{consumerId});
    }

    @Override
    public void markExpiredAccessors() {
        getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                session.createQuery(
                        "UPDATE Accessor a SET a.status=? WHERE a.expirationDate < ?")
                        .setParameter(0, Accessor.Status.EXPIRED)
                        .setDate(1, new Date())
                        .executeUpdate();
                return null;
            }
        });
    }


    @Override
    public Accessor update(Accessor accessor) {
        getHibernateTemplate().update(accessor);
        return accessor;
    }

    @Override
    public void remove(Accessor accessor) {
        getHibernateTemplate().delete(accessor);
    }
}
