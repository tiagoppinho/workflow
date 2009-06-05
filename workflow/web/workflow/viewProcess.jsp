<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<script src="<%= request.getContextPath() + "/javaScript/jquery.alerts.js"%>" type="text/javascript"></script> 
<script src="<%= request.getContextPath() + "/javaScript/alertHandlers.js"%>" type="text/javascript"></script> 
 
<bean:define id="processId" name="process" property="OID" />
<bean:define id="processClassName" name="process" property="class.name" type="java.lang.String"/>
<bean:define id="includeFolder" value="<%= processClassName.replace('.','/')%>"/>

<jsp:include page='<%= "/" + includeFolder + "/header.jsp" %>'/>

 
<logic:present name="process" property="currentOwner">
	<bean:define id="ownerName" name="process" property="currentOwner.presentationName"/>
	<div class="infoop4">
		<bean:message key="message.info.currentOwnerIs" bundle="WORKFLOW_RESOURCES" arg0="<%= ownerName.toString() %>"/>
	</div>
</logic:present>

<table class="structural">
	<tr>
		<td style="width: 50%; padding-right: 1em; border: 1px dotted #aaa; padding: 10px 15px;">
			<p class="mtop0 mbottom05"><b style="color: #555;"><bean:message key="label.activities" bundle="WORKFLOW_RESOURCES"/></b></p>
			<ul class="operations mtop0">
			<logic:iterate id="activity" name="process" property="activeActivities">
				<bean:define id="name" name="activity" property="name" />
				<li>
				<bean:define id="activityName" name="activity" property="localizedName"/>
				<html:link styleId="<%= name.toString() %>"
					page='<%="/workflowProcessManagement.do?method=process&activity=" + name + "&processId=" + processId%>'>
					<fr:view name="activityName"/>
					<logic:equal name="activity" property="confirmationNeeded" value="true">
						<bean:define id="message" name="activity" property="localizedConfirmationMessage"/>
						  <script type="text/javascript"> 
				   				linkConfirmationHook('<%= name %>', '<%= message %>','<%= activityName %>'); 
				 		</script> 
					</logic:equal>
				</html:link>
				</li>
			</logic:iterate>
			</ul>
			<logic:empty name="process" property="activeActivities">
				<p>
					<em>
						<bean:message key="messages.info.noOperatesAvailabeATM" bundle="WORKFLOW_RESOURCES"/>.
					</em>
				</p>
			</logic:empty>
			
			<p>
				<strong><bean:message key="label.otherOperations" bundle="WORKFLOW_RESOURCES"/></strong>
				<ul class="operations">
					<li>
						<html:link page="/workflowProcessManagement.do?method=viewLogs" paramId="processId" paramName="process" paramProperty="OID">
							<bean:message key="link.viewLogs" bundle="WORKFLOW_RESOURCES"/>
						</html:link>
					</li>
				
					<logic:equal name="process" property="commentsSupportAvailable" value="true">
						<bean:size id="comments"  name="process" property="comments"/>
						<li> 
							<html:link page="/workflowProcessManagement.do?method=viewComments" paramId="processId" paramName="process" paramProperty="OID">
								<bean:message key="link.comments" bundle="WORKFLOW_RESOURCES"/> (<%= comments %>)
							</html:link>	
						</li>
					</logic:equal>
				</ul>
			</p>
		</td>
		
		<logic:equal name="process" property="fileSupportAvailable" value="true">
				
		<td style="width: 2%;"></td>
			
			<td style="width: 45%; border: 1px dotted #aaa; padding: 10px 15px;">
				<p class="mtop0 mbottom05"><b style="color: #555;"><bean:message key="label.documents" bundle="EXPENDITURE_RESOURCES"/></b></p>
				<div class="documents mtop0" style="overflow: hidden; width: 300px">
						<fr:view name="process" layout="processFiles"/>
				</div>
						<p>
							<html:link page="/workflowProcessManagement.do?method=fileUpload" paramId="processId" paramName="process" paramProperty="OID">
								<bean:message key="link.uploadFile" bundle="WORKFLOW_RESOURCES"/>
							</html:link>
						</p>
						<p>
							<html:link page="/workflowProcessManagement.do?method=viewRemovedFiles" paramId="processId" paramName="process" paramProperty="OID">
								<bean:message key="link.viewRemovedFiles" bundle="WORKFLOW_RESOURCES"/>
							</html:link>
						</p>
			</td>
		</logic:equal>
	</tr>
</table>

<bean:define id="unreadComments" name="process" property="unreadCommentsForCurrentUser"/>
<logic:notEmpty name="unreadComments">
	<bean:size id="count" name="unreadComments"/>
		<div class="infoop4 mtop05 mbottom15">
		<p class="mvert025">
			<logic:greaterThan name="count" value="1">
				<bean:message key="label.unreadComments.info.moreThanOne" arg0="<%= count.toString() %>" bundle="WORKFLOW_RESOURCES"/>
			</logic:greaterThan>
			<logic:equal name="count" value="1">
				<bean:message key="label.unreadComments.info" arg0="<%= count.toString() %>" bundle="WORKFLOW_RESOURCES"/>
			</logic:equal>
			
			<html:link page="/workflowProcessManagement.do?method=viewComments" paramId="processId" paramName="process" paramProperty="OID">
				<bean:message key="link.view.unreadComments" bundle="WORKFLOW_RESOURCES"/> »
			</html:link>
		</p>
	</div>
</logic:notEmpty>

<jsp:include page='<%= "/" + includeFolder + "/body.jsp" %>'/>