package com.personal.scripts.file_search.app_info;

import com.utils.app_info.AppInfo;
import com.utils.app_info.FactoryAppInfo;

public final class FileSearcherAppInfoUtils {

	private FileSearcherAppInfoUtils() {
	}

	public static AppInfo createAppInfo() {

		final String appTitleDefault = "File Searcher";
		final String appVersionDefault = "1.0.0";
		return FactoryAppInfo.computeInstance(appTitleDefault, appVersionDefault);
	}
}
