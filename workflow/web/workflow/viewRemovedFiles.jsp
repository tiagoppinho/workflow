<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<h2><bean:message key="title.removedFiles" bundle="WORKFLOW_RESOURCES"/></h2>


<p class="mtop05 mbottom15">
	<html:link page="/workflowProcessManagement.do?method=viewProcess" paramId="processId" paramName="process" paramProperty="OID">
		« <bean:message key="link.backToProcess" bundle="WORKFLOW_RESOURCES"/>
	</html:link>
</p>


<bean:define id="processClassName" name="process" property="class.name" type="java.lang.String"/>
<bean:define id="includeFolder" value="<%= processClassName.replace('.','/')%>"/>

<jsp:include page='<%= "/" + includeFolder + "/shortBody.jsp" %>'/>

<ul class="mtop15">
	<logic:iterate id="file" name="process" property="deletedFiles">
		<li>
			<bean:define id="fileId" name="file" property="OID"/>
			<fr:view name="file" property="displayName"/> <html:link page='<%= "/workflowProcessManagement.do?method=downloadFile&fileId=" + fileId %>'><bean:message key="link.downloadFile" bundle="WORKFLOW_RESOURCES"/></html:link> 
		</li>
	</logic:iterate>
</ul>