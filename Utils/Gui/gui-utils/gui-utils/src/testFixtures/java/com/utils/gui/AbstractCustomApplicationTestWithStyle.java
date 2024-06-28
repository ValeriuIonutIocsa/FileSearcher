package com.utils.gui;

import java.util.List;

import com.utils.gui.styles.StyleUtils;
import com.utils.gui.styles.themes.FactoryGuiTheme;
import com.utils.gui.styles.themes.GuiTheme;

public abstract class AbstractCustomApplicationTestWithStyle extends AbstractCustomApplicationTest {

	@Override
	public void fillStylesheetList(
			final List<String> stylesheetList) {

		final GuiTheme defautGuiTheme = FactoryGuiTheme.computeInstance("");
		stylesheetList.add(defautGuiTheme.styleSheetResourcePathString());
		stylesheetList.add(StyleUtils.COMMON_STYLE_SHEET_RESOURCE_PATH_STRING);
	}
}
