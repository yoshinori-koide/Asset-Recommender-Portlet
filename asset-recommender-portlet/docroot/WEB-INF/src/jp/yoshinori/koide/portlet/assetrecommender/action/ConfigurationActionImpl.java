package jp.yoshinori.koide.portlet.assetrecommender.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import jp.yoshinori.koide.portlet.assetrecommender.util.AssetRecommenderUtil;

import com.liferay.portal.kernel.portlet.BaseConfigurationAction;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.service.PortletLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.PortletPreferencesFactoryUtil;

public class ConfigurationActionImpl extends BaseConfigurationAction {
	
	public void processAction(
			PortletConfig portletConfig, ActionRequest actionRequest,
			ActionResponse actionResponse)
		throws Exception {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		if (!cmd.equals(Constants.UPDATE)) {
			return;
		}
		String portletResource = ParamUtil.getString(
				actionRequest, "portletResource");

		PortletPreferences preferences =
			PortletPreferencesFactoryUtil.getPortletSetup(
				actionRequest, portletResource);

		long classNameId = ParamUtil.getLong(actionRequest, "classNameId");
		
		preferences.setValue(AssetRecommenderUtil.PREF_CLASS_NAME_ID, String.valueOf(classNameId));

		preferences.store();

		SessionMessages.add(
			actionRequest, portletConfig.getPortletName() + ".doConfigure");
	}

}
