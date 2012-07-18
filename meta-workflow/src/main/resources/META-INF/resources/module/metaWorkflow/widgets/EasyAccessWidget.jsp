<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>

<bean:define id="dashBoardId" name="dashBoard" property="externalId"/>
<bean:define id="widgetId" name="widget" property="externalId"/>

<fr:form action="<%= "/dashBoardManagement.do?method=widgetSubmition&dashBoardId=" + dashBoardId + "&dashBoardWidgetId=" + widgetId%>">
	<table>
		<tr>
			<td>
				Pesquisar
			</td>
			<td>
				<fr:edit id="processId" name="bean" slot="string"/>
			</td>
			<td>
				<html:submit styleClass="inputbutton">Go</html:submit>
			</td>
		</tr>
	</table>
</fr:form>
