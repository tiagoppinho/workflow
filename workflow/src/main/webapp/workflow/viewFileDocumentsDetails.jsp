<%@page import="pt.ist.bennu.core.util.BundleUtil"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Collection"%>
<%@page import="java.util.ArrayList"%>
<%@page import="module.workflow.domain.ProcessFile"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@page import="module.workflow.presentationTier.WorkflowLayoutContext"%>
<%@page import="pt.ist.bennu.core.presentationTier.actions.ContextBaseAction"%>

<style>
table.files-list {
width: 100%;
}
table.files-list td {
}
table th.file-name {
text-align: left;
}
table th.file-desc {
text-align: left;
}
table th.file-type { }
table th.file-ash { }
table th.file-state { }
table th.file-download { }
table td.file-name {
text-align: left;
}
table td.file-name div {
width: 100px !important;
word-wrap: break-word;
}
table td.file-desc div {
width: 150px !important;
word-wrap: break-word;
text-align: left;
}
table td.file-type { }
table td.file-date { 
width: 110px !important;
}
table td.file-ash {
text-transform: uppercase;
color: #888;
font-size: 10px;
text-align: center;
}
table td.file-ash div {
text-align: center;
margin: auto;
width: 150px !important;
word-wrap: break-word;
}
table td.file-state {
width: 55px !important;
}
table td.file-download {
width: 45px !important;
}
</style>

<h2><bean:message bundle="WORKFLOW_RESOURCES" key="label.fileDetails" /></h2>
<p class="mtop05 mbottom15">
	<html:link page="/workflowProcessManagement.do?method=viewProcess" paramId="processId" paramName="process" paramProperty="externalId">
		« <bean:message key="link.backToProcess" bundle="WORKFLOW_RESOURCES"/>
	</html:link>
</p>


<%
	final WorkflowLayoutContext layoutContext = (WorkflowLayoutContext) ContextBaseAction.getContext(request);
%>

<jsp:include page='<%= layoutContext.getWorkflowShortBody() %>'/>

<logic:empty name="listFiles">
	<p class="mtop15"><em><bean:message key="label.noFiles" bundle="WORKFLOW_RESOURCES"/>.</em></p>
</logic:empty>

<logic:notEmpty name="listFiles">
	<table class="tview1 files-list">
			<tr>
				<th class="file-date"><bean:message bundle="WORKFLOW_RESOURCES" key="label.fileDate"/></th>
				<th class="file-name"><bean:message bundle="WORKFLOW_RESOURCES" key="label.filename"/></th>
				<th class="file-desc"><bean:message bundle="WORKFLOW_RESOURCES" key="label.presentationName"/></th>
				<th class="file-type"><bean:message bundle="WORKFLOW_RESOURCES" key="label.fileType"/></th>
				<th class="file-ash"><bean:message bundle="WORKFLOW_RESOURCES" key="label.digestSha1"/></th>
				<th><bean:message bundle="WORKFLOW_RESOURCES" key="label.fileSize"/></th>
				<th class="file-state"><bean:message bundle="WORKFLOW_RESOURCES" key="label.fileStatus"/></th>
				<th class="file-download"></th>
			</tr>
		<logic:iterate id="document" name="listFiles" type="module.workflow.domain.ProcessDocument">
			<bean:define id="file" name="document" property="document.lastVersionedFile"/>
			<bean:define id="fileNode" name="document" property="fileNode"/>
			<bean:define id="documentId" name="document" property="externalId" type="java.lang.String"/>
				<tr>
					<td class="file-date">
						<div>
							<logic:present name="file" property="creationDate">
								<fr:view name="file" property="creationDate"/>
							</logic:present>
						</div>
					</td>
					<td class="file-name"><div><bean:write name="file" property="filename"/></div></td>
					<td class="file-desc"><div><bean:write name="file" property="displayName"/></div></td>
					<td class="file-type"><%=BundleUtil.getLocalizedNamedFroClass(document.getClass())%></td>
					<td class="file-ash"><div><bean:write name="file" property="hexSHA1MessageDigest"/></div></td>
					<td><fr:view name="file" property="filesize" layout="fileSize"/></td>
					<logic:equal name="fileNode" property="inTrash" value="false" >
						<td class="file-state"><bean:message bundle="WORKFLOW_RESOURCES" key="label.fileStatus.active"/></td>
					</logic:equal>
					<logic:equal name="fileNode" property="inTrash" value="true" >
						<td class="file-state"><bean:message bundle="WORKFLOW_RESOURCES" key="label.fileStatus.deleted"/></td>
					</logic:equal>
					<td class="file-download"><span>(<html:link page='<%= "/workflowProcessManagement.do?method=downloadFileDocument&fileId=" + documentId %>' paramId="processId" paramName="process" paramProperty="externalId"><bean:message key="link.downloadFile" bundle="WORKFLOW_RESOURCES"/></html:link>)</span></td>
				</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>
