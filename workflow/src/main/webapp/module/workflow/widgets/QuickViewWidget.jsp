<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>

<%@page import="module.workflow.widgets.QuickViewWidget"%>
<bean:define id="widget" name="widget" toScope="request" type="module.dashBoard.domain.DashBoardWidget"/>
<bean:define id="widgetId" name="widget" property="externalId" type="java.lang.String" />
	
<fr:form id="quickAccessForm" action="<%="/dashBoardManagement.do?method=widgetSubmition&dashBoardWidgetId=" + widgetId %>">
	<table class="quicksearch">
		<tr>
			<td>
			<fr:edit id="quickAccess" name="searchBean" schema="search.quick">
				<fr:layout>
					<fr:property name="classes" value="thnowrap" />
					<fr:property name="columnClasses" value=",,dnone" />
				</fr:layout>
			</fr:edit>
			</td>
			<td>
				<html:submit><bean:message key="link.view" bundle="MYORG_RESOURCES"/></html:submit>
			</td>
		</tr>
	</table>
	<div>

	</div>
</fr:form>

<bean:define id="theme" name="virtualHost" property="theme.name"/>

<script type="text/javascript">

function spinner(formData, jqForm, options) {
	var warningDiv =$("#quickAccessForm > div");
	warningDiv.empty();
	warningDiv.append('<bean:message key="label.searching" bundle="WORKFLOW_RESOURCES"/>...<img src="<%= request.getContextPath() + "/CSS/" + theme + "/images/autocomplete.gif"%>"/>');
}

function decide(responseText, statusText) {
	if (responseText == '<%= QuickViewWidget.NOT_FOUND %>') {
		var warningDiv =$("#quickAccessForm > div");
		warningDiv.empty();
		warningDiv.append('<p class="mtop0"><em><bean:message key="widget.widgetQuickView.noProcessFound" bundle="WORKFLOW_RESOURCES"/>.</em></p>');
	}
	else {
		window.location = <%= "\"" + request.getContextPath() + "\" + responseText" %>;
	}
}

$("#quickAccessForm").ajaxForm({beforeSubmit: spinner, success: decide});
</script> 
