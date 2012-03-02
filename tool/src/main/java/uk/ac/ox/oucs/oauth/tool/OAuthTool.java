package uk.ac.ox.oucs.oauth.tool;

import org.sakaiproject.component.cover.ComponentManager;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.util.ResourceLoader;
import uk.ac.ox.oucs.oauth.domain.Accessor;
import uk.ac.ox.oucs.oauth.domain.Consumer;
import uk.ac.ox.oucs.oauth.service.OAuthService;

import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Colin Hebert
 */
public class OAuthTool {
    private HtmlDataTable accessorTable;
    private ResourceLoader msgs = new ResourceLoader("uk.ac.ox.oucs.oauth.tool.Messages");
    private SessionManager sessionManager = (SessionManager) ComponentManager.getInstance().get(SessionManager.class.getCanonicalName());
    private OAuthService oAuthService = (OAuthService) ComponentManager.get(OAuthService.class.getCanonicalName());

    public List<SimpleAccessor> getAccessors() {
        List<SimpleAccessor> accessors = new LinkedList<SimpleAccessor>();
        String userId = sessionManager.getCurrentSessionUserId();
        for (Accessor accessor : oAuthService.getAccessAccessorForUser(userId)) {
            Consumer consumer = oAuthService.getConsumer(accessor.getConsumerId());
            SimpleAccessor simpleAccessor = new SimpleAccessor();
            simpleAccessor.setToken(accessor.getToken());
            simpleAccessor.setCreationDate(accessor.getCreationDate());
            simpleAccessor.setExpirationDate(accessor.getExpirationDate());
            simpleAccessor.setConsumerUrl(consumer.getURL());
            simpleAccessor.setConsumerDescription(consumer.getDescription());
            simpleAccessor.setConsumerName(consumer.getName());

            accessors.add(simpleAccessor);
        }

        Collections.sort(accessors);

        return accessors;
    }

    public String remove() {
        SimpleAccessor simpleAccessor = (SimpleAccessor) getAccessorTable().getRowData();
        oAuthService.revokeAccessor(simpleAccessor.getToken());
        FacesContext.getCurrentInstance().addMessage("consumers", new FacesMessage(msgs.getFormattedMessage("removed.ok", new Object[]{simpleAccessor.getConsumerName()})));
        return "main";
    }

    public HtmlDataTable getAccessorTable() {
        return accessorTable;
    }

    public void setAccessorTable(HtmlDataTable accessorTable) {
        this.accessorTable = accessorTable;
    }
}
