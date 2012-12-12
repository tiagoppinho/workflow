<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="processId" name="information" property="process.externalId" type="java.lang.String"/>
<bean:define id="name" name="information" property="activityName"/>

<fr:form id="submitForm" action="<%= "/workflowProcessManagement.do?method=process&processId=" + processId + "&activity=" + name %>" >
	<fr:edit id="activityBean" name="information" visible="false"/>
	
	<fr:edit id="fieldBean" name="information" property="fieldBean">
		<fr:schema type="module.metaWorkflow.domain.FieldValue$FieldValueBean" bundle="META_WORKFLOW_RESOURCES">
			<fr:slot name="dateTimeValue" key="label.metaProcess.field.value" layout="picker" validator="pt.ist.fenixWebFramework.rendererExtensions.validators.DateTimeValidator"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="columnClasses" value=",,tderror" />
		</fr:layout>
	</fr:edit>
	
	<html:submit styleClass="inputbutton"><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/> </html:submit>
</fr:form>

<fr:form id="cancelForm" action='<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + processId %>'>
	<html:submit styleClass="inputbutton"><bean:message key="renderers.form.cancel.name" bundle="RENDERER_RESOURCES"/> </html:submit>
</fr:form>
