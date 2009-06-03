<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="title.uploadFile" bundle="WORKFLOW_RESOURCES"/></h2>

<bean:define id="process" name="process" toScope="request"/>
<bean:define id="processOID" name="process" property="OID" toScope="request"/>

<bean:define id="processClassName" name="process" property="class.name" type="java.lang.String"/>
<bean:define id="includeFolder" value="<%= processClassName.replace('.','/')%>"/>

<jsp:include page='<%= "/" + includeFolder + "/shortBody.jsp" %>'/>

<bean:define id="urlView">/workflowProcessManagement.do?method=viewProcess&amp;processId=<bean:write name="process" property="OID"/></bean:define>

<fr:edit name="bean" id="uploadFile" action='<%= "workflowProcessManagement.do?method=upload&processId=" + processOID %>' schema="addFile">
	<fr:layout name="tabular">
		<fr:property name="classes" value="form mtop05"/>
		<fr:property name="columnClasses" value=",,tderror"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%= urlView %>" />
</fr:edit>