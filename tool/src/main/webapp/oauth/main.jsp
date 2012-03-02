<%@ taglib uri="http://sakaiproject.org/jsf/sakai" prefix="sakai" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<f:view>
    <sakai:view title="Trusted Applications">
        <h:form id="consumers">
            <sakai:instruction_message
                    value="Here you can manage the applications that you wish to allow access to your account."/>

            <sakai:messages/>
            <h3><h:outputText value="List of Applications."/></h3>
            <h:dataTable value="#{oauth.accessors}" var="accessor"
                         binding="#{oauth.accessorTable}">
                <h:column>
                    <h:outputLink value="#{accessor.consumerUrl}" target="_top">
                        <h:outputText value="#{accessor.consumerName}"/>
                    </h:outputLink>
                </h:column>
                <h:column>
                    <h:outputText value="#{accessor.consumerDescription}"/>
                    <f:verbatim><br/></f:verbatim>
                    <h:outputText value="Created:"/>
                    <h:outputFormat value="#{accessor.creationDate}">
                    </h:outputFormat>
                    <f:verbatim><br/></f:verbatim>
                    <h:outputText value="Expires on:"/>
                    <h:outputFormat value="#{accessor.expirationDate}">
                    </h:outputFormat>
                </h:column>
                <h:column>
                    <h:commandButton value="Remove" action="#{oauth.remove}"/>
                </h:column>
            </h:dataTable>

            <h:outputText value="You currently don't have any applications able to access your account."
                          rendered="#{empty oauth.accessors}"/>
        </h:form>
    </sakai:view>
</f:view>
