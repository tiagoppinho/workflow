<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%@ page import="pt.utl.ist.fenix.tools.util.Strings" %>
<%@ page import="myorg.util.BundleUtil" %>

<bean:define id="process" name="information" property="process"/>
<bean:define id="processId" name="process" property="externalId" type="java.lang.String"/>

<bean:define id="field" name="information" property="field" type="module.metaWorkflow.domain.FieldValue"/>

<fr:edit action="<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + processId %>" id="fieldValue" name="field">
	<fr:schema type="module.metaWorkflow.domain.StringsFieldValue" bundle="META_WORKFLOW_RESOURCES">
		<fr:slot name="stringsValue" key="label.metaProcess.field.value"/>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="columnClasses" value=",,tderror" />
	</fr:layout>
</fr:edit>