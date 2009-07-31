<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@page import="module.workflow.presentationTier.WorkflowLayoutContext"%>
<%@page import="myorg.presentationTier.actions.ContextBaseAction"%><jsp:include page="/head.jsp"/>

<%
	final WorkflowLayoutContext layoutContext = (WorkflowLayoutContext) ContextBaseAction.getContext(request);
%>

<jsp:include page="<%= layoutContext.getOldHead() %>"/>

<%
	final String contextPath = request.getContextPath();
%>

<link rel="stylesheet" type="text/css" href="<%= contextPath %>/CSS/metaWorkflowSpecific.css" media="screen"/>