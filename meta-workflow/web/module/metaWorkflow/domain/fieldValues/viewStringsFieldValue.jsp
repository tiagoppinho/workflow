<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="field" name="field" type="module.metaWorkflow.domain.StringsFieldValue"/>

<%
	if ((field.getStringsValue() != null) && (field.getStringsValue().size() > 0)) {
	    for (String string : field.getStringsValue().getUnmodifiableList()) {
			out.println(string);
			out.println("<br>");
	    }
	} else {
	    out.println("-");
	}
%>