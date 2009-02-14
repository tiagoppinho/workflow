<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<bean:define id="processId" name="information" property="process.OID" />
<bean:define id="name" name="information" property="activityName"/>
<bean:define id="activityInformationClass" name="information" property="class.simpleName"/>

<fr:edit id="activityBean" name="information" action='<%="/workflowProcessManagement.do?method=process&activity=" + name + "&processId=" + processId%>'
schema='<%= "activityInformation." + activityInformationClass %>'
>
	<fr:layout name="tabular">
		
	</fr:layout>
	<fr:destination name="cancel" path='<%="/workflowProcessManagement.do?method=viewProcess&processId=" + processId%>'/>
</fr:edit>

