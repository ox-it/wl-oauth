package uk.ac.ox.oucs.oauth.admin.pages;

import org.apache.wicket.IPageMap;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.sakaiproject.component.api.ServerConfigurationService;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.tool.api.SessionManager;

/**
 * @author Colin Hebert
 */
public abstract class SakaiPage extends WebPage implements IHeaderContributor {
    @SpringBean
    private ServerConfigurationService serverConfigurationService;
    @SpringBean
    private SiteService siteService;
    @SpringBean
    private SessionManager sessionManager;

    protected SakaiPage() {
    }

    protected SakaiPage(IModel<?> model) {
        super(model);
    }

    protected SakaiPage(IPageMap pageMap) {
        super(pageMap);
    }

    protected SakaiPage(IPageMap pageMap, IModel<?> model) {
        super(pageMap, model);
    }

    protected SakaiPage(PageParameters parameters) {
        super(parameters);
    }

    protected SakaiPage(IPageMap pageMap, PageParameters parameters) {
        super(pageMap, parameters);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        // get Sakai skin
        String skinRepo = serverConfigurationService.getString("skin.repo");
        String skin = siteService.findTool(sessionManager.getCurrentToolSession().getPlacementId()).getSkin();
        if (skin == null) {
            skin = serverConfigurationService.getString("skin.default");
        }
        String toolCSS = skinRepo + "/" + skin + "/tool.css";
        String toolBaseCSS = skinRepo + "/tool_base.css";

        // Sakai additions
        response.renderJavascriptReference("/library/js/headscripts.js");
        response.renderCSSReference(toolBaseCSS);
        response.renderCSSReference(toolCSS);
        response.renderOnLoadJavascript("\nif (typeof setMainFrameHeight !== 'undefined'){\nsetMainFrameHeight( window.name );\n}");

        // Tool additions (at end so we can override if required)
        response.renderString("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n");
        response.renderCSSReference(new ResourceReference(getClass(), "style.css"));
    }
}
