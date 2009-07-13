<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<h2><bean:message key="title.metaType.descriptionHistory" bundle="META_WORKFLOW_RESOURCES"/></h2>

<bean:define id="processId" name="process" property="OID"/>

<html:link page="<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + processId %>">
	«  <bean:message key="link.backToProcess" bundle="WORKFLOW_RESOURCES"/>
</html:link>

<logic:notEmpty name="metaType" property="orderedDescriptionHistory">
	<fr:form id="diffForm" action="<%= "/metaWorkflow.do?method=doDiff&processId=" + processId %>">
	<html:hidden styleId="rev1" property="rev1" value=""/>
	<html:hidden styleId="rev2" property="rev2" value=""/>

	<table class="tstyle2">
	<tr>
		<th><bean:message key="label.metaType.version" bundle="META_WORKFLOW_RESOURCES"/></th>
		<th><bean:message key="label.metaType.versionOwner" bundle="META_WORKFLOW_RESOURCES"/></th>
		<th><bean:message key="label.metaType.date" bundle="META_WORKFLOW_RESOURCES"/></th>
		<th></th>
	</tr>

	<logic:iterate id="description" name="metaType" property="orderedDescriptionHistory">
		<bean:define id="version" name="description" property="version"/>
		<tr>
			<td><fr:view name="version"/></td>
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
</logic:notEmpty>


<logic:notEmpty name="version1">
	<table class="tstyle2">
		<tr>
			<th><bean:message key="label.metaType.version" bundle="META_WORKFLOW_RESOURCES"/> <fr:view name="version1" property="version"/></th>
			<th><bean:message key="label.metaType.version" bundle="META_WORKFLOW_RESOURCES"/> <fr:view name="version2" property="version"/></th>
		</tr>
	
		<tr>
			<td>
				<fr:view name="version1" property="description"/>
			</td>
			<td>
				<fr:view name="version2" property="description"/>
			</td>
		</tr>
		<tr>
			<td colspan="2"> 
				<fr:view name="diff"/>
			</td>	
		</tr>
		
	</table>
</logic:notEmpty>