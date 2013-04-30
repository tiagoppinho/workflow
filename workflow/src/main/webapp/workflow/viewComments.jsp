<%@page import="pt.ist.bennu.core.domain.User"%>
<%@page import="module.workflow.presentationTier.actions.CommentBean"%>
<%@page import="java.util.TreeSet"%>
<%@page import="module.workflow.domain.WorkflowProcessComment"%>
<%@page import="java.util.Set"%>
<%@page import="pt.ist.fenixframework.FenixFramework"%>
<%@page import="module.workflow.domain.WorkflowProcess"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<%@page import="module.workflow.presentationTier.WorkflowLayoutContext"%>
<%@page import="pt.ist.bennu.core.presentationTier.actions.ContextBaseAction"%>

<bean:define id="currentUser" name="USER_SESSION_ATTRIBUTE" property="user"/>

<h2><bean:message key="title.comments" bundle="WORKFLOW_RESOURCES"/></h2>

<%
	final WorkflowLayoutContext layoutContext = (WorkflowLayoutContext) ContextBaseAction.getContext(request);

//asert if we are to get the comments here or not, usually depending if we are showing the comments in-line or not
String fetch = request.getParameter("displayedInline");
if ( fetch != null && fetch.equalsIgnoreCase("true"))
{
    request.setAttribute("displayedInline", fetch);
    //get the process 
	String oid = request.getParameter("processId");
	final WorkflowProcess process = FenixFramework.getDomainObject(oid);
	request.setAttribute("process", process);

	Set<WorkflowProcessComment> comments = new TreeSet<WorkflowProcessComment>(WorkflowProcessComment.COMPARATOR);
	comments.addAll(process.getComments());
	
	process.markCommentsAsReadForUser((User) currentUser);
	request.setAttribute("comments", comments);
	request.setAttribute("bean", new CommentBean(process));
    
}
else {
    fetch = "false";
    request.setAttribute("displayedInline", fetch);
    
}

%>

<logic:notEqual  name="displayedInline" value="true">
	<p>
		<html:link page="/workflowProcessManagement.do?method=viewProcess" paramId="processId" paramName="process" paramProperty="externalId">
			« <bean:message key="link.backToProcess" bundle="WORKFLOW_RESOURCES"/>
		</html:link>
	</p>


<jsp:include page='<%= layoutContext.getWorkflowShortBody() %>'/>

</logic:notEqual>

<logic:empty name="comments">
	<p class="mtop15"><em><bean:message key="label.noComments" bundle="WORKFLOW_RESOURCES"/>.</em></p>
</logic:empty>


<logic:notEmpty name="comments">
	<fr:view name="process" >
		<fr:layout name="viewComments">
			<fr:property name="commentBlockClasses" value="comment"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<bean:define id="processOid" name="process" property="externalId" type="java.lang.String"/>

<fr:form action='<%= "/workflowProcessManagement.do?method=addComment&processId=" + processOid + "&displayedInLine=" + fetch%>'>
	 
	<fr:edit id="comment" name="bean" visible="false"/>

	<table class="form list-reset">
		<tr>
			<td>
				<bean:message key="label.addComment" bundle="WORKFLOW_RESOURCES"/>:
			</td>
			<td>
				<fr:edit id="comment-text" name="bean" slot="comment" type="java.lang.String" required="true">
					<fr:layout name="longText">
						<fr:property name="rows" value="6"/>
						<fr:property name="columns" value="60"/>
						<fr:property name="classes" value="form"/>
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.notifyPeopleByEmail" bundle="WORKFLOW_RESOURCES"/>:
			</td>
			<td>
				<fr:edit id="peopleToNotify" name="bean" slot="peopleToNotify">
					<fr:layout name="option-select">
						<fr:property name="providerClass" value="module.workflow.presentationTier.renderers.providers.CommentersForProcess"/>
						<fr:property name="eachLayout" value="viewCommenters"/>
						<fr:property name="saveOptions" value="true"/>
						<fr:property name="selectAllShown" value="true"/>
						<fr:property name="classes" value="nobullet"/>
					</fr:layout>
				</fr:edit>
			</td>
		</tr>
			<logic:equal name="bean" property="process.queuesAssociated" value="true">
				<tr>
					<td><bean:message key="label.notifyQueuesByEmail" bundle="WORKFLOW_RESOURCES" />:</td>
					<td>
						<fr:edit id="queuesToNotify" name="bean" slot="queuesToNotify">
							<fr:layout name="option-select">
								<fr:property name="providerClass" value="module.workflow.presentationTier.renderers.providers.QueuesForProcess"/>
								<fr:property name="eachLayout" value="viewQueues"/>
								<fr:property name="saveOptions" value="true"/>
								<fr:property name="selectAllShown" value="true"/>
								<fr:property name="classes" value="nobullet"/>
							</fr:layout>
						</fr:edit>
					</td>
				</tr>
			</logic:equal>
	</table>
	<html:submit styleClass="inputbutton"><bean:message key="button.send" bundle="EXPENDITURE_RESOURCES"/> </html:submit>
</fr:form>


<script type="text/javascript">
jQuery.each($(".unableToNotify"), function() {
	$(this).parent("span").parent("span").parent("label").siblings("input").attr('disabled','true');
});
</script>
