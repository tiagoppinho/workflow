<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<table class="table">
	<tr>
		<th><bean:message key="label.unit" bundle="ORGANIZATION_RESOURCES"/></th>
	</tr>
	<tr>
		<td> <fr:view name="queue" property="unit.presentationName"/> </td>
	</tr>
	<tr>
		<th><bean:message key="label.accountabilityTypes" bundle="ORGANIZATION_RESOURCES"/></th>
	</tr>
	<tr>
		<td class="aleft">
			<ul>
				<logic:iterate id="accountabilityType" name="queue" property="accountabilityTypes">
					<li><fr:view name="accountabilityType" property="name"/></li>
				</logic:iterate>
			</ul>
		</td>
	</tr>
</table>
