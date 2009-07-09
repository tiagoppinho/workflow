<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<div class="infoop2" style="-moz-border-radius: 6px; -webkit-border-radius: 6px;">
	<fr:view name="process" schema="view.meta.process.details">
		<fr:layout name="tabular">
			<fr:property name="columnClasses" value="nowrap aright,"/>
		</fr:layout>	
	</fr:view>
</div>