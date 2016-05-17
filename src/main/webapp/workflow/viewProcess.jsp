<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/workflow" prefix="wf"%>

<%@page import="module.workflow.activities.TakeProcess"%>
<%@page import="module.workflow.activities.StealProcess"%>
<%@page import="module.workflow.activities.ReleaseProcess"%>
<%@page import="module.workflow.activities.GiveProcess"%>

<script src="${pageContext.request.contextPath}/bennu-renderers/js/jquery.alerts.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/bennu-renderers/js/alertHandlers.js" type="text/javascript"></script>

<bean:define id="processId" name="process" property="externalId"  type="java.lang.String"/>
<bean:define id="processClassName" name="process" property="class.name" type="java.lang.String"/>
<bean:define id="includeFolder" value="<%= processClassName.replace('.','/')%>"/>

<logic:present name="signatureNotification">
	<div class="infobox_warning">
	 	<p class="mvert025">
	 		<bean:write name="signatureNotification" />
	 	</p>
	 </div>
</logic:present>

<jsp:include page='${context.workflowHead}'/>

 
<logic:present name="process" property="currentOwner">
	<bean:define id="ownerName" name="process" property="currentOwner.displayName"/>
	<div class="alert alert-warning">
		<bean:message key="message.info.currentOwnerIs" bundle="WORKFLOW_RESOURCES" arg0="<%= ownerName.toString() %>"/>
	</div>
</logic:present>

<p>
	<logic:equal name="process" property="createdByAvailable" value="true">					
		<span class="glyphicon glyphicon-calendar"></span> Criado em <fr:view name="process" property="creationDate" layout="no-time"/>
		<span class="glyphicon glyphicon-user"></span> Criado por ${process.processCreator.displayName} (${process.processCreator.username})
	</logic:equal>

	<logic:equal name="process" property="commentsSupportAvailable" value="true">
		<logic:equal name="process" property="commentsDisplayedInBody" value="false">
			<bean:size id="comments"  name="process" property="comments"/>		 
			<span class="glyphicon glyphicon-comment"></span> 
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


<div class="proccess_main row">
	<div class="col-sm-6">
		<div class="panel panel-default">
			<div class="panel-heading">
				<strong><bean:message key="label.activities" bundle="WORKFLOW_RESOURCES"/></strong>
			</div>
			<div class="activities panel-body">

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
	</div>
	<!-- activities box -->
		
	<logic:equal name="process" property="fileSupportAvailable" value="true">
		<div class="col-sm-6">
			<div class="panel panel-default">
				<div class="panel-heading">
					<strong><bean:message key="label.documents" bundle="WORKFLOW_RESOURCES"/></strong>
				</div>
				<div class="panel-body">
					<fr:view name="process">
						<fr:layout name="processFiles">
							<fr:property name="classes" value=""/>
						</fr:layout>
					</fr:view>
					<div>
						<html:link page="/workflowProcessManagement.do?method=viewFilesDetails" paramId="processId" paramName="process" paramProperty="externalId">
							<bean:message key="link.viewFilesDetails" bundle="WORKFLOW_RESOURCES"/>
						</html:link>
						<logic:equal name="process" property="fileEditionAllowed" value="true">
								<span class="pull-right">
									<a href="${pageContext.request.contextPath}/workflowProcessManagement.do?method=fileUpload&processId=${processId}"
									   class="btn btn-primary">
										<bean:message key="link.uploadFile" bundle="WORKFLOW_RESOURCES"/>
									</a>
								</span>
						</logic:equal>
					</div>
				</div>
			</div>
		</div>
		<!-- file upload box -->
	</logic:equal>
</div>

<div class="clearfix"></div>


<jsp:include page='${context.workflowBody}'/>

<logic:equal name="process" property="commentsSupportAvailable" value="true">
	<logic:equal name="process" property="commentsDisplayedInBody" value="true">
		<jsp:include page="/workflow/viewComments.jsp">
			<jsp:param value="true" name="displayedInline"/>
			<jsp:param value="<%=processId%>" name="processId"/>
		</jsp:include>
	</logic:equal>
</logic:equal>
