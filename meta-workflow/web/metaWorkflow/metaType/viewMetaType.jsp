<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<h2><bean:message key="title.metaType.view" bundle="META_WORKFLOW_RESOURCES"/></h2>

<ul>
	<li>
		<html:link page="/metaTypeManagement.do?method=manageMetaType">
			<bean:message key="link.back" bundle="MYORG_RESOURCES"/>
		</html:link>
	</li>
</ul>

<bean:define id="description" name="metaType" property="currentDescription"/>
	
<table class="table">
	<tr>
		<th><bean:message key="label.metaType.name" bundle="META_WORKFLOW_RESOURCES"/></th>
		<th><bean:message key="label.organizational.structure" bundle="ORGANIZATION_RESOURCES"/></th>
		<th><bean:message key="label.metaType.availableFileTypes" bundle="META_WORKFLOW_RESOURCES"/></th>
		<th><bean:message key="label.metaType.currentVersion" bundle="META_WORKFLOW_RESOURCES"/></th>
	</tr>
	<tr>
		<td><fr:view name="metaType" property="name"/></td>
		<td><fr:view name="metaType" property="organizationalModel.name"/></td>
		<td>	
			<logic:empty name="metaType" property="availableFileTypes">
				-
			</logic:empty>
			<logic:notEmpty name="metaType" property="availableFileTypes">
				<fr:view name="metaType" property="availableFileTypes" >
					<fr:layout name="separator-list">
						<fr:property name="separator" value=","/>
						<fr:property name="eachLayout" value="name-resolver"/>
					</fr:layout>	
				</fr:view>
			</logic:notEmpty>
		</td>
		<td>
			<fr:view name="description" property="version"/>
		</td>
	</tr>
	<tr>
		<th colspan="4"><bean:message key="label.metaType.description" bundle="META_WORKFLOW_RESOURCES"/></th>
	</tr>
	<tr>
		<td colspan="4" class="aleft">
			<logic:notEmpty name="historyVersion">
			<fr:view name="historyVersion" property="description"/>
			<p>
				<em>
					<span class="aright">
					<bean:define id="version" name="historyVersion" property="version"/>
					<bean:define id="versionOwner" name="historyVersion" property="versionOwner.presentationName"/>
					<bean:define id="date">
						<fr:view name="historyVersion" property="date"/>
					</bean:define>
						<bean:message key="label.metaTypeDescription.by" bundle="META_WORKFLOW_RESOURCES" arg0="<%= version.toString() %>" arg1="<%= versionOwner.toString() %>" arg2="<%= date.toString()%>"/>
					</span>
				</em>
			</p>
			</logic:notEmpty>
			<logic:empty name="historyVersion">
				<fr:view name="description" property="description"/>
				<p>
					<em>
						<span class="aright">
						<bean:define id="version" name="description" property="version"/>
						<bean:define id="versionOwner" name="description" property="versionOwner.presentationName"/>
						<bean:define id="date">
							<fr:view name="description" property="date"/>
						</bean:define>
							<bean:message key="label.metaTypeDescription.by" bundle="META_WORKFLOW_RESOURCES" arg0="<%= version.toString() %>" arg1="<%= versionOwner.toString() %>" arg2="<%= date.toString()%>"/>
						</span>
					</em>
				</p>
			</logic:empty>
		</td>
	</tr>
	
</table>

<bean:size id="descriptionCount" name="metaType" property="descriptions"/>

<logic:greaterThan name="descriptionCount" value="1" scope="page">
<h3>
	<bean:message key="title.metaType.descriptionHistory" bundle="META_WORKFLOW_RESOURCES"/>
</h3>

		<bean:define id="metaTypeId" name="metaType" property="externalId" type="java.lang.String"/>
		<fr:form id="diffForm" action="<%= "/metaTypeManagement.do?method=doDiff&metaTypeId=" + metaTypeId%>">
		
		<html:hidden styleId="rev1" property="rev1" value=""/>
		<html:hidden styleId="rev2" property="rev2" value=""/>
	
		<table class="table">
		<tr>
			<th><bean:message key="label.metaType.version" bundle="META_WORKFLOW_RESOURCES"/></th>
			<th><bean:message key="label.metaType.versionOwner" bundle="META_WORKFLOW_RESOURCES"/></th>
			<th><bean:message key="label.metaType.date" bundle="META_WORKFLOW_RESOURCES"/></th>
			<th></th>
		</tr>
	
		<logic:iterate id="description" name="metaType" property="orderedDescriptionHistory">
			<bean:define id="version" name="description" property="version"/>
			<tr>
				<td><html:link page="<%="/metaTypeManagement.do?method=viewVersion&version=" + version + "&metaTypeId=" + metaTypeId %>"><fr:view name="version"/></html:link></td>
				<td><fr:view name="description" property="versionOwner.presentationName"/></td>
				<td><fr:view name="description" property="date"/></td>
				<td>
					<input class="diff1" type="radio" name="rev1" value="<%= version%>"/>
					<input class="diff2" type="radio" name="rev2" value="<%= version%>"/>
				</td>
			</tr>
		</logic:iterate>
		</table>
		
		<a href="#" onClick="javascript:doDiff();"><bean:message key="label.doDiff" bundle="META_WORKFLOW_RESOURCES"/></a>
		
		<script type="text/javascript">
			function doDiff() {
				$("#rev1").attr('value',$("input[name='rev1']:checked").val());
				$("#rev2").attr('value',$("input[name='rev2']:checked").val());
				$("#diffForm").submit();
			}
		</script>
	
		</fr:form>
	
	<logic:notEmpty name="version1">
		
		<script type="text/javascript">
			$("input[name='rev1'][value='<fr:view name="version1" property="version"/>']").attr('checked',true);
			$("input[name='rev2'][value='<fr:view name="version2" property="version"/>']").attr('checked',true);
			
		</script>
		
		<fr:view name="version1" property="version"/> => <fr:view name="version2" property="version"/>
		<fr:view name="diff">
			<fr:layout name="showDiff">
				<fr:property name="allDoc" value="true"/>
				<fr:property name="addWord" value="on"/>
				<fr:property name="removeWord" value="off"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>

</logic:greaterThan>