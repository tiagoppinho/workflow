<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

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
