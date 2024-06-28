package com.utils.gui.styles.themes;

public record GuiTheme(
		String name,
		String displayName,
		int order,
		String styleSheetResourcePathString,
		String description) {

	@Override
	public String toString() {
		return displayName;
	}
}
