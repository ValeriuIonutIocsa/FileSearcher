package com.utils.html.sections.dyn_pane;

import org.apache.commons.lang3.Strings;

import com.utils.annotations.ApiMethod;
import com.utils.io.ResourceFileUtils;
import com.utils.string.StrUtils;

public final class DynamicContentPaneUtils {

	private DynamicContentPaneUtils() {
	}

	@ApiMethod
	public static String createDynamicContentPaneCss(
			final double sidebarWidth) {

		String dynamicContentPaneCss = ResourceFileUtils.resourceFileToString(
				"com/utils/html/sections/dyn_pane/dynamic_content_pane.css");
		dynamicContentPaneCss = Strings.CS.replace(dynamicContentPaneCss, "@@SIDEBAR_WIDTH@@",
				StrUtils.doubleToString(sidebarWidth, 0, 2, false));
		return dynamicContentPaneCss;
	}
}
