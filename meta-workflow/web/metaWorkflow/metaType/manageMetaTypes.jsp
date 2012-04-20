<%@page import="module.workflow.domain.ProcessFile"%>
<%@page import="java.util.Iterator"%>
<%@page import="myorg.util.BundleUtil"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<h2><bean:message key="label.metaType.managament" bundle="META_WORKFLOW_RESOURCES"/></h2>

<ul>
	<li>
		<html:link page="/metaTypeManagement.do?method=prepareCreateMetaType">
			<bean:message key="link.metaType.create" bundle="META_WORKFLOW_RESOURCES"/>
		</html:link>
	</li>
</ul>

<table class="tstyle3">
	<tr>
		<th><bean:message key="label.metaType.name" bundle="META_WORKFLOW_RESOURCES"/></th>
		<th><bean:message key="label.organizational.structure" bundle="ORGANIZATION_RESOURCES"/></th>
		<th><bean:message key="label.metaType.availableFileTypes" bundle="META_WORKFLOW_RESOURCES"/></th>
		<th></th>
	</tr>
	<logic:iterate id="metaType" name="metaTypes" type="module.metaWorkflow.domain.WorkflowMetaType">
		<tr>
			<td>
				<html:link action="<%= "/metaTypeManagement.do?method=viewMetaType&metaTypeId=" + metaType.getExternalId() %>">
					<%= metaType.getName() %>
				</html:link>
			</td>
			<td><%= metaType.getOrganizationalModel().getName() %></td>
			<td><%
					Iterator<java.lang.Class<? extends ProcessFile>> iterator = metaType.getAvailableFileTypes().iterator();
					while (iterator.hasNext()) {
				    	Class<? extends ProcessFile> fileType = iterator.next();
				    	%> <%= BundleUtil.getLocalizedNamedFroClass(fileType) %> <%
				    	if (iterator.hasNext()) {
							%> <%= ", " %> <%
				    	}
					}
				%></td>
			<td>
				<html:link action="<%= "/metaTypeManagement.do?method=manageFields&metaTypeId=" + metaType.getExternalId() %>">
					<bean:message key="link.manage.metaFields" bundle="META_WORKFLOW_RESOURCES"/>
				</html:link>, 
				<html:link action="<%= "/metaTypeManagement.do?method=manageQueues&metaTypeId=" + metaType.getExternalId() %>">
					<bean:message key="link.manageQueuesInMetaType" bundle="META_WORKFLOW_RESOURCES"/>
				</html:link>
			</td>
	</logic:iterate>
</table>

