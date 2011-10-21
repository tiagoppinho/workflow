<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<table class="table tstyle2">
	<tr>
		<th><bean:message key="label.application.users" bundle="MYORG_RESOURCES"/></th>
	</tr>
	<tr>
		<td class="aleft"> 
			<ul>
				<logic:iterate id="user" name="queue" property="users">
					<li><fr:view name="user" property="presentationName"/></li>
				</logic:iterate>
			</ul> 
		</td>
	</tr>
</table>

