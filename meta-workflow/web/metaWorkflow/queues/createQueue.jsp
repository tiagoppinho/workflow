<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<%@page import="module.metaWorkflow.presentationTier.WorkflowQueueLayoutContext"%><h2><bean:message key="title.createQueue" bundle="META_WORKFLOW_RESOURCES"/></h2>

<ul>
	<li>
		<html:link page="/metaWorkflowQueueManagement.do?method=manageQueues">
			<bean:message key="link.back" bundle="MYORG_RESOURCES"/>
		</html:link>
	</li>
</ul>

<fr:form action="/metaWorkflowQueueManagement.do">
	<table>
		<tr>
		<th>
			<bean:message key="label.queue.type" bundle="META_WORKFLOW_RESOURCES"/>
		</th>
		<td>
		<fr:edit id="queueType" name="bean" slot="queueType">
			<fr:layout name="menu-select-postback">
				<fr:property name="providerClass" value="module.metaWorkflow.presentationTier.renderers.providers.AvailableQueueTypes"/>
				<fr:property name="eachLayout" value="name-resolver"/>
			</fr:layout>
			<fr:destination name="postback" path="/metaWorkflowQueueManagement.do?method=selectQueueType"/>
		</fr:edit>
		</td>
		</tr>
	</table>
</fr:form>

<logic:present name="bean" property="queueType">
	<bean:define id="xpto" name="creationPage"/>
	<jsp:include page="<%= xpto.toString() %>"/>
</logic:present>