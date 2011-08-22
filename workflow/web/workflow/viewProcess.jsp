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


<%
	final WorkflowLayoutContext layoutContext = (WorkflowLayoutContext) ContextBaseAction.getContext(request);
%>

<logic:present name="signatureNotification">
	<div class="infobox_warning">
	 	<p class="mvert025">
	 		<bean:write name="signatureNotification" />
	 	</p>
	 </div>
</logic:present>

<jsp:include page='<%=  layoutContext.getWorkflowHead() %>'/>

 
<logic:present name="process" property="currentOwner">
	<bean:define id="ownerName" name="process" property="currentOwner.presentationName"/>
	<div class="highlightBox">
		<bean:message key="message.info.currentOwnerIs" bundle="WORKFLOW_RESOURCES" arg0="<%= ownerName.toString() %>"/>
	</div>
</logic:present>

<p>
	<logic:equal name="process" property="createdByAvailable" value="true">					
		<span class="post">Criado em <fr:view name="process" property="creationDate" layout="no-time"/></span> 
		<span class="author">Criado por
			<logic:present name="process" property="processCreator.shortPresentationName">
				<fr:view name="process" property="processCreator.shortPresentationName"/> 
			</logic:present>
		</span>
	</logic:equal>
	
	<%--
	<logic:greaterThan name="count" value="1">
		<bean:message key="label.unreadComments.info.moreThanOne" arg0="<%= count.toString() %>" bundle="WORKFLOW_RESOURCES"/>
	</logic:greaterThan>
	<logic:equal name="count" value="1">
		<bean:message key="label.unreadComments.info" arg0="<%= count.toString() %>" bundle="WORKFLOW_RESOURCES"/>
	</logic:equal>
	--%>
				
	<logic:equal name="process" property="commentsSupportAvailable" value="true">
		<logic:equal name="process" property="commentsDisplayedInBody" value="false">
			<bean:size id="comments"  name="process" property="comments"/>		 
			<span class="comments">
				<html:link page="/workflowProcessManagement.do?method=viewComments" paramId="processId" paramName="process" paramProperty="externalId">
				<%= comments %>
				<logic:equal name="comments" value="1">
					<bean:message key="link.comment" bundle="WORKFLOW_RESOURCES"/>
				</logic:equal>
				<logic:notEqual name="comments" value="1">
					<bean:message key="link.comments" bundle="WORKFLOW_RESOURCES"/>
				</logic:notEqual>
				</html:link>
				<bean:define id="unreadComments" name="process" property="unreadCommentsForCurrentUser"/>
				<logic:notEmpty name="unreadComments">
					<bean:size id="count" name="unreadComments"/> (<%= count.toString() %> novos) 
				</logic:notEmpty>
			</span>
		</logic:equal>
	</logic:equal>
</p>


<logic:messagesPresent property="message" message="true">
	<div class="error1 mtop15px mbottom10px">
		<html:messages id="errorMessage" property="message" message="true"> 
			<span><fr:view name="errorMessage"/></span>
		</html:messages>
	</div>
</logic:messagesPresent>


<table class="proccess_main">
	<tr>
		<td>
		
			<div class="infobox1 col2-1">
				<h3><bean:message key="label.activities" bundle="WORKFLOW_RESOURCES"/></h3>
				<div class="activities">
					
					<ul id="main-activities">
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
					
					<!--
					<p class="mtop15 mbottom05">
						<b><bean:message key="label.otherOperations" bundle="WORKFLOW_RESOURCES"/></b>
					</p>
					 -->
					
					<ul id="other-activities" style="margin-top: 5px;">
					
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
					<logic:equal name="process" property="currentUserCanViewLogs" value="true"> 
						<li>
							<html:link page="/workflowProcessManagement.do?method=viewLogs" paramId="processId" paramName="process" paramProperty="externalId">
								<bean:message key="link.viewLogs" bundle="WORKFLOW_RESOURCES"/>
							</html:link>
						</li>
					</logic:equal>	
					</ul>
					
				</div>
				<!-- #activities -->
			</div>
			<!-- infobox1 -->

		</td>
		
		
		
		<logic:equal name="process" property="fileSupportAvailable" value="true">
	
			<td class="gutter"></td>
	
			<td>	
				<div class="infobox1 col2-2">
					<h3><bean:message key="label.documents" bundle="WORKFLOW_RESOURCES"/> <!--<a href="">(9)</a>--></h3>
					<div>

						<fr:view name="process">
							<fr:layout name="processFiles">
								<fr:property name="classes" value=""/>
							</fr:layout>
						</fr:view>
						
						<!--
						<table class="process-files">
							<tr>
								<th>18/03/2010</th>
								<td>Documento - <a href="">suspendisse_egestas.doc</a></td>
								<td><a href="">(X)</a></td>
							</tr>
						</table>
						<table class="structural mvert0">
							<tr>
								<td><a href="">Gerir ficheiros (9)</a></td>
								<td class="aright">
									<input type="button" value="+ Adicionar Ficheiro"/>
								</td>
							</tr>
						</table>
						-->
						
						
						<table class="structural mvert0">
							<tr>
								<td>
									<html:link page="/workflowProcessManagement.do?method=viewRemovedFiles" paramId="processId" paramName="process" paramProperty="externalId">
										<bean:message key="link.viewRemovedFiles" bundle="WORKFLOW_RESOURCES"/>
									</html:link>
								</td>
								<td class="aright">

									<form action="<%= request.getContextPath() + "/workflowProcessManagement.do" %>" method="post">
										<input type="hidden" name="method" value="fileUpload"/>
										<input type="hidden" name="processId" value="<%= processId %>"/>

										<html:submit><bean:message key="link.uploadFile" bundle="WORKFLOW_RESOURCES"/></html:submit>
									</form>
								
									<%--
									<input id="addFileButton" type="button" value="<bean:message key="link.uploadFile" bundle="WORKFLOW_RESOURCES"/>"/>
									<script type="java/textscript">
										$("#addFileButton").click(function() {
										window.location = <%= "/workflowProcessManagement.do?method=fileUpload&processId="+processId %>;
										});
									</script>
									--%>
								</td>
							</tr>
						</table>

					</div>
				</div>
				<!-- infobox1 -->

			</td>

		</logic:equal>
	</tr>
</table>

<div class="clear"></div>


<jsp:include page='<%=  layoutContext.getWorkflowBody() %>'/>

<logic:equal name="process" property="commentsSupportAvailable" value="true">
	<logic:equal name="process" property="commentsDisplayedInBody" value="true">
		<jsp:include page="/workflow/viewComments.jsp">
			<jsp:param value="true" name="displayedInline"/>
			<jsp:param value="<%=processId%>" name="processId"/>
		</jsp:include>
	</logic:equal>
</logic:equal>
