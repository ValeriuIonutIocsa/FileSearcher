package com.utils.gui.data;

import javafx.stage.Stage;

public class Dimensions {

	private final int minWidth;
	private final int minHeight;
	private final int maxWidth;
	private final int maxHeight;
	private final int prefWidth;
	private final int prefHeight;

	public Dimensions(
			final int minWidth,
			final int minHeight,
			final int maxWidth,
			final int maxHeight,
			final int prefWidth,
			final int prefHeight) {

		this.minWidth = minWidth;
		this.minHeight = minHeight;
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		this.prefWidth = prefWidth;
		this.prefHeight = prefHeight;
	}

	public void set(
			final Stage stage) {

		if (minWidth >= 0) {
			stage.setMinWidth(minWidth);
		}
		if (minHeight >= 0) {
			stage.setMinHeight(minHeight);
		}

		if (maxWidth >= 0) {
			stage.setMaxWidth(maxWidth);
		}
		if (maxHeight >= 0) {
			stage.setMaxHeight(maxHeight);
		}

		if (prefWidth >= 0) {
			stage.setWidth(prefWidth);
		}
		if (prefHeight >= 0) {
			stage.setHeight(prefHeight);
		}
	}
}
