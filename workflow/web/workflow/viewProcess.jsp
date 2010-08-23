<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/workflow.tld" prefix="wf"%>

<%@page import="myorg.presentationTier.LayoutContext"%>
<%@page import="module.workflow.presentationTier.WorkflowLayoutContext"%>
<%@page import="myorg.presentationTier.actions.ContextBaseAction"%>
<%@page import="module.workflow.activities.TakeProcess"%>
<%@page import="module.workflow.activities.StealProcess"%>
<%@page import="module.workflow.activities.ReleaseProcess"%>
<%@page import="module.workflow.activities.GiveProcess"%><script src="<%= request.getContextPath() + "/javaScript/jquery.alerts.js"%>" type="text/javascript"></script> 

<script src="<%= request.getContextPath() + "/javaScript/alertHandlers.js"%>" type="text/javascript"></script> 
 
<bean:define id="processId" name="process" property="externalId"  type="java.lang.String"/>
<bean:define id="processClassName" name="process" property="class.name" type="java.lang.String"/>
<bean:define id="includeFolder" value="<%= processClassName.replace('.','/')%>"/>

<logic:messagesPresent property="message" message="true">
	<div class="error1">
		<html:messages id="errorMessage" property="message" message="true"> 
			<span><fr:view name="errorMessage"/></span>
		</html:messages>
	</div>
</logic:messagesPresent>
<%
	final WorkflowLayoutContext layoutContext = (WorkflowLayoutContext) ContextBaseAction.getContext(request);
%>

<jsp:include page='<%=  layoutContext.getWorkflowHead() %>'/>

<div id="processControl"> 
<logic:present name="process" property="currentOwner">
	<bean:define id="ownerName" name="process" property="currentOwner.presentationName"/>
	<div class="highlightBox">
		<bean:message key="message.info.currentOwnerIs" bundle="WORKFLOW_RESOURCES" arg0="<%= ownerName.toString() %>"/>
	</div>
</logic:present>

