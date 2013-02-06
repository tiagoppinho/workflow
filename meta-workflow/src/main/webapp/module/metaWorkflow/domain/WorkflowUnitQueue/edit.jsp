<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

		
<fr:form id="form" action="/workflowQueueManagement.do">
<html:hidden  property="method" value="editQueue"/>
<fr:edit id="queue" name="bean" visible="false"/>


	<table>
		<tr>
			<th><bean:message key="label.queue.name" bundle="WORKFLOW_RESOURCES"/></th>
			<td><fr:edit name="bean" slot="name"/></td>
		</tr>
		<tr>
			<th>
				<bean:message key="label.accountabilityTypes" bundle="ORGANIZATION_RESOURCES"/>:
			</th>
			<td>
				<fr:edit id="accountabilityTypes" name="bean" slot="accountabilityTypes">
					<fr:layout name="option-select">
						<fr:property name="providerClass" value="module.organization.presentationTier.renderers.providers.AccountabilityTypesProvider"/>
						<fr:property name="eachLayout" value="values"/>
						<fr:property name="eachSchema" value="module.organization.domain.AccountabilityType.name"/>
					</fr:layout>
					<fr:destination name="cancel" path="/workflowQueueManagement.do?method=manageQueues"/>
				</fr:edit>
			</td>
		</tr>
	</table>	


<html:submit styleClass="inputbutton"><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/></html:submit>
<html:cancel styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/></html:cancel>
</fr:form>
		
