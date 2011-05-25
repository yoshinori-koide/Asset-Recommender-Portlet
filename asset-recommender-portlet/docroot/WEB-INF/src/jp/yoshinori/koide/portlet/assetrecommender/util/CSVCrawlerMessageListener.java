package jp.yoshinori.koide.portlet.assetrecommender.util;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageListener;
import com.liferay.portlet.asset.AssetRendererFactoryRegistryUtil;

public class CSVCrawlerMessageListener implements MessageListener {

	@Override
	public void receive(Message message) {
		_log.info("csv crawler called");

		// delete all csv
		long[] availableClassNameIds = AssetRendererFactoryRegistryUtil.getClassNameIds();
		
		for(long classNameId : availableClassNameIds) {
			DataFileUtil.clearDataFile(classNameId);
		}
		
	}

	private static Log _log = LogFactoryUtil.getLog(CSVCrawlerMessageListener.class);

}
