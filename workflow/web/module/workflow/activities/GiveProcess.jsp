<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="pt.ist.fenixWebFramework.renderers.utils.RenderUtils"%>

<bean:define id="process" name="information" property="process"/>
<bean:define id="processId" name="process" property="externalId" type="java.lang.String"/>
<bean:define id="name" name="information" property="activityName"/>

<div class="highlightBox">
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