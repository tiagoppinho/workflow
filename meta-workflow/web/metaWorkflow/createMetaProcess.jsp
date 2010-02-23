
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="label.create.metaProcess" bundle="META_WORKFLOW_RESOURCES"/></h2>

	<fr:edit id="processBean" name="workflowBean" schema="create.meta.process" action="/metaWorkflow.do?method=createMetaProcess">
		<fr:layout name="tabular">
		</fr:layout>
	<fr:destination name="postBack" path="/metaWorkflow.do?method=prepareCreateProcess"/>
	<fr:destination name="cancel" path="/metaWorkflow.do?method=viewOpenProcessesInMyQueues"/>
	</fr:edit>


