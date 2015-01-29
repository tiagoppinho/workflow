<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2>
	<bean:message key="label.queueDetails" bundle="WORKFLOW_RESOURCES"/>
</h2>

<ul>
	<li>
		<html:link page="/workflowQueueManagement.do?method=manageQueues">
			<bean:message key="link.back" bundle="MYORG_RESOURCES"/>
		</html:link>
	</li>
</ul>


<table class="table tstyle2 mtop15 mbottom15">
	<tr>
		<th><bean:message key="label.queue.name" bundle="WORKFLOW_RESOURCES"/></th>
		<th><bean:message key="label.queue.type" bundle="WORKFLOW_RESOURCES"/></th>
	</tr>
	<tr>
		<td><fr:view name="queue" property="name"/></td>
		<td><fr:view name="queue" property="class" layout="name-resolver"/></td>
	</tr>
</table>

<jsp:include page="${context.viewQueue}"/>
