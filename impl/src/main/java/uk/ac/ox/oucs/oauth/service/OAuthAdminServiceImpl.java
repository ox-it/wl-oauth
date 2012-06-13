package uk.ac.ox.oucs.oauth.service;

import uk.ac.ox.oucs.oauth.dao.AccessorDao;
import uk.ac.ox.oucs.oauth.dao.ConsumerDao;
import uk.ac.ox.oucs.oauth.domain.Accessor;
import uk.ac.ox.oucs.oauth.domain.Consumer;

import java.util.Collection;

/**
 * @author Colin Hebert
 */
public class OAuthAdminServiceImpl implements OAuthAdminService {
    private OAuthService oAuthService;
    private ConsumerDao consumerDao;
    private AccessorDao accessorDao;

    @Override
    public Consumer getConsumer(String consumerId) {
        return consumerDao.get(consumerId);
    }

    @Override
    public void createConsumer(Consumer consumer) {
        consumerDao.create(consumer);
    }

    @Override
    public Consumer updateConsumer(Consumer consumer) {
        return consumerDao.update(consumer);
    }

    @Override
    public void deleteConsumer(Consumer consumer) {
        Collection<Accessor> accessors = accessorDao.getByConsumer(consumer.getId());
        for (Accessor accessor : accessors) {
            oAuthService.revokeAccessor(accessor.getToken());
        }
        consumerDao.remove(consumer);
    }

    @Override
    public Collection<Consumer> getAllConsumers() {
        return consumerDao.getAll();
    }

    @Override
    public void switchRecordMode(Consumer consumer) {
        consumer.setRecordModeEnabled(!consumer.isRecordModeEnabled());
        consumerDao.update(consumer);
    }

    public void setOAuthService(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    public void setConsumerDao(ConsumerDao consumerDao) {
        this.consumerDao = consumerDao;
    }

    public void setAccessorDao(AccessorDao accessorDao) {
        this.accessorDao = accessorDao;
    }
}
