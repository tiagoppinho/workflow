<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<h2>
	<bean:message key="title.search" bundle="META_WORKFLOW_RESOURCES"/>
</h2>

<logic:notPresent name="advancedSearchBean">

	<span>
		<bean:message key="link.simpleSearch" bundle="META_WORKFLOW_RESOURCES"/>
		|
		<html:link page="/metaWorkflow.do?method=advancedSearch">
			<bean:message key="link.advancedSearch" bundle="META_WORKFLOW_RESOURCES"/>
		</html:link>
		</span>
	<div class="mtop15">
		<fr:form action="/metaWorkflow.do?method=search">
			<table>
			<tr>
			<th>
				<bean:message key="label.searchFor" bundle="META_WORKFLOW_RESOURCES"/>:
			</th>
			<td>
				<fr:edit id="searchQuery" name="searchBean" slot="string" type="java.lang.String">
					<fr:layout>
						<fr:property name="size" value="70"/>
					</fr:layout>
				</fr:edit>
			</td>
			</tr>
			</table>
			<html:submit styleClass="inputbutton"><bean:message key="link.search" bundle="MYORG_RESOURCES"/></html:submit>	
		</fr:form>
	</div>
</logic:notPresent>


<logic:present name="advancedSearchBean">
	 
	<span>
		<html:link page="/metaWorkflow.do?method=search">
			<bean:message key="link.simpleSearch" bundle="META_WORKFLOW_RESOURCES"/>
		</html:link>
		|
		<bean:message key="link.advancedSearch" bundle="META_WORKFLOW_RESOURCES"/>
	</span>
	<div class="mtop05">
		<fr:form action="/metaWorkflow.do?method=advancedSearch">
			<fr:edit id="advancedSearch" name="advancedSearchBean" schema="search.advanced.metaWorkflow" /> 
		
			<html:submit styleClass="inputbutton"><bean:message key="link.search" bundle="MYORG_RESOURCES"/></html:submit>	
		</fr:form>
	</div>
</logic:present>


	
	<logic:present name="searchResult">
		<h3 class="mtop15 mbottom075"><bean:message key="label.searchResults" bundle="META_WORKFLOW_RESOURCES"/></h3>
	
	<logic:empty name="searchResult">
		<em><bean:message key="label.noSearchResults" bundle="META_WORKFLOW_RESOURCES"/></em>
	</logic:empty>
	
	<logic:notEmpty name="searchResult">
	
		<div>
			<table class="tstyle3">
				<tr>
					<th><bean:message key="label.metaProcess.subject" bundle="META_WORKFLOW_RESOURCES"/></th>
					<th><bean:message key="label.metaProcess.type" bundle="META_WORKFLOW_RESOURCES"/></th>
					<th><bean:message key="label.processNumber" bundle="WORKFLOW_RESOURCES"/></th>
					<th><bean:message key="label.metaProcess.currentQueues" bundle="META_WORKFLOW_RESOURCES"/></th>
					<th><bean:message key="label.metaProcess.active" bundle="META_WORKFLOW_RESOURCES"/></th>
					<th><bean:message key="label.metaProcess.requestor" bundle="META_WORKFLOW_RESOURCES"/></th>
					<th><bean:message key="label.metaProcess.currentOwner" bundle="META_WORKFLOW_RESOURCES"/></th> 
				</tr>
				<logic:iterate id="process" name="searchResult" indexId="i">
					<logic:equal name="process" property="accessibleToCurrentUser" value="true">
					<tr>
					<td>
						<bean:define id="processId" name="process" property="externalId" type="java.lang.String"/>
						<html:link page="<%= "/workflowProcessManagement.do?method=viewProcess&processId=" + processId %>" > 
							<fr:view name="process" property="subject"/>
						</html:link>
						</td>
					<td> <fr:view name="process" property="metaType.name"/></td>
					<td> <fr:view name="process" property="processNumber"/></td>
					<td class="acenter">
						<fr:view name="process" layout="values">
							<fr:schema bundle="META_WORKFLOW_RESOURCES" type="module.metaWorkflow.domain.WorkflowMetaProcess">
								<fr:slot name="currentQueuesOrdered" layout="flowLayout" key="label.metaProcess.currentQueues">
									<fr:property name="eachLayout" value="values"/>
									<fr:property name="htmlSeparator" value=", "/>
									<fr:property name="eachSchema" value="view.queue.name"/>
								</fr:slot>
							</fr:schema>
						 </fr:view> 
					</td>
					<td><fr:view name="process" property="open"/></td>
					<td class="acenter"> <fr:view name="process" property="requestor" type="module.metaWorkflow.domain.Requestor">
							<fr:layout name="null-as-label">
								<fr:property name="subLayout" value="values"/>
								<fr:property name="subSchema" value="view.requestor.shortName"/>
							</fr:layout>
						 </fr:view> 
					</td>
					<td class="acenter"> <fr:view name="process" property="currentOwner" type="myorg.domain.User">
							<fr:layout name="null-as-label">
								<fr:property name="subLayout" value="values"/>
								<fr:property name="subSchema" value="view.user.shortPresentationName"/>
							</fr:layout>
						 </fr:view> 
					</td>
					</tr>
					</logic:equal>
				</logic:iterate>	
			</table>
		</div>
	</logic:notEmpty>
	</logic:present>