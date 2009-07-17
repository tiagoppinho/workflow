<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<div class="infoop2" style="-moz-border-radius: 6px; -webkit-border-radius: 6px;">
		<strong><bean:message key="label.queue.create" bundle="META_WORKFLOW_RESOURCES"/></strong>:
		
		<fr:form id="form" action="/metaWorkflowQueueManagement.do">
			<html:hidden  property="method" value="createNewQueue"/>
			<fr:edit id="newQueue" name="bean" visible="false"/>
			
			<fr:edit id="standardFields" name="bean" schema="create.queue" action="/metaWorkflowQueueManagement?method=createNewQueue">
				<fr:layout name="tabular">
					<fr:property name="classes" value="form"/>
				</fr:layout>
			</fr:edit>
			<p>
				<strong> Utilizadores </strong>
			</p>
			<ul>
			<logic:iterate id="userAdded" name="bean" property="users" >
				<li><fr:view name="userAdded" property="presentationName"/></li>
			</logic:iterate>
			</ul>
			
			<table>
			<tr>
			<td>

				<fr:edit id="users" name="bean" slot="userToAdd">
					<fr:layout name="autoComplete">
						<fr:property name="labelField" value="username" />
						<fr:property name="format" value="${presentationName}" />
						<fr:property name="minChars" value="3" />
						<fr:property name="args"
							value="provider=myorg.presentationTier.renderers.autoCompleteProvider.UserAutoComplete" />
						<fr:property name="size" value="40" />
					</fr:layout>
				</fr:edit>
			</td>
			<td>
				<input id="addUser" type="button"  value="+"/> </input>
			</td>
			</tr>
			</table>	
			
			
			<script type="text/javascript">
				$("#addUser").click(function(){
					$("#form > input[name='method']").attr('value','oneMoreUserInQueue');
					$("#form").submit();
				});
			</script>
			<html:submit><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/></html:submit>
		</fr:form>
		
	</div>
