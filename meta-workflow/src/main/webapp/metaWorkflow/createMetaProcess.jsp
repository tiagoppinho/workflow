
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="label.create.metaProcess" bundle="META_WORKFLOW_RESOURCES"/></h2>

	<fr:edit id="processBean" name="workflowBean" schema="create.meta.process" action="/metaWorkflow.do?method=createMetaProcess">
		<fr:layout name="tabular">
		</fr:layout>
	<fr:destination name="postBack" path="/metaWorkflow.do?method=prepareCreateProcess"/>
	<fr:destination name="cancel" path="/metaWorkflow.do?method=viewOpenProcessesInMyQueues"/>
	</fr:edit>


