package com.utils.app_info;

import com.utils.annotations.ApiMethod;
import com.utils.string.StrUtils;

public class AppInfo {

	private final String appTitle;
	private final boolean defaultTitle;
	private final String appVersion;
	private final boolean defaultVersion;
	private final String buildTime;
	private final boolean defaultBuildTime;

	AppInfo(
			final String appTitle,
			final boolean defaultTitle,
			final String appVersion,
			final boolean defaultVersion,
			final String buildTime,
			final boolean defaultBuildTime) {

		this.appTitle = appTitle;
		this.defaultTitle = defaultTitle;
		this.appVersion = appVersion;
		this.defaultVersion = defaultVersion;
		this.buildTime = buildTime;
		this.defaultBuildTime = defaultBuildTime;
	}

	public String createAppStartMessage() {
		return "starting " + appTitle + " v" + appVersion + " (" + buildTime + ")";
	}

	public String getAppTitleAndVersion() {
		return appTitle + " v" + appVersion;
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	@ApiMethod
	public String getAppTitle() {
		return appTitle;
	}

	@ApiMethod
	public boolean isDefaultTitle() {
		return defaultTitle;
	}

	@ApiMethod
	public String getAppVersion() {
		return appVersion;
	}

	@ApiMethod
	public boolean isDefaultVersion() {
		return defaultVersion;
	}

	@ApiMethod
	public String getBuildTime() {
		return buildTime;
	}

	@ApiMethod
	public boolean isDefaultBuildTime() {
		return defaultBuildTime;
	}
}
