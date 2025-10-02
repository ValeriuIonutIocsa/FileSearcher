package com.utils.gui.data;

import javafx.stage.Stage;

public class Dimensions {

	private final double minWidth;
	private final double minHeight;
	private final double maxWidth;
	private final double maxHeight;
	private final double prefWidth;
	private final double prefHeight;

	public Dimensions(
			final double minWidth,
			final double minHeight,
			final double maxWidth,
			final double maxHeight,
			final double prefWidth,
			final double prefHeight) {

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
