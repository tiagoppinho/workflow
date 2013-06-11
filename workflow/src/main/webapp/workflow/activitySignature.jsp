<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@page import="module.workflow.presentationTier.WorkflowLayoutContext"%>
<%@page import="pt.ist.bennu.core.presentationTier.actions.ContextBaseAction"%>

<bean:define id="process" name="information" property="process" toScope="request"/>
<bean:define id="processId" name="process" property="externalId"  type="java.lang.String"/>
<bean:define id="name" name="information" property="activityName"/>
<bean:define id="activityInformationClass" name="information" property="activityClass.simpleName"/>

<%
	final WorkflowLayoutContext layoutContext = (WorkflowLayoutContext) ContextBaseAction.getContext(request);
%>
<jsp:include page='<%=  layoutContext.getWorkflowHead() %>'/>
<h3><fr:view name="information" property="localizedName" layout="html"/></h3>

<jsp:include page='<%= layoutContext.getWorkflowShortBody() %>'/>

<fr:view name="signIntention" />

<html:link action="<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + processId  %>">
Finish?
</html:link>
