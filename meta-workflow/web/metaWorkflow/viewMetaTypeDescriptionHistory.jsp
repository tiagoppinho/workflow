<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<h2><bean:message key="title.metaType.descriptionHistory" bundle="META_WORKFLOW_RESOURCES"/></h2>

<bean:define id="processId" name="process" property="OID"/>

<logic:iterate id="description" name="metaType" property="orderedDescriptionHistory">
	<div class="infoop2" style="-moz-border-radius: 6px; -webkit-border-radius: 6px;">
		<fr:view name="description" property="version"/><br/>
		<fr:view name="description" property="versionOwner.presentationName"/><br/>
		<fr:view name="description" property="description"/><br/>
		<div class="infoop4">
			<fr:view name="description" property="diffWithLastVersion"/>
		</div>
	</div>
</logic:iterate>

