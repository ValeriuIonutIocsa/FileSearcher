package com.personal.scripts.file_search;

import com.personal.scripts.file_search.app_info.FileSearcherAppInfoUtils;
import com.utils.app_info.AppInfo;
import com.utils.log.Logger;

import javafx.application.Application;

final class AppStartFileSearcher {

	private AppStartFileSearcher() {
	}

	public static void main(
			final String[] args) {

		final AppInfo appInfo = FileSearcherAppInfoUtils.createAppInfo();
		final String appTitleAndVersion = appInfo.getAppTitleAndVersion();
		Logger.printProgress("starting " + appTitleAndVersion);

		Application.launch(ApplicationFileSearcher.class, args);
	}
}
