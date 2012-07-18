<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="fieldBean" name="information" property="fieldBean" type="module.metaWorkflow.domain.FieldValue.FieldValueBean"/>

<p>
<h3><fr:view name="information" property="fieldBean.fieldName"/></h3>

<div class="dinline forminline">
	<jsp:include page="<%= "../domain/fieldValues/edit" + fieldBean.getFieldClass().getSimpleName() + ".jsp" %>"/>
</div>
