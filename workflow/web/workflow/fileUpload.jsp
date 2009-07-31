<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@page import="module.workflow.presentationTier.WorkflowLayoutContext"%>
<%@page import="myorg.presentationTier.actions.ContextBaseAction"%>

<h2><bean:message key="title.uploadFile" bundle="WORKFLOW_RESOURCES"/></h2>

<bean:define id="process" name="process" toScope="request"/>
<bean:define id="processOID" name="process" property="OID" toScope="request"/>

<bean:define id="selectedInstance" name="bean" property="selectedInstance.simpleName"/>

<bean:define id="schema" value="<%= "addFile-" + selectedInstance%>" toScope="request"/>


<%
	final WorkflowLayoutContext layoutContext = (WorkflowLayoutContext) ContextBaseAction.getContext(request);
%>

<jsp:include page='<%= layoutContext.getWorkflowShortBody() %>'/>

<bean:define id="urlView">/workflowProcessManagement.do?method=viewProcess&amp;processId=<bean:write name="process" property="OID"/></bean:define>
<bean:define id="urlPostBack">/workflowProcessManagement.do?method=uploadPostBack&amp;processId=<bean:write name="process" property="OID"/></bean:define>


<fr:edit name="bean" id="uploadFile" action='<%= "workflowProcessManagement.do?method=upload&processId=" + processOID %>' schema="<%= schema %>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%= urlView %>" />
	<fr:destination name="postBack"  path="<%= urlPostBack %>"/>
</fr:edit>