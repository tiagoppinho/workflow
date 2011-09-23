<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="process" name="information" property="process"/>
<bean:define id="processId" name="process" property="externalId" type="java.lang.String"/>
<bean:define id="name" name="information" property="activityName"/>

<bean:define id="field" name="information" property="field" type="module.metaWorkflow.domain.FieldValue"/>

<p>
<h3><fr:view name="information" property="field.metaField.name.content"/></h3>

<fr:edit action="<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + processId %>" id="fieldValue" name="field" schema="<%= field.getSchemaName() %>" >
</fr:edit>