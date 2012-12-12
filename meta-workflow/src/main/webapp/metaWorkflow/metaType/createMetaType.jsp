<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<h2><bean:message key="label.metaType.create" bundle="META_WORKFLOW_RESOURCES"/></h2>

<fr:edit id="newMetaType" name="bean" schema="create.meta.type" action="/metaTypeManagement?method=createNewMetaType">
	<fr:destination name="cancel" path="/metaTypeManagement.do?method=manageMetaType"/>
</fr:edit>
