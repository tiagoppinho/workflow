<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="module.workflow.presentationTier.WorkflowLayoutContext"%>
<%@page import="myorg.presentationTier.actions.ContextBaseAction"%><h2><bean:message key="title.removedFiles" bundle="WORKFLOW_RESOURCES"/></h2>


<p class="mtop05 mbottom15">
	<html:link page="/workflowProcessManagement.do?method=viewProcess" paramId="processId" paramName="process" paramProperty="externalId">
		« <bean:message key="link.backToProcess" bundle="WORKFLOW_RESOURCES"/>
	</html:link>
</p>


<%
	final WorkflowLayoutContext layoutContext = (WorkflowLayoutContext) ContextBaseAction.getContext(request);
%>

<jsp:include page='<%= layoutContext.getWorkflowShortBody() %>'/>

<ul class="mtop15">
	<logic:iterate id="file" name="process" property="deletedFiles">
		<li>
			<bean:define id="fileId" name="file" property="externalId" type="java.lang.String"/>
			<fr:view name="file" property="displayName"/> <html:link page='<%= "/workflowProcessManagement.do?method=downloadFile&fileId=" + fileId %>' paramId="processId" paramName="process" paramProperty="externalId"><bean:message key="link.downloadFile" bundle="WORKFLOW_RESOURCES"/></html:link> 
		</li>
	</logic:iterate>
</ul>