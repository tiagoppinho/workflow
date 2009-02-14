<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<h3>
	<bean:message key="title.removedFiles" bundle="WORKFLOW_RESOURCES"/>
</h3>

<p>
	<html:link page="/workflowProcessManagement.do?method=viewProcess" paramId="processId" paramName="process" paramProperty="OID">
		« <bean:message key="link.backToProcess" bundle="WORKFLOW_RESOURCES"/>
	</html:link>
</p>

<ul>
	<logic:iterate id="file" name="process" property="deletedFiles">
		<li>
			<bean:define id="fileId" name="file" property="OID"/>
			<fr:view name="file" property="displayName"/> <html:link page='<%= "/workflowProcessManagement.do?method=downloadFile&fileId=" + fileId %>'><bean:message key="link.downloadFile" bundle="WORKFLOW_RESOURCES"/></html:link> 
		</li>
	</logic:iterate>
</ul>