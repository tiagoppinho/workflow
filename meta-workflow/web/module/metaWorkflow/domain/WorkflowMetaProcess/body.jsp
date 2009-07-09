<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<strong><bean:message key="label.metaType.description" bundle="META_WORKFLOW_RESOURCES"/></strong>:

<div class="infoop2" style="-moz-border-radius: 6px; -webkit-border-radius: 6px;">
	<bean:define id="currentDescription" name="process" property="metaType.currentDescription"/>
	<fr:view name="currentDescription" property="description"/>
	<p>
		<em>
			<span class="aright">
			<bean:define id="version" name="currentDescription" property="version"/>
			<bean:define id="versionOwner" name="currentDescription" property="versionOwner.presentationName"/>
			
				<bean:message key="label.metaTypeDescription.by" bundle="META_WORKFLOW_RESOURCES" arg0="<%= version.toString() %>" arg1="<%= versionOwner.toString() %>" />
			</span>
		</em>
		<bean:define id="processOID" name="process" property="OID"/>
		(<html:link page="<%= "/metaWorkflow.do?method=editMetaTypeDescription&processId=" + processOID %>">
			<bean:message key="link.edit" bundle="META_WORKFLOW_RESOURCES"/>
		</html:link>, 
		<html:link page="<%= "/metaWorkflow.do?method=viewMetaTypeDescriptionHistory&processId=" + processOID %>">
			<bean:message key="link.history" bundle="META_WORKFLOW_RESOURCES"/>
		</html:link>)
	</p>
</div>


<strong><bean:message key="label.metaProcess.information" bundle="META_WORKFLOW_RESOURCES"/></strong>:

<div class="infoop2" style="-moz-border-radius: 6px; -webkit-border-radius: 6px;">
	<fr:view name="process" schema="view.meta.process.details">
		<fr:layout name="tabular">
			<fr:property name="columnClasses" value="nowrap aright,"/>
		</fr:layout>	
	</fr:view>
</div>