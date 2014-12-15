<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="process" name="information" property="process"/>
<bean:define id="processId" name="process" property="externalId" type="java.lang.String"/>
<bean:define id="name" name="information" property="activityName"/>

<div class="alert alert-warning">
	<bean:message key="label.GiveProcess.warning" bundle="WORKFLOW_RESOURCES"/>:
</div>

<div class="dinline forminline">
		<fr:edit id="activityBean" name="information" schema="activityInformation.GiveProcess" type="module.workflow.activities.UserInformation"
			action='<%= "/workflowProcessManagement.do?method=process&processId=" + processId + "&activity=" + name %>'>
			<fr:layout name="tabular">
				<fr:property name="classes" value="form listInsideClear" />
				<fr:property name="columnClasses" value="width100px,,tderror" />
			</fr:layout>
			<fr:destination name="cancel" path='<%="/workflowProcessManagement.do?method=viewProcess&processId=" + processId%>'/>
		</fr:edit>
</div>
