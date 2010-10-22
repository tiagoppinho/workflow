<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/workflow.tld" prefix="wf"%>


<%@page import="myorg.presentationTier.servlets.filters.contentRewrite.ContentContextInjectionRewriter"%>
<%@page import="pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter"%><bean:define id="processOID" name="process" property="externalId" type="java.lang.String"/>

<logic:equal name="process" property="open" value="false">
	<div class="highlightBox mtop05 mbottom15">
		<bean:message key="label.message.processIsClosed" bundle="META_WORKFLOW_RESOURCES"/>
	</div>
</logic:equal>

<script type="text/javascript">
	$(function() {
		$("#tabs").tabs();
	});
	</script>

<div>
	
	<div id="tabs">
		<%= ContentContextInjectionRewriter.BLOCK_HAS_CONTEXT_PREFIX %>
		<ul>
			<li><%= GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#tabs-1"><bean:message key="label.tab.information" bundle="META_WORKFLOW_RESOURCES"/></a></li>
			<bean:size id="observersCount" name="process" property="observers"/>
			<li><%= GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#tabs-2"><bean:message key="label.tab.observers" bundle="META_WORKFLOW_RESOURCES"/> (<fr:view name="observersCount"/>)</a></li>
			<li><%= GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#tabs-3"><bean:message key="label.tab.rules" bundle="META_WORKFLOW_RESOURCES"/></a></li>
		</ul>
		<%= ContentContextInjectionRewriter.END_BLOCK_HAS_CONTEXT_PREFIX %>
		<div id="tabs-1">
			<fr:view name="process" schema="view.meta.process.details">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1 thlight thtop thnowrap thright"/>
				</fr:layout>	
			</fr:view>
		</div>
		
		<div id="tabs-2">
			
			<logic:empty name="process" property="observers">
				<p class="mvert05"><em><bean:message key="label.noObserversSpecified" bundle="META_WORKFLOW_RESOURCES"/>.</em></p>
			</logic:empty>

			<logic:notEmpty name="process" property="metaTypeObservers">
				<p class="mvert05"><bean:message key="label.metaTypeObservers" bundle="META_WORKFLOW_RESOURCES"/>:</p>
			</logic:notEmpty>

			<ul class="mvert05">
				<logic:iterate id="observer" name="process" property="metaTypeObservers">		
					<bean:define id="userPresentation" name="observer" property="presentationName"/>
					<bean:define id="userID" name="observer" property="externalId" type="java.lang.String"/>
					<li class="mvert025">
						<span>
							<fr:view name="userPresentation"/>
						</span>
					</li>
				</logic:iterate>
			</ul>

			<logic:notEmpty name="process" property="processObservers">
				<p class="mtop1 mbottom05"><bean:message key="label.processObservers" bundle="META_WORKFLOW_RESOURCES"/>:</p>
			</logic:notEmpty>
			
			<ul class="mvert05">
				<logic:iterate id="observer" name="process" property="processObservers">			
					<bean:define id="userPresentation" name="observer" property="presentationName"/>
					<bean:define id="userID" name="observer" property="externalId" type="java.lang.String"/>
					<li class="mvert025">
						<span>
							<fr:view name="userPresentation"/>
							<wf:activityLink id="<%= "remove-" + userID %>" processName="process" activityName="RemoveObserver" scope="request" paramName0="user" paramValue0="<%= userID%>">
								<bean:message bundle="MYORG_RESOURCES" key="link.remove"/>
							</wf:activityLink>					
							<script type="text/javaScript">
							 linkConfirmationHook("<%= "remove-" + userID %>", '<bean:message key="label.confirm.removal" bundle="META_WORKFLOW_RESOURCES" arg0="<%= userPresentation.toString() %>"/>', ' <bean:message key="title.removeObserver" bundle="META_WORKFLOW_RESOURCES"/>');
							</script>
						</span>
					</li>
				</logic:iterate>
			</ul>	
			

			
			<p class="mtop15 mbottom05">
				<wf:activityLink processName="process" activityName="AddObserver" scope="request">
					+ <bean:message key="activity.AddObserver" bundle="WORKFLOW_RESOURCES"/>
				</wf:activityLink>
			</p>
			
		</div>
		
		<div id="tabs-3">
			<bean:define id="currentDescription" name="process" property="metaType.currentDescription"/>
			<div class="mtop05 mbottom15">
				<fr:view name="currentDescription" property="description" layout="html"/>
			</div>
			<p class="mbottom05">
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
				<bean:define id="metaTypeId" name="process" property="metaType.externalId" type="java.lang.String"/>
				(<html:link page="<%= "/metaWorkflow.do?method=viewMetaTypeDescriptionHistory&processId="+ processOID + "&metaTypeId=" + metaTypeId %>">
					<bean:message key="link.history" bundle="META_WORKFLOW_RESOURCES"/>
				</html:link>)
			</p>
		</div>
	</div>
</div>

<h3 class="mbottom05"><bean:message key="title.comments" bundle="WORKFLOW_RESOURCES"/></h3>

<div>
	<fr:view name="process" layout="viewComments">
		<fr:layout name="viewComments">
			<fr:property name="commentBlockClasses" value="comment"/>
		</fr:layout>
	</fr:view>

	<logic:present name="commentBean">
		<fr:form action='<%= "/metaWorkflow.do?method=addComment&processId=" + processOID %>'>
			<div class="addcomment" style="-moz-border-radius-bottomleft: 6px; -moz-border-radius-bottomright: 6px; -webkit-border-bottom-left-radius: 6px; -webkit-border-bottom-right-radius: 6px; padding-left: 130px;">
				<p class="mbottom05"><bean:message key="label.comment" bundle="WORKFLOW_RESOURCES"/>:</p>
				<fr:edit id="comment" name="commentBean" slot="string" type="java.lang.String" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:layout name="longText">
						<fr:property name="rows" value="6"/>
						<fr:property name="columns" value="60"/>
						<fr:property name="classes" value="form"/>
					</fr:layout>
				</fr:edit>
				<p><html:submit styleClass="inputbutton"><bean:message key="label.addComment" bundle="WORKFLOW_RESOURCES"/></html:submit></p>
			</div>
		</fr:form>
	</logic:present>	
</div>