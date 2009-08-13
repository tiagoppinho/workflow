<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<h2><bean:message key="title.metaType.manageQueues" bundle="META_WORKFLOW_RESOURCES"/></h2>

<bean:define id="metaTypeId" name="metaType" property="externalId"/>

<table class="table">
<tr>
	<th> A remover </th>
	<th> A adicionar </th>
</tr>

<tr>
<td>
<logic:iterate id="presentQueue" name="presentQueues">
	<fr:view name="presentQueue" property="name"/>
	<html:link page="<%= "/metaTypeManagement.do?method=removeQueue&metaTypeId=" + metaTypeId %>" paramId="queueId" paramName="presentQueue" paramProperty="externalId">
		<bean:message key="link.remove" bundle="MYORG_RESOURCES"/>
	</html:link>
</logic:iterate>
</td>
<td>
<logic:iterate id="possibleQueue" name="possibleQueues">
	<fr:view name="possibleQueue" property="name"/>
	<html:link page="<%= "/metaTypeManagement.do?method=addQueue&metaTypeId=" + metaTypeId %>" paramId="queueId" paramName="possibleQueue" paramProperty="externalId">
		<bean:message key="link.add" bundle="MYORG_RESOURCES"/>
	</html:link>
</logic:iterate>
</td>
</tr>
</table>