<%@page import="module.workflow.domain.WorkflowSystem"%>
<%@page import="pt.ist.bennu.core.domain.VirtualHost"%>
<%@page import="pt.ist.bennu.core.domain.MyOrg"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<%
	final VirtualHost virtualHost = VirtualHost.getVirtualHostForThread();
	final WorkflowSystem workflowSystem = virtualHost.getWorkflowSystem();
%>

<h2><bean:message key="link.topBar.configuration" bundle="WORKFLOW_RESOURCES"/></h2>

<h3><bean:message key="link.topBar.configuration.virtual.hosts" bundle="WORKFLOW_RESOURCES"/></h3>

<table class="tstyle2">
	<tr>
		<th>
			<bean:message key="link.topBar.configuration.virtual.hosts.title" bundle="WORKFLOW_RESOURCES"/>
		</th>
		<th>
			<bean:message key="link.topBar.configuration.virtual.hosts.system" bundle="WORKFLOW_RESOURCES"/>
		</th>
		<th>
		</th>
	</tr>
	<%
		for (final VirtualHost someVirtualHost : MyOrg.getInstance().getVirtualHostsSet()) {
	%>
			<tr>
				<td <% if (virtualHost == someVirtualHost) { %>style="background-color: #99FF66;"<% } %>>
					<%= someVirtualHost.getApplicationTitle() %>
					<br/>
					<%= someVirtualHost.getHostname() %>
				</td>
				<% 	final WorkflowSystem someWorkflowSystem = someVirtualHost.getWorkflowSystem();
					if (someWorkflowSystem != null) {
				%>
						<td <% if (virtualHost == someVirtualHost) { %>style="background-color: #99FF66;"<% } %>>
							<%= someWorkflowSystem.getExternalId() %>
						</td>
				<%
					} else {
				%>
						<td <% if (virtualHost == someVirtualHost) { %>style="background-color: #99FF66;"<% } %>>
							--
						</td>
				<%
					}
				%>
				<td <% if (virtualHost == someVirtualHost) { %>style="background-color: #99FF66;"<% } %>>
					<%
						if (someWorkflowSystem != null && someWorkflowSystem != workflowSystem) {
					%>
							<html:link action="<%= "/workflowConfiguration.do?method=useSystem&amp;systemId=" + someWorkflowSystem.getExternalId() %>">
								<bean:message key="link.topBar.configuration.virtual.hosts.use.system" bundle="WORKFLOW_RESOURCES"/>
							</html:link>
					<%
						}
						if (someVirtualHost == virtualHost) {
					%>
							<html:link action="/workflowConfiguration.do?method=createNewSystem">
								<bean:message key="link.topBar.configuration.virtual.hosts.create.new.system" bundle="WORKFLOW_RESOURCES"/>
							</html:link>
					<%
						}
					%>
				</td>
			</tr>
	<%
		}
	%>
</table>


