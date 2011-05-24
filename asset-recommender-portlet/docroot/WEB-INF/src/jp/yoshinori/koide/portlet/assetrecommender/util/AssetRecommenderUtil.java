package jp.yoshinori.koide.portlet.assetrecommender.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.portlet.PortletPreferences;
import javax.servlet.jsp.PageContext;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetEntryServiceUtil;
import com.liferay.portlet.asset.service.persistence.AssetEntryQuery;

public class AssetRecommenderUtil {
	
	public static final String PREF_CLASS_NAME_ID = "class-name-ids";
	
	public static long getClassNameId(
			PortletPreferences preferences, long[] availableClassNameIds) {

			long classNameIds = 0;

			if ((preferences.getValue(PREF_CLASS_NAME_ID, null) != null)) {

				classNameIds = GetterUtil.getLong(
					preferences.getValue(PREF_CLASS_NAME_ID, null));
			}
			else {
				classNameIds = availableClassNameIds[0];
			}

			return classNameIds;
		}
	
	public static List getEntries(AssetEntryQuery assetEntryQuery, long userId, int count) {
		
		try {
			String path = DataFileUtil.getDataFilePath(assetEntryQuery.getClassNameIds()[0]);

			DataModel model = new FileDataModel(new File(path));
			UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
			UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, model);
			Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
			
			List<RecommendedItem> recommendations = recommender.recommend(userId, count);

			if(recommendations.isEmpty()) throw new Exception("Recommend empty...");
			
			Collection<Object> assetIds = new ArrayList<Object>();

			for (RecommendedItem recommendation: recommendations) {
				assetIds.add(recommendation.getItemID());
			}
			
			DynamicQuery dq = DynamicQueryFactoryUtil.forClass(
					AssetEntry.class, PortalClassLoaderUtil.getClassLoader());
			dq.add(
				RestrictionsFactoryUtil.in("classPK", assetIds));
			
			return AssetEntryLocalServiceUtil.dynamicQuery(dq);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			return new ArrayList<AssetEntry>();
		}
		
	}
	
	
}

