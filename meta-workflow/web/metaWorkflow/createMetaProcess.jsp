
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="label.create.metaProcess" bundle="META_WORKFLOW_RESOURCES"/></h2>

<div class="infoop2" style="-moz-border-radius: 6px; -webkit-border-radius: 6px;">
	<fr:edit id="processBean" name="workflowBean" schema="create.meta.process" action="/metaWorkflow.do?method=createMetaProcess">
		<fr:layout name="tabular">
			<fr:property name="classes" value="form"/>
		</fr:layout>
	</fr:edit>
</div>


