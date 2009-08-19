<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

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