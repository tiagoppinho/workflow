<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<logic:present role="myorg.domain.RoleType.MANAGER">
	<bean:define id="url">/metaWorkflowOrganizationModel.do?method=prepareCreateQueuet&amp;organizationalModelOid=<bean:write name="organizationalModel" property="externalId"/>&amp;viewName=queueView</bean:define>
	<html:link action="<%= url %>" paramId="partyOid" paramName="party" paramProperty="externalId">
		<bean:message key="label.unit.queue.create" bundle="META_WORKFLOW_RESOURCES"/>
	</html:link>
</logic:present>

<br/>
<br/>

<logic:empty name="party" property="queues">
	<p style="font-style: italic;">
		<bean:message bundle="META_WORKFLOW_RESOURCES" key="label.message.party.queues.none"/>
	</p>
</logic:empty>

<logic:notEmpty name="party" property="queues">
	<logic:iterate id="queue" name="party" property="queues">
		<bean:write name="queue" property="metaType.name"/>
		<bean:write name="queue" property="name"/>
	</logic:iterate>
</logic:notEmpty>
