<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<fr:form id="form" action="/workflowQueueManagement.do">
	<html:hidden  property="method" value="createNewQueue"/>
	<fr:edit id="newQueue" name="bean" visible="false"/>
	

	<fr:edit name="bean" schema="create.unitQueue">
		<fr:destination name="postBack" path="/workflowQueueManagement.do?method=doPostback"/>
		<fr:destination name="cancel" path="/workflowQueueManagement.do?method=manageQueues"/>
	</fr:edit>

	<html:submit styleClass="inputbutton"><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/></html:submit>
	<html:cancel styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/></html:cancel>

</fr:form>

