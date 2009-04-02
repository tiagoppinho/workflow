<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="processId" name="process" property="OID" />
<bean:define id="processClassName" name="process" property="class.name" type="java.lang.String"/>
<bean:define id="includeFolder" value="<%= processClassName.replace('.','/')%>"/>

<jsp:include page='<%= "/" + includeFolder + "/header.jsp" %>'/>

<logic:present name="process" property="currentOwner">
	<bean:define id="ownerName" name="process" property="currentOwner.username"/>
	<div class="infoop4">
		<bean:message key="message.info.currentOwnerIs" bundle="WORKFLOW_RESOURCES" arg0="<%= ownerName.toString() %>"/>
	</div>
</logic:present>


<ul>
<logic:iterate id="activity" name="process" property="activeActivities">
	<bean:define id="name" name="activity" property="name" />
	<li>
	<html:link
		page='<%="/workflowProcessManagement.do?method=process&activity=" + name + "&processId=" + processId%>'>
		<fr:view name="activity" property="localizedName" />
	</html:link>
	</li>
</logic:iterate>
</ul>

<logic:present name="information">
	<logic:equal name="information" property="confirmationNeeded" value="true">
		<logic:equal name="information" property="confirmed" value="false">
			<bean:define id="name" name="information" property="activityName"/>
			<div>
				<fr:form  action='<%="/workflowProcessManagement.do?method=confirmActivity&activity=" + name + "&processId=" + processId%>'>
					<fr:edit id="confirmationBean" name="information" visible="false">
						<fr:destination name="cancel" path='<%="/workflowProcessManagement.do?method=viewProcess&processId=" + processId%>'/>
					</fr:edit>
					<fr:view name="information" property="localizedMessage"/>
					<html:submit><bean:message key="button.confirm" bundle="WORKFLOW_RESOURCES"/></html:submit>
					<html:cancel><bean:message key="button.cancel" bundle="WORKFLOW_RESOURCES"/></html:cancel>
				</fr:form>
				
			</div>
		</logic:equal>
	</logic:equal>
</logic:present>

<ul class="operations">
	<li>
		<html:link page="/workflowProcessManagement.do?method=fileUpload" paramId="processId" paramName="process" paramProperty="OID">
			<bean:message key="link.uploadFile" bundle="WORKFLOW_RESOURCES"/>
		</html:link>
	</li>
	<li>
		<html:link page="/workflowProcessManagement.do?method=viewRemovedFiles" paramId="processId" paramName="process" paramProperty="OID">
			<bean:message key="link.viewRemovedFiles" bundle="WORKFLOW_RESOURCES"/>
		</html:link>
	</li>
	<li>
		<html:link page="/workflowProcessManagement.do?method=viewLogs" paramId="processId" paramName="process" paramProperty="OID">
			<bean:message key="link.viewLogs" bundle="WORKFLOW_RESOURCES"/>
		</html:link>
	</li>

	<bean:size id="comments"  name="process" property="comments"/>
	<li> 
		<html:link page="/workflowProcessManagement.do?method=viewComments" paramId="processId" paramName="process" paramProperty="OID">
			<bean:message key="link.comments" bundle="WORKFLOW_RESOURCES"/> (<%= comments %>)
		</html:link>	
		<logic:greaterThan name="comments" value="0">
			|
			<span class="color888" style="font-size: 0.9em;">
				<bean:message key="label.lastBy" bundle="WORKFLOW_RESOURCES"/> 
				<bean:define id="mostRecentComment" name="process" property="mostRecentComment"/>
				<strong><fr:view name="mostRecentComment" property="commenter.username"/></strong>, <fr:view name="mostRecentComment" property="date"/> 
			</span>
		</logic:greaterThan>
	</li>
</ul>

<ul>
	<logic:iterate id="file" name="process" property="files">
		<li>
		<bean:define id="fileId" name="file" property="OID"/>
		<fr:view name="file" property="displayName"/> <html:link page='<%= "/workflowProcessManagement.do?method=downloadFile&fileId=" + fileId %>'><bean:message key="link.downloadFile" bundle="WORKFLOW_RESOURCES"/></html:link> <html:link page='<%= "/workflowProcessManagement.do?method=removeFile&fileId=" + fileId %>'><bean:message key="link.removeFile" bundle="WORKFLOW_RESOURCES"/></html:link>
		</li>
	</logic:iterate>
</ul>

<jsp:include page='<%= "/" + includeFolder + "/body.jsp" %>'/>