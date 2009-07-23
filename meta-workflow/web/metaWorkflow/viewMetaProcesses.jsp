
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="label.create.metaProcess" bundle="META_WORKFLOW_RESOURCES"/></h2>


<div>
	<fr:form action="/metaWorkflow.do?method=search">
		<fr:edit id="searchQuery" name="searchBean" slot="string" type="java.lang.String" />
		<html:submit>Procurar</html:submit>	
	</fr:form>
</div>	
	
	<logic:notEmpty name="searchResult">
	<div class="infoop2 roundCorners">
		<table>
			<tr>
				<th><bean:message key="label.metaProcess.subject" bundle="META_WORKFLOW_RESOURCES"/></th>
				<th><bean:message key="label.metaProcess.type" bundle="META_WORKFLOW_RESOURCES"/></th>
				<th><bean:message key="label.processNumber" bundle="WORKFLOW_RESOURCES"/></th>
				<th><bean:message key="label.metaProcess.currentQueue" bundle="META_WORKFLOW_RESOURCES"/></th>
				<th><bean:message key="label.metaProcess.requestor" bundle="META_WORKFLOW_RESOURCES"/></th>
				<th> Taken by (?) </th> 
			</tr>
			<logic:iterate id="process" name="searchResult" indexId="i">
				<logic:equal name="process" property="accessibleToCurrentUser" value="true">
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
				</logic:equal>
			</logic:iterate>	
		</table>
	</div>
	
	</logic:notEmpty>






<table class="mtlist">
<tr>
<td>

<h3 class="mtop05 mbottom05">Processos Activos</h3>

<div>
	<table>
		<tr>
			<th><bean:message key="label.metaProcess.subject" bundle="META_WORKFLOW_RESOURCES"/></th>
			<th><bean:message key="label.metaProcess.type" bundle="META_WORKFLOW_RESOURCES"/></th>
			<th><bean:message key="label.processNumber" bundle="WORKFLOW_RESOURCES"/></th>
			<th><bean:message key="label.metaProcess.currentQueue" bundle="META_WORKFLOW_RESOURCES"/></th>
			<th><bean:message key="label.metaProcess.requestor" bundle="META_WORKFLOW_RESOURCES"/></th>
			<th>Taken by (?)</th> 
		</tr>
		<logic:iterate id="process" name="processes" indexId="i">
			<logic:equal name="process" property="accessibleToCurrentUser" value="true">
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
			</logic:equal>
		</logic:iterate>	
	</table>
</div>

<logic:present name="user">

<h3 class="mbottom05">Os meus processos</h3>

<div>
	<table>
		<tr>
			<th><bean:message key="label.metaProcess.subject" bundle="META_WORKFLOW_RESOURCES"/></th>
			<th><bean:message key="label.metaProcess.type" bundle="META_WORKFLOW_RESOURCES"/></th>
			<th><bean:message key="label.processNumber" bundle="WORKFLOW_RESOURCES"/></th>
			<th><bean:message key="label.metaProcess.currentQueue" bundle="META_WORKFLOW_RESOURCES"/></th>
			<th><bean:message key="label.metaProcess.requestor" bundle="META_WORKFLOW_RESOURCES"/></th>
			<th>Taken by (?)</th> 
		</tr>
		<logic:iterate id="process" name="user" property="metaProcesses" indexId="i">
			<logic:equal name="process" property="accessibleToCurrentUser" value="true">
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
			</logic:equal>
		</logic:iterate>	
	</table>
</div>
</logic:present>

</td>


<td style="padding-left: 1em;">

<h3 class="mtop05 mbottom05">Filas</h3>

<div>
	<table>
		<tr>
			<th><bean:message key="label.queue" bundle="META_WORKFLOW_RESOURCES"/></th>
			<th><bean:message key="label.queue.openProcesses" bundle="META_WORKFLOW_RESOURCES"/></th>
			<th><bean:message key="label.queue.closeProcesses" bundle="META_WORKFLOW_RESOURCES"/></th>
		</tr>
		<logic:present name="user">
			<logic:iterate id="queue" name="user" property="queues" indexId="i">
				<tr>
				<td>
					<bean:define id="queueId" name="queue" property="OID"/>
					<html:link page="<%= "/metaWorkflow.do?method=viewProcessInQueue&queueId=" +  queueId %>" > 
						<fr:view name="queue" property="name"/>
					</html:link>
					</td>
					<td>
						<fr:view name="queue" property="activeProcessCount"/>
					</td>
					<td>
						<fr:view name="queue" property="notActiveProcessCount"/>
					</td>
				</tr>
			</logic:iterate>	
		</logic:present>
		<logic:notPresent name="user">
			<tr><td> - </td></tr>
		</logic:notPresent>
	</table>
</div>
</td>
</tr>
</table>


