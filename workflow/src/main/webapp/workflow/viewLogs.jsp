<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<%@page import="module.workflow.presentationTier.WorkflowLayoutContext"%>
<%@page import="pt.ist.bennu.core.presentationTier.actions.ContextBaseAction"%>
<%
	final WorkflowLayoutContext layoutContext = (WorkflowLayoutContext) ContextBaseAction.getContext(request);
%>
<jsp:include page='<%=  layoutContext.getWorkflowHead() %>'/>

<h3><bean:message key="title.viewLogs" bundle="WORKFLOW_RESOURCES"/></h3>



<p class="mtop05 mbottom15">
	<html:link action="/workflowProcessManagement.do?method=viewProcess" paramId="processId" paramName="process" paramProperty="externalId">
		«  <bean:message key="link.backToProcess" bundle="WORKFLOW_RESOURCES"/>
	</html:link>
</p>


<jsp:include page='<%= layoutContext.getWorkflowShortBody() %>'/>


<logic:empty name="operationLogs">
	<p>
		<em><bean:message key="label.noLogs" bundle="WORKFLOW_RESOURCES"/>.</em>
	</p>
</logic:empty>


<fr:view name="operationLogs" schema="viewLogs">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 mtop1 noraquo"/>
		<fr:property name="columnClasses" value="smalltxt, aleft, aleft"/>
		<fr:property name="sortBy" value="whenOperationWasRan"/>
	</fr:layout>
</fr:view>
