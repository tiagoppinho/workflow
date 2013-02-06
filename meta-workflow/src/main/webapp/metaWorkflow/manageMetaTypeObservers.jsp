<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<script src="<%= request.getContextPath() + "/javaScript/jquery.alerts.js"%>" type="text/javascript"></script> 
<script src="<%= request.getContextPath() + "/javaScript/alertHandlers.js"%>" type="text/javascript"></script> 


<html:link page="/metaWorkflow.do?method=manageMetaType">
	<bean:message key="link.back" bundle="MYORG_RESOURCES"/>
</html:link>

<bean:define id="metaTypeID" name="metaType" property="externalId" type="java.lang.String"/>

<logic:iterate id="observer" name="metaType" property="metaTypeObservers">			
		
		<bean:define id="userPresentation" name="observer" property="presentationName"/>
		<bean:define id="userID" name="observer" property="externalId" type="java.lang.String"/>
		
		<p>
		<span>
			<fr:view name="userPresentation"/>
			<html:link styleId="<%= "remove-" + userID %>" page="<%= "/metaWorkflow.do?method=removeMetaTypeObserver&metaTypeId=" + metaTypeID + "&userId=" + userID %>">
				<bean:message bundle="MYORG_RESOURCES" key="link.remove"/>
			</html:link>
			<script type="text/javaScript">
			 linkConfirmationHook("<%= "remove-" + userID %>", '<bean:message key="label.confirm.removal" bundle="META_WORKFLOW_RESOURCES" arg0="<%= userPresentation.toString() %>"/>', ' <bean:message key="title.removeObserver" bundle="META_WORKFLOW_RESOURCES"/>');
			</script>
		</span>
		</p>
	</logic:iterate>


<fr:edit id="userToAdd" name="bean" slot="domainObject" action="<%= "/metaWorkflow.do?method=addMetaTypeObserver&metaTypeId=" + metaTypeID%>">
	<fr:layout name="autoComplete">
		<fr:property name="labelField" value="username" />
		<fr:property name="format" value="${presentationName}" />
		<fr:property name="minChars" value="3" />
		<fr:property name="args"
			value="provider=pt.ist.bennu.core.presentationTier.renderers.autoCompleteProvider.UserAutoComplete" />
		<fr:property name="size" value="40" />
	</fr:layout>
</fr:edit>

