<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2>
	<bean:message key="title.comments" bundle="WORKFLOW_RESOURCES"/>
</h2>

<p>
	<html:link page="/workflowProcessManagement.do?method=viewProcess" paramId="processId" paramName="process" paramProperty="OID">
		« <bean:message key="link.backToProcess" bundle="WORKFLOW_RESOURCES"/>
	</html:link>
</p>

<logic:empty name="comments">
	<p class="mtop15"><em><bean:message key="label.noComments" bundle="WORKFLOW_RESOURCES"/>.</em></p>
</logic:empty>


<logic:notEmpty name="comments">
	<div class="mvert2">
	<logic:iterate id="comment" name="comments">
		<div class="comment">
			<p>
				<span><fr:view name="comment" property="commenter.presentationName"/></span> <fr:view name="comment" property="date"/>
			</p>
			<div class="body"><fr:view name="comment" property="comment" layout="null-as-label" type="java.lang.String"/></div>
		</div>
	</logic:iterate>
	</div>
</logic:notEmpty>

<bean:define id="processOid" name="process" property="OID"/>


<fr:form action='<%= "/workflowProcessManagement.do?method=addComment&processId=" + processOid%>'>
	<table class="form">
		<tr>
			<td>
				<bean:message key="label.addComment" bundle="WORKFLOW_RESOURCES"/>:
			</td>
			<td>
				<fr:edit id="comment" name="bean" slot="string" type="java.lang.String" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
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
