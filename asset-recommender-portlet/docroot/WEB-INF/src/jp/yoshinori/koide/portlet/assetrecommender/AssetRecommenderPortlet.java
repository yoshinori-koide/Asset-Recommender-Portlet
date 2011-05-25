package jp.yoshinori.koide.portlet.assetrecommender;

import javax.portlet.PortletException;

import jp.yoshinori.koide.portlet.assetrecommender.util.DataFileUtil;

import com.liferay.util.bridges.mvc.MVCPortlet;

public class AssetRecommenderPortlet extends MVCPortlet {

	public void init() throws PortletException {

		super.init();
		
		DataFileUtil.init(
				getPortletContext().getRealPath(
						getInitParameter("csv-base"))
						
		);
		
		
	}

	
}
