<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<fr:form id="form" action="/workflowQueueManagement.do">
<html:hidden  property="method" value="createNewQueue"/>
<fr:edit id="newQueue" name="bean" visible="false"/>

	
<table class="mtop15">
	<tr>
		<th><bean:message key="label.queue.name" bundle="WORKFLOW_RESOURCES"/></th>
		<td colspan="2"><fr:edit name="bean" slot="name" >
				<fr:layout>
					<fr:property name="size" value="40"/>
				</fr:layout>
			</fr:edit>
		</td>
	</tr>
	<tr>
		<th>
			<bean:message key="label.addUser" bundle="MYORG_RESOURCES"/>
		</th>
		<td>
			<fr:edit id="users" name="bean" slot="userToAdd">
				<fr:layout name="autoComplete">
					<fr:property name="labelField" value="username" />
					<fr:property name="format" value="\${displayName}" />
					<fr:property name="minChars" value="3" />
					<fr:property name="provider"
						value="org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.UserAutoComplete" />
					<fr:property name="size" value="40" />
				</fr:layout>
				<fr:destination name="cancel" path="/workflowQueueManagement.do?method=manageQueues"/>
			</fr:edit>
		</td>
		<td>
			<input id="addUser" type="button"  value="+" class="inputbutton"/> </input>
		</td>
	</tr>
	<tr>
		<td colspan="3">
			<p>
				<strong> <bean:message key="label.workflowQueue.addedUsers" bundle="WORKFLOW_RESOURCES"/>:</strong>
			</p>
			
			<logic:notEmpty name="bean" property="users">
				<ul>
					<logic:iterate id="userAdded" name="bean" property="users" >
						<li><fr:view name="userAdded" property="profile.fullName"/></li>
					</logic:iterate>
				</ul>
			</logic:notEmpty>
			<logic:empty name="bean" property="users">
				<em><bean:message key="label.workflowQueue.noAddedUsers" bundle="WORKFLOW_RESOURCES"/></em>
			</logic:empty>
		</td>
	</tr>
</table>

<script type="text/javascript">
	$("#addUser").click(function(){
		$("#form > input[name='method']").attr('value','oneMoreUserInQueueInCreation');
		$("#form").submit();
	});
</script>
<html:submit styleClass="inputbutton"><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/></html:submit>
<html:cancel styleClass="inputbutton cancel"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/></html:cancel>

</fr:form>
		
