<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@page import="module.workflow.presentationTier.WorkflowLayoutContext"%>
<%@page import="pt.ist.bennu.core.presentationTier.actions.ContextBaseAction"%>

<%
	final String contextPath = request.getContextPath();
%>

<link rel="stylesheet" type="text/css" href="<%= contextPath %>/CSS/metaWorkflowSpecific.css" media="screen"/>
