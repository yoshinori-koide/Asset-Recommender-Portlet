<%@ page contentType="text/html; charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %>
<%@ taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %>
<%@ taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %>

<%@ page import="java.util.Set" %>

<%@ page import="javax.portlet.PortletPreferences" %>

<%@ page import="com.liferay.portal.kernel.language.LanguageUtil" %>

<%@ page import="com.liferay.portal.kernel.util.Constants" %>
<%@ page import="com.liferay.portal.kernel.util.ParamUtil" %>
<%@ page import="com.liferay.portal.kernel.util.GetterUtil" %>
<%@ page import="com.liferay.portal.kernel.util.SetUtil" %>

<%@ page import="com.liferay.portal.model.*" %>
<%@ page import="com.liferay.portal.service.*" %>

<%@ page import="com.liferay.portlet.asset.AssetRendererFactoryRegistryUtil" %>
<%@ page import="com.liferay.portlet.PortletPreferencesFactoryUtil" %>

<%@ page import="jp.yoshinori.koide.portlet.assetrecommender.util.AssetRecommenderUtil" %>

<portlet:defineObjects />

<liferay-theme:defineObjects />

<%
String portletResource = ParamUtil.getString(
		renderRequest, "portletResource");

PortletPreferences preferences =
	PortletPreferencesFactoryUtil.getPortletSetup(
			renderRequest, portletResource);

long[] availableClassNameIds = AssetRendererFactoryRegistryUtil.getClassNameIds();

Set<Long> availableClassNameIdsSet = SetUtil.fromArray(availableClassNameIds);
long recommendClassNameId = GetterUtil.getLong(preferences.getValue(AssetRecommenderUtil.PREF_CLASS_NAME_ID, null));

%>
<liferay-portlet:actionURL portletConfiguration="true" var="configurationActionURL" />

<aui:form action="<%= configurationActionURL %>" method="post" name="fm">
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />

	<aui:select label="asset-type" name="classNameId">
<%
		for (long classNameId : availableClassNameIdsSet) {
			ClassName className = ClassNameServiceUtil.getClassName(classNameId);
%>
			<aui:option label='<%= LanguageUtil.get(pageContext, "model.resource." + className.getValue()) %>' selected="<%= (classNameId==recommendClassNameId) %>" value="<%= classNameId %>" />
<%
		}
%>
	</aui:select>

	<aui:button-row>
		<aui:button type="submit" />
	</aui:button-row>
	
</aui:form>