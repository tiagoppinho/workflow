<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<h2><bean:message key="label.queues.management" bundle="META_WORKFLOW_RESOURCES"/></h2>

<fr:view name="queues" schema="view.queues.details">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2"/>
	</fr:layout>
</fr:view>


<fr:form action="/metaWorkflowQueueManagement.do">
	<fr:edit id="queueType" name="bean" slot="queueType">
		<fr:layout name="menu-select-postback">
			<fr:property name="providerClass" value="module.metaWorkflow.presentationTier.renderers.providers.AvailableQueueTypes"/>
			<fr:property name="eachLayout" value="name-resolver"/>
		</fr:layout>
		<fr:destination name="postback" path="/metaWorkflowQueueManagement.do?method=selectQueueType"/>
	</fr:edit>
</fr:form>

<logic:present name="bean" property="queueType">

	<bean:define id="selectedType" name="bean" property="queueType" type="java.lang.Class"/>
	<jsp:include page="<%= "/" + selectedType.getName().replaceAll("\\\\.","/") + "/create.jsp" %>"/>
</logic:present>