<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<h2><bean:message key="label.metaType.create" bundle="META_WORKFLOW_RESOURCES"/></h2>

<fr:edit id="newMetaType" name="bean" schema="create.meta.type" action="/metaTypeManagement?method=createNewMetaType">
	<fr:destination name="cancel" path="/metaTypeManagement.do?method=manageMetaType"/>
</fr:edit>
