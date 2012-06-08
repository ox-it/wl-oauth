package uk.ac.ox.oucs.oauth.admin.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import uk.ac.ox.oucs.oauth.dao.ConsumerDao;
import uk.ac.ox.oucs.oauth.domain.Consumer;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: oucs0164
 * Date: 06/06/2012
 * Time: 11:43
 * To change this template use File | Settings | File Templates.
 */
public class ListConsumers extends WebPage {
    @SpringBean
    private ConsumerDao consumerDao;

    public ListConsumers() {
        add(new ListView<Consumer>("consumerlist", new ArrayList<Consumer>(consumerDao.getAll())) {
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
                        consumerDao.remove(getModelObject());
                    }
                });
            }
        });
    }
}