<div style="float: left; width: 100%">
<table style="width: 100%; margin: 1em 0;">
	<tr>
		<td style="border: 1px dotted #aaa; padding: 10px 15px; width: 48%; vertical-align: top;">
			<p class="mtop0 mbottom05">
				<b><bean:message key="label.activities" bundle="WORKFLOW_RESOURCES"/></b>
			</p>
			
			<ul class="operations mtop0">
				<logic:iterate id="activity" name="process" property="activeActivities">
					<logic:equal name="activity" property="visible" value="true">
						<bean:define id="name" name="activity" property="class.simpleName" type="java.lang.String"/>
						<li>
							<wf:activityLink id="<%= name %>" processName="process" activityName="<%= name %>" scope="request">
								<wf:activityName processName="process" activityName="<%= name %>" scope="request"/>
							</wf:activityLink>
						</li>
					</logic:equal>
				</logic:iterate>
			</ul>
			
			<logic:empty name="process" property="activeActivities">
				<p class="mvert05">
					<em>
						<bean:message key="messages.info.noOperatesAvailabeATM" bundle="WORKFLOW_RESOURCES"/>.
					</em>
				</p>
			</logic:empty>
			
			<p class="mtop15 mbottom05">
				<b><bean:message key="label.otherOperations" bundle="WORKFLOW_RESOURCES"/></b>
			</p>
			<ul class="operations mbottom05">
				<li>
					<html:link page="/workflowProcessManagement.do?method=viewLogs" paramId="processId" paramName="process" paramProperty="externalId">
						<bean:message key="link.viewLogs" bundle="WORKFLOW_RESOURCES"/>
					</html:link>
				</li>
	
				<logic:equal name="process" property="ticketSupportAvailable" value="true">
				<li>
					<wf:activityLink id="take-process" processName="process" activityName="<%=  TakeProcess.class.getSimpleName() %>" scope="request">
								<wf:activityName processName="process" activityName="<%= TakeProcess.class.getSimpleName() %>" scope="request"/>
					</wf:activityLink>
						<wf:activityLink id="steal-process" processName="process" activityName="<%=  StealProcess.class.getSimpleName() %>" scope="request">
								<wf:activityName processName="process" activityName="<%= StealProcess.class.getSimpleName() %>" scope="request"/>
					</wf:activityLink>
					<wf:activityLink id="release-process" processName="process" activityName="<%=  ReleaseProcess.class.getSimpleName() %>" scope="request">
								<wf:activityName processName="process" activityName="<%= ReleaseProcess.class.getSimpleName() %>" scope="request"/>
					</wf:activityLink>
				</li>	
				
					<wf:isActive processName="process" activityName="<%= GiveProcess.class.getSimpleName() %>" scope="request">
					<li>
						<wf:activityLink id="give-process" processName="process" activityName="<%=  GiveProcess.class.getSimpleName() %>" scope="request">
									<wf:activityName processName="process" activityName="<%= GiveProcess.class.getSimpleName() %>" scope="request"/>
						</wf:activityLink>
					</li>
					</wf:isActive>
				</logic:equal>	
				
				<logic:equal name="process" property="commentsSupportAvailable" value="true">
					<bean:size id="comments"  name="process" property="comments"/>
					<li> 
						<html:link page="/workflowProcessManagement.do?method=viewComments" paramId="processId" paramName="process" paramProperty="externalId">
							<bean:message key="link.comments" bundle="WORKFLOW_RESOURCES"/> (<%= comments %>)
						</html:link>	
					</li>
				</logic:equal>
			</ul>
		</td>
		
		<logic:equal name="process" property="fileSupportAvailable" value="true">
	
			<td style="border: none; width: 2%; padding: 0;"></td>
	
			<td style="border: 1px dotted #aaa; padding: 10px 15px; width: 48%; vertical-align: top;">
			
				<p class="mtop0 mbottom05">
					<b><bean:message key="label.documents" bundle="WORKFLOW_RESOURCES"/></b>
				</p>
				
				<div class="documents mtop0 mbottom05" style="overflow: hidden; ">
					<fr:view name="process">
						<fr:layout name="processFiles">
							<fr:property name="classes" value=""/>
						</fr:layout>
					</fr:view>
				</div>
				
				<ul class="operations mtop1">
					<li>
						<html:link page="/workflowProcessManagement.do?method=fileUpload" paramId="processId" paramName="process" paramProperty="externalId">
							<bean:message key="link.uploadFile" bundle="WORKFLOW_RESOURCES"/>
						</html:link>
					</li>
					<li>
						<html:link page="/workflowProcessManagement.do?method=viewRemovedFiles" paramId="processId" paramName="process" paramProperty="externalId">
							<bean:message key="link.viewRemovedFiles" bundle="WORKFLOW_RESOURCES"/>
						</html:link>
					</li>
				</ul>
			
			</td>

		</logic:equal>
	</tr>
</table>
</div>
<div style="clear: left;"></div>

<logic:equal name="process" property="commentsSupportAvailable" value="true">
	<bean:define id="unreadComments" name="process" property="unreadCommentsForCurrentUser"/>
	<logic:notEmpty name="unreadComments">
		<bean:size id="count" name="unreadComments"/>
			<div class="highlightBox mtop05 mbottom15">
			<p class="mvert025">
				<logic:greaterThan name="count" value="1">
					<bean:message key="label.unreadComments.info.moreThanOne" arg0="<%= count.toString() %>" bundle="WORKFLOW_RESOURCES"/>
				</logic:greaterThan>
				<logic:equal name="count" value="1">
					<bean:message key="label.unreadComments.info" arg0="<%= count.toString() %>" bundle="WORKFLOW_RESOURCES"/>
				</logic:equal>
				
				<html:link page="/workflowProcessManagement.do?method=viewComments" paramId="processId" paramName="process" paramProperty="externalId">
					<bean:message key="link.view.unreadComments" bundle="WORKFLOW_RESOURCES"/> »
				</html:link>
			</p>
		</div>
	</logic:notEmpty>
</logic:equal>
</div>

<jsp:include page='<%=  layoutContext.getWorkflowBody() %>'/>