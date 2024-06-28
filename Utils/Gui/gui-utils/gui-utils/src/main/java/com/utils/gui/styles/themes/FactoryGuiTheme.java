package com.utils.gui.styles.themes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.utils.reflect.ReflectionUtils;

public final class FactoryGuiTheme {

	public static final List<GuiTheme> GUI_THEME_LIST = createGuiThemeList();

	private static List<GuiTheme> createGuiThemeList() {

		final List<GuiTheme> guiThemeList = new ArrayList<>();

		final List<GuiThemes> guiThemesList = new ArrayList<>();
		ReflectionUtils.fillSingletonList("com.utils.gui.styles.themes",
				GuiThemes.class, guiThemesList);

		for (final GuiThemes guiThemes : guiThemesList) {
			guiThemes.addGuiThemes(guiThemeList);
		}

		guiThemeList.sort(Comparator.comparing(GuiTheme::order));

		return guiThemeList;
	}

	private FactoryGuiTheme() {
	}

	public static GuiTheme computeInstance(
			final String nameParam) {

		GuiTheme guiTheme = null;
		if (!GUI_THEME_LIST.isEmpty()) {

			guiTheme = GUI_THEME_LIST.getFirst();
			for (final GuiTheme value : GUI_THEME_LIST) {

				final String name = value.name();
				if (name.equals(nameParam)) {

					guiTheme = value;
					break;
				}
			}
		}
		return guiTheme;
	}
}
