<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<h2><bean:message key="label.metaType.managament" bundle="META_WORKFLOW_RESOURCES"/></h2>

<logic:iterate id="metaType" name="metaTypes">
	<bean:define id="metaTypeId" name="metaType" property="OID"/>
	<bean:define id="description" name="metaType" property="currentDescription"/>
	<div class="infoop2 roundCorners">
		<p>
			<strong><fr:view name="metaType" property="name"/></strong>
			(<html:link page="<%= "/metaWorkflow.do?method=editMetaTypeDescription&metaTypeId=" + metaTypeId %>">
				<bean:message key="link.edit" bundle="META_WORKFLOW_RESOURCES"/>
			</html:link>, 
			<html:link page="<%= "/metaWorkflow.do?method=viewMetaTypeDescriptionHistory&metaTypeId="+ metaTypeId %>">
				<bean:message key="link.history" bundle="META_WORKFLOW_RESOURCES"/>
			</html:link>,
			<html:link page="<%= "/metaWorkflow.do?method=manageMetaTypeObservers&metaTypeId="+ metaTypeId %>">
				<bean:message key="link.manageMetaTypeObservers" bundle="META_WORKFLOW_RESOURCES"/>
			</html:link>)
		</p>
		
		<p class="mbottom05"><strong><bean:message key="label.metaType.currentVersion" bundle="META_WORKFLOW_RESOURCES"/> (v. <fr:view name="description" property="version"/>):</strong></p>
		
		<fr:view name="description" property="description"/>
		
		<p class="mbottom05"><strong><bean:message key="label.metaType.availableFileTypes" bundle="META_WORKFLOW_RESOURCES"/>:</strong></p>
		<logic:empty name="metaType" property="availableFileTypes">
			-
		</logic:empty>
		<logic:notEmpty name="metaType" property="availableFileTypes">
			<fr:view name="metaType" property="availableFileTypes" >
				<fr:layout name="separator-list">
					<fr:property name="separator" value=","/>
					<fr:property name="eachLayout" value="name-resolver"/>
				</fr:layout>	
			</fr:view>
		</logic:notEmpty>

	</div>
</logic:iterate>

<div class="mtop2">
	<strong><bean:message key="label.metaType.create" bundle="META_WORKFLOW_RESOURCES"/></strong>
	<fr:edit id="newMetaType" name="bean" schema="create.meta.type" action="/metaWorkflow?method=createNewMetaType">
		<fr:layout name="tabular">
			<fr:property name="classes" value="form icleanlist"/>
			<fr:property name="columnClasses" value=""/>
		</fr:layout>
	</fr:edit>
</div>
