<%@page import="module.workflow.domain.WorkflowProcess"%>
<%@page import="myorg.util.BundleUtil"%>
<%@page import="java.net.URLEncoder"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page import="module.workflow.presentationTier.WorkflowLayoutContext"%>
<%@page import="myorg.presentationTier.actions.ContextBaseAction"%>

<logic:empty name="processesWithUnreadComments">
	<h2>Não existem comentários novos</h2>
	<html:link action="/dashBoard.do">Voltar ao ecrã de resumo</html:link>

</logic:empty>

<logic:notEmpty name="processesWithUnreadComments" >
	<h2><bean:message bundle="WORKFLOW_RESOURCES" key="label.processes.with.unread.comments" />:</h2>
	<%Class oldProcessClass = null;%>
	
	<style>
		div.comment {
		background: #f5f5f5;
		border: 1px solid #ddd;
		padding: 10px;
		}
		td.photoHolder {
		padding: 5px 0 0 0 !important;
		}
		
		div.process-id {
		margin: 10px 0 10px 0;
		}
		div.process-id h2, div.process-id h3, div.process-id h4 {
		display: inline;
		font-size: 11px;
		}
		
		div.process-id .processNumber {
		font-weight: normal;
		}
	
	</style>
	<logic:iterate id="process" name="processesWithUnreadComments" indexId="processId" >
	<% if (oldProcessClass == null || !oldProcessClass.equals(process.getClass()))
	{
	     oldProcessClass = process.getClass();
	     %>
	     	<h3 class="mtop1 mbottom1"><%=BundleUtil.getLocalizedNamedFroClass(process.getClass())%></h3>
	     
	<% }
	   %>
	   
	<div id="<%="processId-"+processId%>" class="process-id">
			<% 
			WorkflowLayoutContext layoutContext = WorkflowLayoutContext.getDefaultWorkflowLayoutContext((WorkflowProcess) process);
			request.setAttribute("process", process);
			%>
		<jsp:include page='<%= layoutContext.getWorkflowHead() %>'/>
		<bean:define id="oid" name="process" property="externalId" type="java.lang.String"/>
		| <html:link page="<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + oid %>"><bean:message bundle="WORKFLOW_RESOURCES" key="unreadCommentsWidget.label.showProcess"/></html:link>
	</div>
		
	<fr:view name="process" >
		<fr:layout name="viewUnreadComments">
			<fr:property name="commentBlockClasses" value="comment"/>
		</fr:layout>
	</fr:view>
	<p style="margin-top: -10px;" class="aright"><html:link action="<%="/workflowWidgetActions.do?method=markCommentsAsRead&processClass="+request.getParameter("processClass")+"&processId="+oid+"#"+URLEncoder.encode("processId-")+processId.intValue()%>"><bean:message bundle="WORKFLOW_RESOURCES" key="unreadCommentsWidget.label.markAsRead"/></html:link></p>
					
	</logic:iterate>
</logic:notEmpty>
