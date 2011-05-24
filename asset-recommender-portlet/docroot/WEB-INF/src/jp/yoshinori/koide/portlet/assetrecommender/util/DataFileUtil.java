package jp.yoshinori.koide.portlet.assetrecommender.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portlet.ratings.model.RatingsEntry;
import com.liferay.portlet.ratings.service.RatingsEntryLocalServiceUtil;

public class DataFileUtil {
	
	public static void init(String base) {
		_csvBase = base;
	}

	public static String getDataFilePath(long classNameId) throws SystemException, IOException {
		
		String path = _dataFiles.get(new Long(classNameId));
		
		if(path==null||StringPool.BLANK.equals(path)||
				(!FileUtil.exists(path))) {
			path = getCSVFileName(classNameId);
			StringBundler sb = new StringBundler();
			// Create a csv file
			DynamicQuery dq = DynamicQueryFactoryUtil.forClass(
					RatingsEntry.class, PortalClassLoaderUtil.getClassLoader());
			// !!! CAUTION :: THIS IS NOT FOR MULTI-TENANT !!!!
			dq.add(
				RestrictionsFactoryUtil.eq("classNameId", classNameId));
			Iterator entries = RatingsEntryLocalServiceUtil.dynamicQuery(dq).iterator();
			while(entries.hasNext()) {
				RatingsEntry rating = (RatingsEntry)entries.next();
				sb.append(rating.getUserId())
					.append(StringPool.COMMA)
					.append(rating.getClassPK())
					.append(StringPool.COMMA)
					.append(rating.getScore())
					.append(StringPool.RETURN_NEW_LINE);
			}
			
			FileUtil.write(path, sb.toString());
			
			_dataFiles.put(new Long(classNameId), path);
			
		}
		
		return path;
	}

	public static void clearDataFile(long classNameId) {
		
		FileUtil.delete(getCSVFileName(classNameId));

		_dataFiles.remove(new Long(classNameId));
	}
	
	public static String getCSVFileName(long classNameId) {
		return _csvBase + "/" + classNameId + ".csv";
	}
	
	private static String _csvBase = StringPool.BLANK;
	private static Map<Long, String> _dataFiles = new HashMap<Long,String>();
}
