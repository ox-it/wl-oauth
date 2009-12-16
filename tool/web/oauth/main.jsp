<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai"%>

<%
	response.setContentType("text/html; charset=UTF-8");
	response.addDateHeader("Expires", System.currentTimeMillis()
			- (1000L * 60L * 60L * 24L * 365L));
	response.addDateHeader("Last-Modified", System.currentTimeMillis());
	response
			.addHeader("Cache-Control",
					"no-store, no-cache, must-revalidate, max-age=0, post-check=0, pre-check=0");
	response.addHeader("Pragma", "no-cache");
%>
<f:view>
	<sakai:view title="#{msgs.title}">

		<h:form id="consumers">
			<sakai:instruction_message value="#{msgs.instructions}" />

			<sakai:messages/>
			<h3><h:outputText value="#{msgs.consumers_heading}"/></h3>
			<h:dataTable value="#{oauth.consumers}" var="consumer"
				binding="#{oauth.accessorTable}">
				<h:column>
					<h:outputLink value="#{consumer.url}" target="_top">
						<h:outputText value="#{consumer.name}" />
					</h:outputLink>
				</h:column>
				<h:column>
					<h:outputText value="#{consumer.description}" />
					<f:verbatim><br/></f:verbatim>
					<h:outputText value="#{msgs.last_updated}"/>
					<h:outputFormat value="#{consumer.age}">
					</h:outputFormat>
				</h:column>
				<h:column>
					<h:commandButton value="#{msgs.remove}" action="#{oauth.remove}" />
				</h:column>
			</h:dataTable>

			<h:outputText value="#{msgs.noconsumers}"
				rendered="#{empty oauth.consumers}" />

		</h:form>
	</sakai:view>
</f:view>