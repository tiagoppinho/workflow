<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<h2><bean:message key="label.metaType.managament" bundle="META_WORKFLOW_RESOURCES"/></h2>

<ul>
	<li>
		<html:link page="/metaTypeManagement.do?method=prepareCreateMetaType">
			<bean:message key="link.metaType.create" bundle="META_WORKFLOW_RESOURCES"/>
		</html:link>
	</li>
</ul>

<fr:view name="metaTypes" schema="view.metaType.list">
	<fr:layout name="tabular">
		<fr:property name="classes" value="table"/>
		<fr:property name="linkFormat(details)" value="/metaTypeManagement.do?method=viewMetaType&metaTypeId=${externalId}"/>
		<fr:property name="bundle(details)" value="MYORG_RESOURCES"/>
		<fr:property name="key(details)" value="link.view"/>
		<fr:property name="order(details)" value="1"/>
		
		<fr:property name="linkFormat(edit)" value="/metaTypeManagement.do?method=editMetaType&metaTypeId=${externalId}"/>
		<fr:property name="bundle(edit)" value="MYORG_RESOURCES"/>
		<fr:property name="key(edit)" value="link.edit"/>
		<fr:property name="order(edit)" value="2"/>
		
	</fr:layout>
</fr:view>	
