package uk.ac.ox.oucs.oauth.admin;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import uk.ac.ox.oucs.oauth.admin.pages.ListConsumers;

public class OauthAdminApplication extends WebApplication {
    @Override
    protected void init() {
        //Configure for Spring injection
        addComponentInstantiationListener(new SpringComponentInjector(this));
        //getComponentInstantiationListeners().add(new SpringComponentInjector(this));
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return ListConsumers.class;
    }


}
