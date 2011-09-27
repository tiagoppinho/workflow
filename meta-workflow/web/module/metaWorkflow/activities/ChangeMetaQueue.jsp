<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="process" name="information" property="process"/>
<bean:define id="processId" name="process" property="externalId" type="java.lang.String"/>
<bean:define id="name" name="information" property="activityName"/>

<p>

<div class="dinline forminline">
	<fr:form id="submitForm" action="<%= "/workflowProcessManagement.do?method=process&processId=" + processId + "&activity=" + name %>">
		<h3><bean:message key="label.queues.to.remove" bundle="META_WORKFLOW_RESOURCES" /></h3>
		
		<fr:edit id="activityBean" name="information" visible="false"/>
		
		<fr:edit id="queuesToRemove" name="information">
			<fr:schema type="module.workflow.activities.ChangeQueueInformation" bundle="MYORG_RESOURCES">
				<fr:slot name="queuesToRemove" layout="option-select" key="label.blank">
					<fr:property name="eachLayout" value="values"/>
					<fr:property name="eachSchema" value="queue.name"/>
					<fr:property name="providerClass" value="module.metaWorkflow.presentationTier.renderers.providers.CurrentProcessQueues" />
				</fr:slot>
				<fr:layout>
					<fr:property name="labelTerminator" value=""/>
				</fr:layout>
			</fr:schema>
		</fr:edit>
		
		<p>
		<h3><bean:message key="label.queues.to.add" bundle="META_WORKFLOW_RESOURCES" /></h3>
		
		<fr:edit id="queuesToAdd" name="information">
			<fr:schema type="module.workflow.activities.ChangeQueueInformation" bundle="MYORG_RESOURCES">
				<fr:slot name="queuesToAdd" layout="option-select" key="label.blank">
					<fr:property name="eachLayout" value="values"/>
					<fr:property name="eachSchema" value="queue.name"/>
					<fr:property name="providerClass" value="module.metaWorkflow.presentationTier.renderers.providers.AvailableQueuesToMoveTo" />
				</fr:slot>
				<fr:layout>
					<fr:property name="labelTerminator" value=""/>
				</fr:layout>
			</fr:schema>
		</fr:edit>
		
		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/> </html:submit>
	</fr:form>

	<fr:form id="cancelForm" action='<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + processId %>'>
		<html:submit styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/> </html:submit>
	</fr:form>
</div>