package com.utils.gui.styles.themes;

import java.util.List;

public final class GuiThemesDefault implements GuiThemes {

	public static final GuiThemesDefault INSTANCE = new GuiThemesDefault();

	private static final GuiTheme STANDARD = new GuiTheme("STANDARD", "Standard", 101,
			"com/utils/gui/styles/style_theme_standard.css",
			"Standard Theme: the standard theme of the application");
	private static final GuiTheme DARK = new GuiTheme("DARK", "Dark", 102,
			"com/utils/gui/styles/style_theme_dark.css",
			"Dark Theme: a dark theme (favours dark grey and black instead of light grey and white)");

	private GuiThemesDefault() {
	}

	@Override
	public void addGuiThemes(
			final List<GuiTheme> guiThemeList) {

		guiThemeList.add(STANDARD);
		guiThemeList.add(DARK);
	}
}
