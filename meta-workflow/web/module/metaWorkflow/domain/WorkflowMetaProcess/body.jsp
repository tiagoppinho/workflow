<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="processOID" name="process" property="OID"/>

<div>
<strong>
	<a id="showRules" href="#"><bean:message key="label.metaType.description" bundle="META_WORKFLOW_RESOURCES"/></a>
</strong>

	<div id="rules" class="infoop2" style="-moz-border-radius: 6px; -webkit-border-radius: 6px;">
		<bean:define id="currentDescription" name="process" property="metaType.currentDescription"/>
		<fr:view name="currentDescription" property="description"/>
		<p>
			<em>
				<span class="aright">
				<bean:define id="version" name="currentDescription" property="version"/>
				<bean:define id="versionOwner" name="currentDescription" property="versionOwner.presentationName"/>
				
					<bean:message key="label.metaTypeDescription.by" bundle="META_WORKFLOW_RESOURCES" arg0="<%= version.toString() %>" arg1="<%= versionOwner.toString() %>" />
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
	
	<script type="text/javaScript">
		$("#rules").hide();
		$("#showRules").click(
			function() {
				$("#rules").slideToggle();
		});
	</script>
</div>

<strong><bean:message key="label.metaProcess.information" bundle="META_WORKFLOW_RESOURCES"/></strong>:

<div class="infoop2" style="-moz-border-radius: 6px; -webkit-border-radius: 6px;">
	<fr:view name="process" schema="view.meta.process.details">
		<fr:layout name="tabular">
			<fr:property name="columnClasses" value="nowrap aright,"/>
		</fr:layout>	
	</fr:view>
</div>

<strong><bean:message key="title.comments" bundle="WORKFLOW_RESOURCES"/></strong>:

<div class="dotted roundCorners">
	
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
	<fr:view name="process" layout="viewComments"/>
	
</div>