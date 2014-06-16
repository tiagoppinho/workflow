<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.Map"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>

<bean:define id="dashBoardId" name="widget" property="dashBoardPanel.externalId"/>
<bean:define id="widgetId" name="widget" property="externalId"/>

<bean:define id="optionsEdit" value="edit-widgetOptions-${widgetId}" type="java.lang.String" toScope="page"/>
<bean:define id="optionsView" value="widgetOptions-${widgetId}" type="java.lang.String" toScope="page"/>

<%boolean unreadCommentsExist = false; %>

<logic:notPresent name="<%= optionsEdit %>" scope="request">
	<logic:notEmpty name="numberUnreadCommentsPerProcess">
		<table class="width100pc">
			<logic:iterate id="process" name="numberUnreadCommentsPerProcess" indexId="numberOfProcessType">
						<%String nameForProcessClass = module.workflow.util.WorkflowClassUtil.getNameForType(((Map.Entry<Class,Integer>) process).getKey());%>
				<tr>
					<td>
						<%=nameForProcessClass.toString()%>
					</td>
					<td>
					<%-- if the value is 0, we shouldn't make it a link --%>
					<%if (!((Map.Entry<Class,Integer>) process).getValue().equals(Integer.valueOf(0))) { 
					unreadCommentsExist = true;%>
						<html:link page="<%=\"/workflowWidgetActions.do?method=viewListUnreadComments&processClass=\"+java.net.URLEncoder.encode(((Map.Entry<Class,Integer>) process).getKey().toString())+\"#\"+ java.net.URLEncoder.encode(((Map.Entry<Class,Integer>) process).getKey().toString())%>">
					<%=((Map.Entry<Class,Integer>) process).getValue().toString() %>
						</html:link>
						<%} else { %>
					<%=((Map.Entry<Class,Integer>) process).getValue().toString() %>
						<% }%>
					</td>
				</tr>
			</logic:iterate>
			<% if (unreadCommentsExist) {%>
			<tr>
				<td colspan="2" class="aright" style="padding-bottom: 8px !important;">
				<html:link page="/workflowWidgetActions.do?method=viewListUnreadComments">
					<bean:message key="label.viewAll" bundle="WORKFLOW_RESOURCES"/>
				</html:link>
				</td>			
			</tr>
			<% }%>
		</table>
	</logic:notEmpty>
	<%-- code outdated because there array is always non empty  though it can be useful if the behaviour changes in the future so that the list only contains processes with unread comments
	<logic:empty name="numberUnreadCommentsPerProcess">
		<p><em><bean:message key="label.no.processesWithUnreadComments" bundle="WORKFLOW_RESOURCES"/>.</em></p>
	</logic:empty>
	--%>
</logic:notPresent>
