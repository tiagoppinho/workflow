<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@page import="java.util.List"%>

<% boolean hasSomeProcess = false; %>
<logic:notEmpty name="pendingProcessList">
	<table class="width100pc">
		<logic:iterate id="counter" name="pendingProcessList">
			<bean:define id="count" name="counter" property="count"/>
			<logic:greaterThan name="count" value="0">
				<% hasSomeProcess = true; %>
				<tr>
					<td><fr:view name="counter" property="processClass" layout="name-resolver"/></td>
					<td>
					<bean:define id="typeOfProcess" name="counter" property="processClassForForwarding" type="java.lang.Class"/> 

					<html:link page="#">
						<bean:write name="count"/>
					</html:link>
					</td>
				</tr>
			</logic:greaterThan>
		</logic:iterate>
	</table>
</logic:notEmpty>
<% if (!hasSomeProcess) { %>
	<p><em><bean:message key="widget.label.noProcesses" bundle="WORKFLOW_RESOURCES"/></em></p>
<% } %>
