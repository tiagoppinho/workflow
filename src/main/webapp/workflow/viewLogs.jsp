<%@page import="java.util.Collection"%>
<%@page import="java.util.TreeSet"%>
<%@page import="module.workflow.domain.WorkflowLog"%>
<%@page import="java.util.SortedSet"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<jsp:include page='${context.workflowHead}'/>

<h3><bean:message key="title.viewLogs" bundle="WORKFLOW_RESOURCES"/></h3>



<p class="mtop05 mbottom15">
	<html:link action="/workflowProcessManagement.do?method=viewProcess" paramId="processId" paramName="process" paramProperty="externalId">
		«  <bean:message key="link.backToProcess" bundle="WORKFLOW_RESOURCES"/>
	</html:link>
</p>


<jsp:include page='${context.workflowShortBody}'/>


<logic:empty name="operationLogs">
	<p>
		<em><bean:message key="label.noLogs" bundle="WORKFLOW_RESOURCES"/>.</em>
	</p>
</logic:empty>

<table class="tstyle2 mtop1 noraquo table">
    <tr>
        <th>
            <bean:message key="label.whenOperationWasRan" bundle="WORKFLOW_RESOURCES"/>
        </th>
        <th>
            <bean:message key="label.description" bundle="WORKFLOW_RESOURCES"/>
        </th>
        <th>
            <bean:message key="label.activityExecutor" bundle="WORKFLOW_RESOURCES"/>
        </th>
    </tr>
    <%
        final SortedSet logs = new TreeSet(WorkflowLog.COMPARATOR_BY_WHEN);
        logs.addAll((Collection) request.getAttribute("operationLogs"));
        for (final Object o : logs) {
            WorkflowLog operationLog = (WorkflowLog) o;
            request.setAttribute("operationLog", operationLog);
    %>
            <tr>
                <td class="smalltxt">
                    <%= operationLog.getWhenOperationWasRan().toString("yyyy-MM-dd HH:mm") %>
                </td>
                <td class="aleft">
                    <bean:write name="operationLog" property="description" filter="true"/>
                </td>
                <td class="aleft">
                    <%= operationLog.getActivityExecutor().getDisplayName() %>
                    (<%= operationLog.getActivityExecutor().getUsername() %>)
                    <% if (operationLog.getUsedAuthenticationMethods().contains("OAuth Access Token")) { %>
                        <br>
                        <small style="color: gray;"><bean:message key="label.via.remote.system" bundle="WORKFLOW_RESOURCES"/></small>
                    <% } %>
                </td>
            </tr>
    <%
        }
    %>
</table>
