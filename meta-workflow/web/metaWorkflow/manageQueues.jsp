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

	<div class="infoop2" style="-moz-border-radius: 6px; -webkit-border-radius: 6px;">
		<strong><bean:message key="label.queue.create" bundle="META_WORKFLOW_RESOURCES"/></strong>:
		<fr:edit id="newQueue" name="bean" schema="create.queue" action="/metaWorkflow?method=createNewQueue">
			<fr:layout name="tabular">
				<fr:property name="classes" value="form"/>
			</fr:layout>
		</fr:edit>
	</div>
