package uk.ac.ox.oucs.oauth.admin.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.sakaiproject.authz.api.FunctionManager;
import uk.ac.ox.oucs.oauth.dao.ConsumerDao;
import uk.ac.ox.oucs.oauth.domain.Consumer;

import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: oucs0164
 * Date: 06/06/2012
 * Time: 11:44
 * To change this template use File | Settings | File Templates.
 */
public class ConsumerAdministration extends WebPage {
    @SpringBean
    private FunctionManager functionManager;
    @SpringBean
    private ConsumerDao consumerDao;

    private final Consumer consumer;

    public ConsumerAdministration(PageParameters parameters) {
        super(parameters);
        String consumerId = parameters.getString("consumer");
        consumer = consumerDao.get(consumerId);

        Form consumerForm = new Form<Void>("consumerForm") {
            @Override
            protected void onSubmit() {
                super.onSubmit();
                consumerDao.update(consumer);
                info(consumer.getName() + " has been saved.");
            }
        };

        consumerForm.add(new TextField<String>("id", new PropertyModel<String>(consumer, "id")));
        consumerForm.add(new TextField<String>("name", new PropertyModel<String>(consumer, "name")));
        consumerForm.add(new TextArea<String>("description", new PropertyModel<String>(consumer, "description")));
        consumerForm.add(new TextField<String>("url", new PropertyModel<String>(consumer, "uRL")));
        consumerForm.add(new TextField<String>("callbackURL", new PropertyModel<String>(consumer, "callbackURL")));
        consumerForm.add(new TextField<String>("secret", new PropertyModel<String>(consumer, "secret")));
        consumerForm.add(new TextField<String>("accessorSecret", new PropertyModel<String>(consumer, "accessorSecret")));
        consumerForm.add(new TextField<Integer>("defaultValidity", new PropertyModel<Integer>(consumer, "defaultValidity")));

        //Create a list of possible rights as checkboxes, pre-check already granted permissions
        CheckBoxMultipleChoice<String> rightCheckboxes = new CheckBoxMultipleChoice<String>("rights",
                new PropertyModel<Collection<String>>(consumer, "rights"), getAvailableFunctions());
        consumerForm.add(rightCheckboxes);

        add(new FeedbackPanel("feedback"));
        add(new Label("consumerName", consumer.getName()));
        add(consumerForm);
    }

    private List<String> getAvailableFunctions() {
        return functionManager.getRegisteredFunctions();
    }
}