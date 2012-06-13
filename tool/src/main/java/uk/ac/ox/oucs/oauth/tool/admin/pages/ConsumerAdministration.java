package uk.ac.ox.oucs.oauth.tool.admin.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.sakaiproject.authz.api.FunctionManager;
import uk.ac.ox.oucs.oauth.dao.ConsumerDao;
import uk.ac.ox.oucs.oauth.domain.Consumer;
import uk.ac.ox.oucs.oauth.tool.pages.SakaiPage;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * @author Colin Hebert
 */
public class ConsumerAdministration extends SakaiPage {
    @SpringBean
    private FunctionManager functionManager;
    @SpringBean
    private ConsumerDao consumerDao;

    private final Consumer consumer;

    public ConsumerAdministration() {
        consumer = new Consumer();
        //Manually set an empty Set for rights to avoid confusion with CheckBoxMultipleChoice and not ending up with a List
        consumer.setRights(new HashSet<String>());
        init(false);
    }

    public ConsumerAdministration(PageParameters parameters) {
        super(parameters);

        String consumerId = parameters.getString("consumer");
        consumer = consumerDao.get(consumerId);
        init(true);
    }

    private void init(final boolean edit) {
        addMenuLink(ListConsumers.class, new ResourceModel("menu.list.consumer"), null);
        addMenuLink(ConsumerAdministration.class, new ResourceModel("menu.add.consumer"), null);

        Form consumerForm = new Form<Void>("consumerForm") {
            @Override
            protected void onSubmit() {
                super.onSubmit();
                try {
                    if (edit)
                        consumerDao.update(consumer);
                    else
                        consumerDao.create(consumer);
                    setResponsePage(ListConsumers.class);
                    getSession().info(consumer.getName() + " has been saved.");
                } catch (Exception e) {
                    error("Couldn't update '" + consumer.getName() + "': " + e.getLocalizedMessage());
                }
            }
        };

        TextField<String> idTextField;
        if (edit) {
            idTextField = new TextField<String>("id");
            idTextField.add(new SimpleAttributeModifier("disabled", "disabled"));
            idTextField.setModel(Model.of(consumer.getId()));
        } else {
            idTextField = new RequiredTextField<String>("id", new PropertyModel<String>(consumer, "id"));
        }
        consumerForm.add(idTextField);

        consumerForm.add(new RequiredTextField<String>("name", new PropertyModel<String>(consumer, "name")));
        consumerForm.add(new TextArea<String>("description", new PropertyModel<String>(consumer, "description")));
        consumerForm.add(new TextField<String>("url", new PropertyModel<String>(consumer, "uRL")));
        consumerForm.add(new TextField<String>("callbackURL", new PropertyModel<String>(consumer, "callbackURL")));
        consumerForm.add(new RequiredTextField<String>("secret", new PropertyModel<String>(consumer, "secret")));
        consumerForm.add(new TextField<String>("accessorSecret", new PropertyModel<String>(consumer, "accessorSecret")));
        consumerForm.add(new TextField<Integer>("defaultValidity", new PropertyModel<Integer>(consumer, "defaultValidity")));

        //Create a list of possible rights as checkboxes, pre-check already granted permissions
        CheckBoxMultipleChoice<String> rightCheckboxes = new CheckBoxMultipleChoice<String>("rights",
                new PropertyModel<Collection<String>>(consumer, "rights"), getAvailableFunctions());
        consumerForm.add(rightCheckboxes);

        add(new Label("consumerName", consumer.getName()));
        add(consumerForm);
    }

    private List<String> getAvailableFunctions() {
        return functionManager.getRegisteredFunctions();
    }
}
