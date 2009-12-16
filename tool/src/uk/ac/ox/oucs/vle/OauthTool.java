/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright (c) 2005, 2006, 2007, 2008 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/

package uk.ac.ox.oucs.vle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;

import net.oauth.OAuthAccessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.util.ResourceLoader;

/**
 * @author buckett
 */
public class OauthTool {

	private static final long serialVersionUID = 1L;

	/** Our log (commons). */
	private static Log M_log = LogFactory.getLog(OauthTool.class);

	private HtmlDataTable accessorTable;

	ResourceLoader msgs = new ResourceLoader("tool-oauth");

	private TokenManager M_token_manager = TokenManagerCover.getInstance();

	public List<OAuthConsumer> getConsumers() {

		List<OAuthAccessor> accessList = new ArrayList<OAuthAccessor>(M_token_manager.getAuthorizedConsumers());
		List<OAuthConsumer> consumers = new ArrayList<OAuthConsumer>(accessList.size());
		for(OAuthAccessor accessor: accessList) {
			String name = (String) accessor.consumer.getProperty(OAuthProvider.NAME);
			String description = (String) accessor.consumer.getProperty(OAuthProvider.DESCRIPTION);
			String url = (String) accessor.consumer.getProperty(OAuthProvider.URL);
			String id = (String) accessor.getProperty(TokenManager.ID);
			Date updated = (Date) accessor.getProperty(TokenManager.UPDATED);
			consumers.add(new OAuthConsumer(id, name, description, url, updated));
		}
		return consumers;
	}

	public String remove() {
		OAuthConsumer consumer = (OAuthConsumer)getAccessorTable().getRowData();
		M_token_manager.removeToken(consumer.getId());
		FacesContext.getCurrentInstance().addMessage("consumers", new FacesMessage(msgs.getFormattedMessage("removed.ok", new Object[]{consumer.getName()})));
		return "main";
	}

	public HtmlDataTable getAccessorTable() {
		return accessorTable;
	}

	public void setAccessorTable(HtmlDataTable accessorTable) {
		this.accessorTable = accessorTable;
	}



}
