<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<logic:present role="pt.ist.bennu.core.domain.RoleType.MANAGER">
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
		<bean:write name="queue" property="name"/>
		<bean:write name="queue" property="metaType"/>		
		<br/>
	</logic:iterate>
</logic:notEmpty>
