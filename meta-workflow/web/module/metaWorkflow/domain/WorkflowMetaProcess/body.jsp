<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/workflow.tld" prefix="wf"%>


<%@page import="myorg.presentationTier.servlets.filters.contentRewrite.ContentContextInjectionRewriter"%>
<%@page import="pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter"%><bean:define id="processOID" name="process" property="OID"/>


<logic:equal name="process" property="open" value="false">
	<div class="infoop2" style="-moz-border-radius: 6px; -webkit-border-radius: 6px;">
		<bean:message key="label.message.processIsClosed" bundle="META_WORKFLOW_RESOURCES"/>
	</div>
</logic:equal>

<bean:define id="linkRemoveLabel">
		<bean:message bundle="MYORG_RESOURCES" key="link.remove"/>
</bean:define>
	
<script type="text/javascript">
	$(function() {
		$("#tabs").tabs();
	});
	</script>


<%= ContentContextInjectionRewriter.BLOCK_HAS_CONTEXT_PREFIX %>
<div id="tabs">
	<ul>
		<li><%= GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#tabs-1">Informações</a></li>
		<li><%= GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#tabs-2"><bean:message key="label.observers" bundle="META_WORKFLOW_RESOURCES"/></a></li>
		<li><%= GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#tabs-3">Regras</a></li>
	</ul>
	<div id="tabs-1">
	<fr:view name="process" schema="view.meta.process.details">
		<fr:layout name="tabular">
			<fr:property name="columnClasses" value="nowrap aright,"/>
		</fr:layout>	
	</fr:view>	</div>
	<div id="tabs-2">
	<logic:iterate id="observer" name="process" property="observers">			
		
		<bean:define id="userPresentation" name="observer" property="presentationName"/>
		<bean:define id="userID" name="observer" property="OID"/>
		
		<p>
		<span>
			<fr:view name="userPresentation"/>
			<wf:activityLink id="<%= "remove-" + userID %>" processName="process" activityName="RemoveObserver" linkName="<%= linkRemoveLabel %>" scope="request">
						<wf:activityLinkParameter parameter="user" value="<%= userID.toString() %>"/>
			</wf:activityLink>
			<script type="text/javaScript">
			 linkConfirmationHook("<%= "remove-" + userID.toString() %>", '<bean:message key="label.confirm.removal" bundle="META_WORKFLOW_RESOURCES" arg0="<%= userPresentation.toString() %>"/>', ' <bean:message key="title.removeObserver" bundle="META_WORKFLOW_RESOURCES"/>');
			</script>
		</span>
		</p>
	</logic:iterate>	</div>
	<div id="tabs-3">
		<bean:define id="currentDescription" name="process" property="metaType.currentDescription"/>
		<fr:view name="currentDescription" property="description"/>
		<p>
			<em>
				<span class="aright">
				<bean:define id="version" name="currentDescription" property="version"/>
				<bean:define id="versionOwner" name="currentDescription" property="versionOwner.presentationName"/>
				<bean:define id="date">
					<fr:view name="currentDescription" property="date"/>
				</bean:define>
				
					<bean:message key="label.metaTypeDescription.by" bundle="META_WORKFLOW_RESOURCES" arg0="<%= version.toString() %>" arg1="<%= versionOwner.toString() %>" arg2="<%= date.toString()%>"/>
				</span>
			</em>
			(<html:link page="<%= "/metaWorkflow.do?method=editMetaTypeDescription&processId=" + processOID %>">
				<bean:message key="link.edit" bundle="META_WORKFLOW_RESOURCES"/>
			</html:link>, 
			<html:link page="<%= "/metaWorkflow.do?method=viewMetaTypeDescriptionHistory&processId=" + processOID %>">
				<bean:message key="link.history" bundle="META_WORKFLOW_RESOURCES"/>
			</html:link>)
		</p>
			</div>
</div>
<%= ContentContextInjectionRewriter.END_BLOCK_HAS_CONTEXT_PREFIX %>


<strong><bean:message key="title.comments" bundle="WORKFLOW_RESOURCES"/></strong>:

<div class="dotted roundCorners">
	
	<fr:view name="process" layout="viewComments"/>

	<logic:present name="commentBean">
		<fr:form action='<%= "/metaWorkflow.do?method=addComment&processId=" + processOID %>'>
			<table class="form">
				<tr>
					<td>
						<bean:message key="label.addComment" bundle="WORKFLOW_RESOURCES"/>:
					</td>
					<td>
						<fr:edit id="comment" name="commentBean" slot="string" type="java.lang.String" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
							<fr:layout name="longText">
								<fr:property name="rows" value="6"/>
								<fr:property name="columns" value="60"/>
								<fr:property name="classes" value="form"/>
							</fr:layout>
						</fr:edit>
					</td>
				</tr>
			</table>
			<html:submit styleClass="inputbutton"><bean:message key="renderers.form.add.name" bundle="RENDERER_RESOURCES"/> </html:submit>
		</fr:form>
	</logic:present>	
</div>