<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<bean:define id="metaType" name="metaType" type="module.metaWorkflow.domain.WorkflowMetaType"></bean:define>
<bean:define id="fieldSet" name="metaType" property="fieldSet" type="module.metaWorkflow.domain.MetaFieldSet"></bean:define>

<h2><bean:message key="link.manage.metaFields" bundle="META_WORKFLOW_RESOURCES"/></h2>

<h3><bean:message key="label.metaType" bundle="META_WORKFLOW_RESOURCES"/> - <fr:view name="metaType" property="name"/></h3>

<p>

<strong><bean:write name="fieldSet" property="name.content"/>:</strong>

<logic:notEmpty name="fieldSet" property="childFields">
	<fr:view name="fieldSet" property="orderedChildFields">
		<fr:schema type="module.metaWorkflow.domain.MetaField" bundle="META_WORKFLOW_RESOURCES">
			<fr:slot name="fieldOrder" key="label.module.metaWorkflow.presentationTier.dto.MetaFieldBean.order"/>
			<fr:slot name="name.content" key="label.module.metaWorkflow.presentationTier.dto.MetaFieldBean.name"/>
			<fr:slot name="localizedClassName" key="label.module.metaWorkflow.presentationTier.dto.MetaFieldBean.fieldClass"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2"/>
			<fr:property name="columnClasses" value="aright,aleft,aleft"/>
			
			<fr:property name="linkFormat(remove)" value="<%="/metaTypeManagement.do?method=removeField&metaTypeId=" + metaType.getExternalId() + "&fieldSetId=" + fieldSet.getExternalId() + "&fieldId=${externalId}" %>"/>
			<fr:property name="bundle(remove)" value="META_WORKFLOW_RESOURCES"/>
			<fr:property name="key(remove)" value="link.manage.removeField"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
<logic:empty name="fieldSet" property="childFields">
	<bean:message key="label.noFields" bundle="META_WORKFLOW_RESOURCES" />
</logic:empty>

<p>

<html:link action="<%="/metaTypeManagement.do?method=prepareAddField&metaTypeId=" + metaType.getExternalId() + "&fieldSetId=" + fieldSet.getExternalId() %>">
	<bean:message key="link.manage.addField" bundle="META_WORKFLOW_RESOURCES" />
</html:link>
