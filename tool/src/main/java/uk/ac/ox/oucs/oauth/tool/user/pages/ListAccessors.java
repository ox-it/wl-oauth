package uk.ac.ox.oucs.oauth.tool.user.pages;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.sakaiproject.tool.api.SessionManager;
import uk.ac.ox.oucs.oauth.domain.Accessor;
import uk.ac.ox.oucs.oauth.domain.Consumer;
import uk.ac.ox.oucs.oauth.exception.InvalidConsumerException;
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
        ListView<Accessor> accessorList = new ListView<Accessor>("accessorlist", new ArrayList<Accessor>(accessors)) {
            @Override
            protected void populateItem(ListItem<Accessor> components) {
                try {
                    final Consumer consumer = oAuthService.getConsumer(components.getModelObject().getConsumerId());
                    DateFormat dateFormat = DateFormat.getDateInstance();
                    ExternalLink consumerHomepage = new ExternalLink("consumerUrl", consumer.getURL(), consumer.getName());
                    consumerHomepage.setEnabled(consumer.getURL() != null && !consumer.getURL().isEmpty());
                    components.add(consumerHomepage);
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
                } catch (InvalidConsumerException invalidConsumerException) {
                    //Invalid consumer, it is probably deleted
                    //For security reasons, this token should be revoked
                    oAuthService.revokeAccessor(components.getModelObject().getToken());
                    components.setVisible(false);
                }
            }

            @Override
            public boolean isVisible() {
                return !getModelObject().isEmpty() && super.isVisible();
            }
        };
        add(accessorList);



        Label noAccessorLabel = new Label("noAccessor", new ResourceModel("no.accessor"));
        noAccessorLabel.setVisible(!accessorList.isVisible());
        add(noAccessorLabel);
    }
}
