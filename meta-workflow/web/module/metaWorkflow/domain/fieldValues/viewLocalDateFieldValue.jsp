<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%@ page import="module.metaWorkflow.domain.WorkflowMetaType"%>
<%@ page import="module.metaWorkflow.domain.WorkflowMetaProcess"%>
<%@ page import="module.metaWorkflow.domain.LocalDateFieldValue"%>
<%
	WorkflowMetaType metaType = WorkflowMetaType.readMetaType((String) request.getParameter("metaTypeId"));
	WorkflowMetaProcess process = metaType.getProcess((String) request.getParameter("processId"));
	LocalDateFieldValue field = (LocalDateFieldValue) process.getField((String) request.getParameter("fieldId"));
	request.setAttribute("field", field);
%>

<logic:notEmpty name="field" property="localDateValue">
	<fr:view name="field" property="localDateValue"/>
</logic:notEmpty>
<logic:empty name="field" property="localDateValue">
	-
</logic:empty>
