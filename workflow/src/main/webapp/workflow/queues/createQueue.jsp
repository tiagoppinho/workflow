<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<%@page import="module.workflow.presentationTier.WorkflowQueueLayoutContext"%><h2><bean:message key="title.createQueue" bundle="WORKFLOW_RESOURCES"/></h2>

<ul>
	<li>
		<html:link page="/workflowQueueManagement.do?method=manageQueues">
			<bean:message key="link.back" bundle="MYORG_RESOURCES"/>
		</html:link>
	</li>
</ul>

<fr:form action="/workflowQueueManagement.do">
	<table>
		<tr>
		<th>
			<bean:message key="label.queue.type" bundle="WORKFLOW_RESOURCES"/>
		</th>
		<td>
		<fr:edit id="queueType" name="bean" slot="queueType">
			<fr:layout name="menu-select-postback">
				<fr:property name="providerClass" value="module.workflow.presentationTier.renderers.providers.AvailableQueueTypes"/>
				<fr:property name="eachLayout" value="name-resolver"/>
			</fr:layout>
			<fr:destination name="postback" path="/workflowQueueManagement.do?method=selectQueueType"/>
		</fr:edit>
		</td>
		</tr>
	</table>
</fr:form>

<logic:present name="bean" property="queueType">
	<bean:define id="xpto" name="creationPage"/>
	<jsp:include page="<%= xpto.toString() %>"/>
</logic:present>
