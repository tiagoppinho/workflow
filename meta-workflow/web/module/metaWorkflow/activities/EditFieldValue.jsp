<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="fieldBean" name="information" property="fieldBean" type="module.metaWorkflow.domain.FieldValue.FieldValueBean"/>

<p>
<h3><fr:view name="information" property="fieldBean.fieldName"/></h3>

<div class="dinline forminline">
	<jsp:include page="<%= "../domain/fieldValues/edit" + fieldBean.getFieldClass().getSimpleName() + ".jsp" %>"/>
</div>