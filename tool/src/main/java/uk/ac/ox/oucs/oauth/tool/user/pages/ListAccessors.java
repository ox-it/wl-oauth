package uk.ac.ox.oucs.oauth.tool.user.pages;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.sakaiproject.tool.api.SessionManager;
import uk.ac.ox.oucs.oauth.domain.Accessor;
import uk.ac.ox.oucs.oauth.domain.Consumer;
import uk.ac.ox.oucs.oauth.service.OAuthService;
import uk.ac.ox.oucs.oauth.tool.pages.SakaiPage;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Colin Hebert
 */
public class ListAccessors extends SakaiPage {
    @SpringBean
    private SessionManager sessionManager;
    @SpringBean
    private OAuthService oAuthService;

    public ListAccessors() {
        String userId = sessionManager.getCurrentSessionUserId();
        Collection<Accessor> accessors = oAuthService.getAccessAccessorForUser(userId);
        add(new ListView<Accessor>("accessorlist", new ArrayList<Accessor>(accessors)) {
            @Override
            protected void populateItem(ListItem<Accessor> components) {
                final Consumer consumer = oAuthService.getConsumer(components.getModelObject().getConsumerId());
                DateFormat dateFormat = DateFormat.getDateInstance();
                components.add(new ExternalLink("consumerUrl", consumer.getURL(), consumer.getName()));
                components.add(new Label("consumerDescription", consumer.getDescription()));
                components.add(new Label("creationDate", dateFormat.format(components.getModelObject().getCreationDate())));
                components.add(new Label("expirationDate", dateFormat.format(components.getModelObject().getExpirationDate())));

                components.add(new Link<Accessor>("delete", components.getModel()) {
                    @Override
                    public void onClick() {
                        try {
                            oAuthService.revokeAccessor(getModelObject().getToken());
                            setResponsePage(getPage().getClass());
                            getSession().info(consumer.getName() + "' token has been removed.");
                        } catch (Exception e) {
                            warn("Couldn't remove '" + consumer.getName() + "'s token': " + e.getLocalizedMessage());
                        }
                    }
                });
            }
        });
    }
}
