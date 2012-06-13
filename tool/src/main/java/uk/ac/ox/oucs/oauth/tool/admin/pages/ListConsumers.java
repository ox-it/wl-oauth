package uk.ac.ox.oucs.oauth.tool.admin.pages;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import uk.ac.ox.oucs.oauth.dao.ConsumerDao;
import uk.ac.ox.oucs.oauth.domain.Consumer;
import uk.ac.ox.oucs.oauth.tool.pages.SakaiPage;

import java.util.ArrayList;

/**
 * @author Colin Hebert
 */
public class ListConsumers extends SakaiPage {
    @SpringBean
    private ConsumerDao consumerDao;

    public ListConsumers() {
        addMenuLink(ListConsumers.class, new ResourceModel("menu.list.consumer"), null);
        addMenuLink(ConsumerAdministration.class, new ResourceModel("menu.add.consumer"), null);

        ListView<Consumer> consumerList = new ListView<Consumer>("consumerlist", new ArrayList<Consumer>(consumerDao.getAll())) {
            @Override
            protected void populateItem(ListItem<Consumer> components) {
                components.add(new Label("id", components.getModelObject().getId()));
                components.add(new Label("name", components.getModelObject().getName()));
                components.add(new Label("description", components.getModelObject().getDescription()));

                components.add(new BookmarkablePageLink<Consumer>("edit", ConsumerAdministration.class)
                        .setParameter("consumer", components.getModelObject().getId()));

                components.add(new Link<Consumer>("delete", components.getModel()) {
                    @Override
                    public void onClick() {
                        try {
                            consumerDao.remove(getModelObject());
                            setResponsePage(getPage().getClass());
                            getSession().info(getModelObject().getName() + " has been removed.");
                        } catch (Exception e) {
                            warn("Couldn't remove '" + getModelObject().getName() + "': " + e.getLocalizedMessage());
                        }
                    }
                });


                Link<Consumer> recordLink = new Link<Consumer>("record", components.getModel()) {
                    @Override
                    public void onClick() {
                        try {
                            getModelObject().setRecordModeEnabled(!getModelObject().isRecordModeEnabled());
                            consumerDao.update(getModelObject());
                            setResponsePage(getPage().getClass());
                            getSession().info(getModelObject().getName() + " record mode has changed.");
                        } catch (Exception e) {
                            warn("Couldn't change record mode on '" + getModelObject().getName() + "': " + e.getLocalizedMessage());
                        }
                    }
                };
                if (components.getModelObject().isRecordModeEnabled())
                    recordLink.add(new Label("recordLink", new ResourceModel("record.disable.link")));
                else
                    recordLink.add(new Label("recordLink", new ResourceModel("record.enable.link")));
                components.add(recordLink);

            }

            @Override
            public boolean isVisible() {
                return !getModelObject().isEmpty() && super.isVisible();
            }
        };
        add(consumerList);

        Label noConsumerLabel = new Label("noConsumer", new ResourceModel("no.consumer"));
        noConsumerLabel.setVisible(!consumerList.isVisible());
        add(noConsumerLabel);
    }
}
