<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h2> <bean:message key="link.topBar.nodeSelectionConfiguration" bundle="WORKFLOW_RESOURCES"/> </h2>

<strong><bean:message key="label.mappedProcesses" bundle="WORKFLOW_RESOURCES"/></strong>:

<logic:notEmpty name="mappers">
	<ul>
	<logic:iterate id="mapperToEdit" name="mappers">
		<li>
			<html:link page="/processSelectionNodeConfiguration.do?method=manageNodeSelection" paramId="mapperId" paramName="mapperToEdit" paramProperty="externalId">
				<fr:view name="mapperToEdit" property="mappedClass" layout="name-resolver"/>
			</html:link>
			-
			<html:link page="/processSelectionNodeConfiguration.do?method=deleteMapper" paramId="mapperToDeleteId" paramName="mapperToEdit" paramProperty="externalId">
				<bean:message key="link.delete" bundle="MYORG_RESOURCES"/>
			</html:link>
		</li>
	</logic:iterate>
	</ul>
</logic:notEmpty>

<div>
	<fr:form action="/processSelectionNodeConfiguration.do?method=newMapper">
				<fr:edit id="newMapper" name="newMapper">
						<fr:schema bundle="WORKFLOW_RESOURCES" type="myorg.util.VariantBean">
							<fr:slot name="object" layout="menu-select" key="label.createNewMapper">
								<fr:property name="providerClass" value="module.workflow.presentationTier.renderers.providers.ProcessesClassesProvider"/>
								<fr:property name="eachLayout" value="name-resolver"/>
							</fr:slot>
						</fr:schema>
						<fr:layout>
						</fr:layout>					
				</fr:edit>
				<html:submit styleClass="inputbutton"><bean:message key="renderers.form.submit.name" bundle="RENDERER_RESOURCES"/></html:submit>
	</fr:form>
</div>

<logic:notEmpty name="mapper">
	<p>
	<strong>
		<fr:view name="mapper" property="classname"/>:
	</strong>
	</p>
	
	<bean:define id="mapperId" name="mapper" property="externalId"/>
	<bean:message key="label.currentProcessMappingPath" bundle="WORKFLOW_RESOURCES"/>:
	<logic:iterate id="nodeMapping" name="mapper" property="orderedNodeMappings" indexId="index">
		<logic:greaterThan name="index" value="0">
			&gt;
		</logic:greaterThan> 
		<fr:view name="nodeMapping" property="node.link" type="java.lang.String"/>
		<html:link page="<%= "/processSelectionNodeConfiguration.do?method=removeNodeSelection&index=" + index.toString() + "&mapperId=" + mapperId %>">
			(<bean:message key="link.remove" bundle="MYORG_RESOURCES"/>)
		</html:link>
	</logic:iterate>
	<p>
		<logic:notEmpty name="nodes">
		<ul>
		<logic:iterate id="node" name="nodes">
			<bean:define id="nodeId" name="node" property="externalId"/>
			<li><html:link page="<%= "/processSelectionNodeConfiguration.do?method=addNodeSelection&nodeId=" + nodeId + "&mapperId=" + mapperId %>">
				<bean:message key="link.add" bundle="MYORG_RESOURCES"/> <bean:write name="node" property="link"/>
			</html:link></li>
		</logic:iterate>
		</ul>
		</logic:notEmpty>
	</p>
</logic:notEmpty>