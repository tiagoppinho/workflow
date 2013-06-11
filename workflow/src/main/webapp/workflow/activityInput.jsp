<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@page import="module.workflow.presentationTier.WorkflowLayoutContext"%>
<%@page import="pt.ist.bennu.core.presentationTier.actions.ContextBaseAction"%><bean:define id="process" name="information" property="process" toScope="request"/>

<bean:define id="processId" name="process" property="externalId"  type="java.lang.String"/>
<bean:define id="name" name="information" property="activityName"/>
<bean:define id="activityInformationSchema" name="information" property="usedSchema" type="java.lang.String"/>
<%
	final WorkflowLayoutContext layoutContext = (WorkflowLayoutContext) ContextBaseAction.getContext(request);
%>
<jsp:include page='<%=  layoutContext.getWorkflowHead() %>'/>
<h3><fr:view name="information" property="localizedName" layout="html"/></h3>


<jsp:include page='<%= layoutContext.getWorkflowShortBody() %>'/>

<logic:messagesPresent property="message" message="true">
	<div class="error1">
		<html:messages id="errorMessage" property="message" message="true"> 
			<span><fr:view name="errorMessage"/></span>
		</html:messages>
	</div>
</logic:messagesPresent>
<fr:edit id="activityBean" name="information" action='<%="/workflowProcessManagement.do?method=process&activity=" + name + "&processId=" + processId%>'
schema='<%= activityInformationSchema %>'>
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
		<fr:property name="columnClasses" value=",,tderror"/>
		<fr:property name="requiredMarkShown" value="true"/>
	</fr:layout>
	<fr:destination name="cancel" path='<%="/workflowProcessManagement.do?method=viewProcess&processId=" + processId%>'/>
	<fr:destination name="postback" path='<%= "/workflowProcessManagement.do?method=activityDefaultPostback&processId=" + processId%>'/>
</fr:edit>
