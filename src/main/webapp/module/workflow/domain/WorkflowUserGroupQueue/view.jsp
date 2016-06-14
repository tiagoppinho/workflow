<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<table class="table tstyle2">
	<tr>
		<th><bean:message key="label.application.users" bundle="MYORG_RESOURCES"/></th>
	</tr>
	<tr>
		<td class="aleft"> 
			<ul>
				<logic:iterate id="user" name="queue" property="users">
					<li><fr:view name="user" property="displayName"/></li>
				</logic:iterate>
			</ul> 
		</td>
	</tr>
</table>

