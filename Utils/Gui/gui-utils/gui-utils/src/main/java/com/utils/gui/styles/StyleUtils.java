package com.utils.gui.styles;

import com.utils.annotations.ApiMethod;
import com.utils.gui.GuiUtils;
import com.utils.gui.styles.themes.FactoryGuiTheme;
import com.utils.gui.styles.themes.GuiTheme;

import javafx.scene.Scene;

public final class StyleUtils {

	public static final String COMMON_STYLE_SHEET_RESOURCE_PATH_STRING =
			"com/utils/gui/styles/style.css";

	private StyleUtils() {
	}

	@ApiMethod
	public static void configureStyle(
			final Scene scene,
			final String specificStyleSheetResourceFilePath,
			final String guiThemeName) {

		final GuiTheme guiTheme = FactoryGuiTheme.computeInstance(guiThemeName);
		if (guiTheme != null) {

			final String themeStyleSheetResourcePathString = guiTheme.styleSheetResourcePathString();
			GuiUtils.setStyleCss(scene, COMMON_STYLE_SHEET_RESOURCE_PATH_STRING,
					themeStyleSheetResourcePathString, specificStyleSheetResourceFilePath);
		}
	}
}
