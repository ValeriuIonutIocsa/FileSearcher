package com.utils.gui.alerts;

import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;

public class CustomTextInputDialog extends AbstractCustomDialog<String, TextInputDialog> {

	private final String headerText;
	private final String contentText;
	private final String initialValue;

	public CustomTextInputDialog(
			final Scene scene,
			final String headerText,
			final String contentText,
			final String initialValue) {

		super(scene);

		this.headerText = headerText;
		this.contentText = contentText;
		this.initialValue = initialValue;
	}

	@Override
	protected TextInputDialog createDialog() {
		return new TextInputDialog(initialValue);
	}

	@Override
	protected String getTitle() {
		return "Input";
	}

	@Override
	protected String getHeaderText() {
		return headerText;
	}

	@Override
	protected String getContentText() {
		return contentText;
	}
}
