package com.utils.app_info;

import com.utils.annotations.ApiMethod;

public final class JvmVersion {

	private JvmVersion() {
	}

	@ApiMethod
	public static boolean isJvm32Bit() {
		return checkJavaVersion("32");
	}

	@ApiMethod
	public static boolean isJvm64Bit() {
		return checkJavaVersion("64");
	}

	private static boolean checkJavaVersion(
			final String version) {

		final String archModelProperty = System.getProperty("sun.arch.data.model");
		return version.equals(archModelProperty);
	}
}
