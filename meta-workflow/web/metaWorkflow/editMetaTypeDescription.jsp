<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<h2><bean:message key="title.metaType.editDescription" bundle="META_WORKFLOW_RESOURCES"/></h2>

<bean:define id="processId" name="process" property="OID"/>

<fr:edit name="metaType" property="currentDescription" schema="edit.metaTypeDescription" action="<%=  "/metaWorkflow.do?method=viewMetaProcess&processId=" + processId.toString() %>" schema="edit.metaTypeDescription">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%=  "/metaWorkflow.do?method=viewMetaProcess&processId=" + processId.toString() %>"/>
</fr:edit>
