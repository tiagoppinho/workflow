<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<h2><bean:message key="label.queues.management" bundle="WORKFLOW_RESOURCES"/></h2>

<ul>
	<li>
		<html:link page="/workflowQueueManagement.do?method=prepareCreateQueue">
			<bean:message key="link.createQueue" bundle="WORKFLOW_RESOURCES"/>
		</html:link>
	</li>
</ul>

<fr:view name="queues" schema="view.queues.details">
	<fr:layout name="tabular">
		<fr:property name="classes" value="table tsyle2 mtop05"/>
		<fr:property name="linkFormat(view)" value="/workflowQueueManagement.do?method=viewQueue&queueId=${externalId}"/>
		<fr:property name="key(view)" value="link.view"/>
		<fr:property name="bundle(view)" value="MYORG_RESOURCES"/>
		
		<fr:property name="linkFormat(edit)" value="/workflowQueueManagement.do?method=prepareEditQueue&queueId=${externalId}"/>
		<fr:property name="key(edit)" value="link.edit"/>
		<fr:property name="bundle(edit)" value="MYORG_RESOURCES"/>
	</fr:layout>
</fr:view>

