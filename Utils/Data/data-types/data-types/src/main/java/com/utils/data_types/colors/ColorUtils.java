package com.utils.data_types.colors;

import com.utils.annotations.ApiMethod;

public final class ColorUtils {

	private ColorUtils() {
	}

	@ApiMethod
	public static String webColorNameToCssColorName(
			final String webColorName) {

		String cssColorName = null;
		if (webColorName != null && webColorName.length() >= 8) {
			cssColorName = "#" + webColorName.substring(2);
		}
		return cssColorName;
	}
}
