<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<%@page import="myorg.presentationTier.actions.ContextBaseAction"%>
<%@page import="myorg.domain.contents.Node"%>
<%@page import="module.workflow.presentationTier.ProcessNodeSelectionMapper"%>


<%@page import="myorg.presentationTier.servlets.filters.contentRewrite.ContentContextInjectionRewriter"%>
<%@page import="java.util.List"%><logic:notEmpty name="pendingProcessList">
	<table class="width100pc">
		<logic:iterate id="counter" name="pendingProcessList">
			<tr>
				<td><fr:view name="counter" property="countableObject" layout="name-resolver"/></td>
				<td>
			
				<bean:define id="typeOfProcess" name="counter" property="countableObject" type="java.lang.Class"/> 
				
				<% List<Node> nodes = ProcessNodeSelectionMapper.getForwardFor(typeOfProcess); Node node = nodes != null && nodes.size() > 0 ? nodes.get(nodes.size()-1) : null; %>
				<html:link page='<%= node != null ? node.getUrl() : "#" %>'>
					<fr:view name="counter" property="value" />
				</html:link>
				</td>
				
			</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>
<logic:empty name="pendingProcessList">
	<p><em><bean:message key="widget.label.noProcesses" bundle="WORKFLOW_RESOURCES"/></em></p>
</logic:empty>
