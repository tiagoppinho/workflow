<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="metaType" name="metaType" type="module.metaWorkflow.domain.WorkflowMetaType"></bean:define>
<bean:define id="fieldSet" name="metaType" property="fieldSet" type="module.metaWorkflow.domain.MetaFieldSet"></bean:define>

<logic:messagesPresent property="message" message="true">
	<div class="error1">
		<html:messages id="errorMessage" property="message" message="true">
			<span><fr:view name="errorMessage"/></span>
		</html:messages>
	</div>
</logic:messagesPresent>
<p>

<h2><bean:message key="link.manage.addField" bundle="META_WORKFLOW_RESOURCES"/></h2>

<h3><bean:message key="label.metaType" bundle="META_WORKFLOW_RESOURCES"/> - <fr:view name="metaType" property="name"/></h3>

<h3><bean:message key="label.fieldSet" bundle="META_WORKFLOW_RESOURCES"/> - <fr:view name="fieldSet" property="name.content"/></h3>

<p>

<fr:edit id="fieldBean" name="fieldBean" action="<%="/metaTypeManagement.do?method=addField&metaTypeId=" + metaType.getExternalId() + "&fieldSetId=" + fieldSet.getExternalId() %>">
	<fr:schema type="module.metaWorkflow.presentationTier.dto.MetaFieldBean" bundle="META_WORKFLOW_RESOURCES">
		<fr:slot name="fieldClass" layout="menu-select" required="true">
			<fr:property name="providerClass" value="module.metaWorkflow.presentationTier.provider.MetaFieldClassProvider"/>
			<fr:property name="eachSchema" value="metaField.class.fromLabel"/>
			<fr:property name="eachLayout" value="values"/>
		</fr:slot>
		<fr:slot name="name" validator="pt.ist.fenixWebFramework.rendererExtensions.validators.MultiLanguageStringValidator"/>
		<fr:slot name="order" required="true"/>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%="/metaTypeManagement.do?method=manageFields&metaTypeId=" + metaType.getExternalId() + "&fieldSetId=" + fieldSet.getExternalId() %>" />
</fr:edit>


