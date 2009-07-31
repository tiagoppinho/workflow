<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

		
<bean:define id="queueId" name="bean" property="queue.OID"/>
		
<fr:form id="form" action="/metaWorkflowQueueManagement.do">
<html:hidden  property="method" value="editQueue"/>
<html:hidden property="queueId" value="<%= queueId.toString() %>"/>
<fr:edit id="queue" name="bean" visible="false"/>



<table>
	<tr>
		<th><bean:message key="label.queue.name" bundle="META_WORKFLOW_RESOURCES"/></th>
		<td colspan="2"><fr:edit name="bean" slot="name"/></td>
	</tr>
	<tr>
		<th>
			<bean:message key="label.addUser" bundle="MYORG_RESOURCES"/>
		</th>
		<td>
			<fr:edit id="users" name="bean" slot="userToAdd">
				<fr:layout name="autoComplete">
					<fr:property name="labelField" value="username" />
					<fr:property name="format" value="${presentationName}" />
					<fr:property name="minChars" value="3" />
					<fr:property name="args"
						value="provider=myorg.presentationTier.renderers.autoCompleteProvider.UserAutoComplete" />
					<fr:property name="size" value="40" />
				</fr:layout>
				<fr:destination name="cancel" path="/metaWorkflowQueueManagement.do?method=manageQueues"/>
			</fr:edit>
		</td>
		<td>
			<input id="addUser" type="button"  value="+" class="inputbutton"/> </input>
		</td>
	</tr>
	<tr>
		<td colspan="3">
			<p>
				<strong> <bean:message key="label.workflowQueue.addedUsers" bundle="META_WORKFLOW_RESOURCES"/>:</strong>
			</p>
	
			<logic:notEmpty name="bean" property="users">
				<table class="structural mbottom05">
					<logic:iterate id="userAdded" name="bean" property="users" >
						<bean:define id="userId" name="userAdded" property="OID"/>
						
						<tr>
							<td><fr:view name="userAdded" property="presentationName"/></td>
							<td>(<html:link page="<%= "/metaWorkflowQueueManagement.do?method=removeUser&queueId=" + queueId + "&userId=" + userId%>"> 
								<bean:message key="link.remove" bundle="MYORG_RESOURCES"/>
							</html:link>)</td>
						</tr>
					</logic:iterate>
				</table>
			</logic:notEmpty>
			<logic:empty name="bean" property="users">
				<em><bean:message key="label.workflowQueue.noAddedUsers" bundle="META_WORKFLOW_RESOURCES"/></em>
			</logic:empty>
		</td>
	</tr>
</table>
			


<script type="text/javascript">
	$("#addUser").click(function(){
		$("#form > input[name='method']").attr('value','oneMoreUserInQueueInEdition');
		$("#form").submit();
	});
</script>
			
<html:submit styleClass="inputbutton"><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/></html:submit>
<html:cancel styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/></html:cancel>
</fr:form>

		
