
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="label.create.metaProcess" bundle="META_WORKFLOW_RESOURCES"/></h2>

<div class="infoop2 roundCorners">
	<table style="width: 100%">
		<tr>
			<th> <bean:message key="label.metaProcess.subject" bundle="META_WORKFLOW_RESOURCES"/> </th>
			<th> <bean:message key="label.metaProcess.type" bundle="META_WORKFLOW_RESOURCES"/> </th>
			<th> <bean:message key="label.processNumber" bundle="WORKFLOW_RESOURCES"/> </th>
			<th> <bean:message key="label.metaProcess.currentQueue" bundle="META_WORKFLOW_RESOURCES"/> </th>
			<th> <bean:message key="label.metaProcess.requestor" bundle="META_WORKFLOW_RESOURCES"/> </th>
			<th> Taken by (?) </th> 
		</tr>
		<logic:iterate id="process" name="processes" indexId="i">
			<tr>
			<td>
				<bean:define id="processId" name="process" property="OID"/>
				<html:link page="<%= "/workflowProcessManagement.do?method=viewProcess&processId=" +  processId %>" > 
					<fr:view name="process" property="subject"/>
				</html:link>
				</td>
			<td> <fr:view name="process" property="metaType.name"/></td>
			<td> <fr:view name="process" property="processNumber"/></td>
			<td class="acenter"> <fr:view name="process" property="currentQueue" type="module.metaWorkflow.domain.WorkflowQueue">
					<fr:layout name="null-as-label">
						<fr:property name="subLayout" value="values"/>
						<fr:property name="subSchema" value="view.queue.name"/>
					</fr:layout>
				 </fr:view> 
			</td>
			<td class="acenter"> <fr:view name="process" property="requestor" type="module.metaWorkflow.domain.Requestor">
					<fr:layout name="null-as-label">
						<fr:property name="subLayout" value="values"/>
						<fr:property name="subSchema" value="view.requestor.name"/>
					</fr:layout>
				 </fr:view> 
			</td>
			<td class="acenter"> <fr:view name="process" property="currentOwner" type="myorg.domain.User">
					<fr:layout name="null-as-label">
						<fr:property name="subLayout" value="values"/>
						<fr:property name="subSchema" value="view.user.presentationName"/>
					</fr:layout>
				 </fr:view> 
			</td>
			</tr>
		</logic:iterate>	
	</table>
</div>
