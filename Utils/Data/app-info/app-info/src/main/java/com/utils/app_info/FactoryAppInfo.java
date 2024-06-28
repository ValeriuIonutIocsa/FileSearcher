package com.utils.app_info;

import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.commons.lang3.StringUtils;

import com.utils.annotations.ApiMethod;
import com.utils.string.StrUtils;

public final class FactoryAppInfo {

	private static AppInfo instance;

	private FactoryAppInfo() {
	}

	public static AppInfo computeInstance(
			final String appTitleDefault,
			final String appVersionDefault) {

		if (instance == null) {
			instance = newInstance(appTitleDefault, appVersionDefault);
		}
		return instance;
	}

	@ApiMethod
	private static AppInfo newInstance(
			final String appTitleDefault,
			final String appVersionDefault) {

		String appTitle;
		final boolean defaultTitle;
		final Package classPackage = AppInfo.class.getPackage();
		appTitle = classPackage.getImplementationTitle();
		if (StringUtils.isNotBlank(appTitle)) {
			defaultTitle = false;
		} else {
			appTitle = appTitleDefault;
			defaultTitle = true;
		}
		if (StringUtils.isNotBlank(appTitle)) {
			appTitle = formatTitle(appTitle);
		} else {
			appTitle = "";
		}

		String appVersion = classPackage.getImplementationVersion();
		final boolean defaultVersion;
		if (StringUtils.isNotBlank(appVersion)) {
			defaultVersion = false;
		} else {
			appVersion = appVersionDefault;
			defaultVersion = true;
		}

		String buildTime = null;
		boolean defaultBuildTime = false;
		try {
			final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			final Enumeration<URL> resourceEnumeration =
					classLoader.getResources("META-INF/MANIFEST.MF");
			while (resourceEnumeration.hasMoreElements()) {

				try (InputStream inputStream = resourceEnumeration.nextElement().openStream()) {

					final Manifest manifest = new Manifest(inputStream);
					final Attributes mainAttributes = manifest.getMainAttributes();
					final String implementationTitleAttribute =
							mainAttributes.getValue("Implementation-Title");
					if (appTitleDefault.equals(implementationTitleAttribute)) {

						buildTime = mainAttributes.getValue("Build-Time");
						if (StringUtils.isNotBlank(buildTime)) {

							defaultBuildTime = true;
							break;
						}
					}
				}
			}

		} catch (final Exception ignored) {
		}
		if (!defaultBuildTime) {
			buildTime = StrUtils.createDisplayDateTimeString();
		}

		return new AppInfo(
				appTitle, defaultTitle, appVersion, defaultVersion, buildTime, defaultBuildTime);
	}

	static String formatTitle(
			final String appTitle) {

		final StringBuilder stringBuilder = new StringBuilder();
		char lastCh = 0;
		for (int i = 0; i < appTitle.length(); i++) {

			final char ch = appTitle.charAt(i);
			if (i > 0 && Character.isUpperCase(ch) && !Character.isUpperCase(lastCh)) {
				stringBuilder.append(' ');
			}
			if (Character.isLetterOrDigit(ch)) {
				stringBuilder.append(ch);
			}
			lastCh = ch;
		}
		return stringBuilder.toString();
	}
}
