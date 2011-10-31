<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%@ page import="module.workflow.domain.WorkflowSystem"%>
<%@ page import="module.metaWorkflow.domain.WorkflowMetaType"%>
<%@ page import="module.metaWorkflow.domain.WorkflowMetaProcess"%>
<%@ page import="module.metaWorkflow.domain.DateTimeFieldValue"%>
<%
	WorkflowMetaType metaType = WorkflowSystem.getInstance().getMetaType((String) request.getParameter("metaTypeId"));
	WorkflowMetaProcess process = metaType.getProcess((String) request.getParameter("processId"));
	DateTimeFieldValue field = (DateTimeFieldValue) process.getField((String) request.getParameter("fieldId"));
	request.setAttribute("field", field);
%>

<logic:notEmpty name="field" property="dateTimeValue">
	<fr:view name="field" property="dateTimeValue"/>
</logic:notEmpty>
<logic:empty name="field" property="dateTimeValue">
	-
</logic:empty>
