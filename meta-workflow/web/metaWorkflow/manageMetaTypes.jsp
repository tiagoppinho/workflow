<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<h2><bean:message key="label.metaType.managament" bundle="META_WORKFLOW_RESOURCES"/></h2>

<fr:view name="metaTypes" schema="view.meta.type">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2"/>
		<fr:property name="columnClasses" value=",aleft,aleft"/>
	</fr:layout>
</fr:view>


	<div class="infoop2" style="-moz-border-radius: 6px; -webkit-border-radius: 6px;">
		<strong><bean:message key="label.metaType.create" bundle="META_WORKFLOW_RESOURCES"/></strong>:
		<fr:edit id="newMetaType" name="bean" schema="create.meta.type" action="/metaWorkflow?method=createNewMetaType">
			<fr:layout name="tabular">
				<fr:property name="classes" value="form"/>
			</fr:layout>
		</fr:edit>
	</div>
