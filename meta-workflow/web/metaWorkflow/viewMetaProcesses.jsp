
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="label.create.metaProcess" bundle="META_WORKFLOW_RESOURCES"/></h2>

<fr:view name="processes" schema="view.meta.process">
	<fr:layout name="tabular">
			<fr:property name="linkFormat(view)" value="/workflowProcessManagement.do?method=viewProcess&processId=${OID}"/>
				<fr:property name="bundle(view)" value="META_WORKFLOW_RESOURCES"/>
				<fr:property name="key(view)" value="link.view"/>
				<fr:property name="order(view)" value="1"/>
	</fr:layout>
</fr:view>


