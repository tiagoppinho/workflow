<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="title.dashboard" bundle="META_WORKFLOW_RESOURCES"/></h2>

<html:link page="/dashBoardManagement.do?method=doTest">
Teste
</html:link>

<logic:present name="user">
	<table class="structural">
		<tr>
			<td>
				<logic:present name="displayProcesses">
					<bean:message key="label.activeProcesses" bundle="META_WORKFLOW_RESOURCES"/> | 
					<html:link page="/metaWorkflow.do?method=viewOwnProcesses">
						<bean:message key="label.myProcesses" bundle="META_WORKFLOW_RESOURCES"/>
					</html:link>
				</logic:present>
					
				<logic:present name="myProcess">	
					<html:link page="/metaWorkflow.do?method=metaProcessHome">
						<bean:message key="label.activeProcesses" bundle="META_WORKFLOW_RESOURCES"/> 
					</html:link>
					| 
					<bean:message key="label.myProcesses" bundle="META_WORKFLOW_RESOURCES"/>
				</logic:present>
				</td>
			<td>
				<span style="float: right;">
					<fr:form action="/metaWorkflow.do?method=search">
							<fr:edit id="searchQuery" name="searchBean" slot="string" type="java.lang.String">
								<fr:layout>
									<fr:property name="size" value="40"/>
								</fr:layout>
							</fr:edit>
							<html:submit styleClass="inputbutton"><bean:message key="link.search" bundle="MYORG_RESOURCES"/></html:submit>	
					</fr:form>
				</span>
			</td>
		</tr>
	</table>

<table class="structural">
<tr>
<td>

<logic:present name="displayProcesses">
	<h3 class="mtop05 mbottom075"><bean:message key="label.activeProcesses" bundle="META_WORKFLOW_RESOURCES"/></h3>

	<logic:notEmpty name="displayProcesses">
		<div>
			<fr:view name="displayProcesses" schema="view.metaProcesses.list">
				<fr:layout name="tabular">
					<fr:property name="classes" value="table"/>
				</fr:layout>
			</fr:view>
		</div>
	</logic:notEmpty>
	
	<logic:empty name="displayProcesses">
		<em><bean:message key="label.queue.noProcessesForFilter" bundle="META_WORKFLOW_RESOURCES"/></em>
	</logic:empty>
	
</logic:present>

<logic:present name="myProcess">
	<h3 class="mtop05 mbottom075"><bean:message key="label.myProcesses" bundle="META_WORKFLOW_RESOURCES"/></h3>
	
	<logic:notEmpty name="myProcess">
		<div>
			<fr:view name="myProcess" schema="view.metaProcesses.list.withState">
				<fr:layout name="tabular">
					<fr:property name="classes" value="table"/>
				</fr:layout>
			</fr:view>
		</div>
	</logic:notEmpty>
	
	<logic:empty name="myProcess">
		<em><bean:message key="label.queue.noProcessesForFilter" bundle="META_WORKFLOW_RESOURCES"/></em>
	</logic:empty>
</logic:present>

</td>


<td style="padding-left: 1em;">

<logic:notEmpty name="availableQueues">
	<h3 class="mtop05 mbottom075"><bean:message key="label.queues" bundle="META_WORKFLOW_RESOURCES"/></h3>
	
	<div>
		<table class="table">
			<tr>
				<th><bean:message key="label.queue" bundle="META_WORKFLOW_RESOURCES"/></th>
				<th><bean:message key="label.queue.openProcesses" bundle="META_WORKFLOW_RESOURCES"/></th>
				<th><bean:message key="label.queue.closeProcesses" bundle="META_WORKFLOW_RESOURCES"/></th>
			</tr>
				<logic:iterate id="queue" name="availableQueues" indexId="i">
					<tr>
					<td>
						<bean:define id="queueId" name="queue" property="OID"/>
						<html:link page="<%= "/metaWorkflow.do?method=viewProcessInQueue&queueId=" +  queueId %>" > 
							<fr:view name="queue" property="name"/>
						</html:link>
						</td>
						<td>
							<html:link page="<%= "/metaWorkflow.do?method=viewProcessInQueue&active=true&queueId=" +  queueId %>" > 
								<fr:view name="queue" property="activeProcessCount"/>
							</html:link>
						</td>
						<td>
							<html:link page="<%= "/metaWorkflow.do?method=viewProcessInQueue&active=false&queueId=" +  queueId %>" > 
								<fr:view name="queue" property="notActiveProcessCount"/>
							</html:link>
						</td>
					</tr>
				</logic:iterate>	
		</table>
	</div>
</logic:notEmpty>

</td>
</tr>
</table>

</logic:present>
