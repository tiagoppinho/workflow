<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

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

