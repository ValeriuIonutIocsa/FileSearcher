package com.utils.gui.alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class CustomAlertConfirm extends AbstractCustomAlert {

	private final String headerText;
	private final String contentText;
	private final ButtonType[] buttonTypes;

	public CustomAlertConfirm(
			final String headerText,
			final String contentText,
			final ButtonType... buttonTypes) {

		this.headerText = headerText;
		this.contentText = contentText;
		this.buttonTypes = buttonTypes;
	}

	@Override
	Alert.AlertType getAlertType() {
		return Alert.AlertType.CONFIRMATION;
	}

	@Override
	protected String getTitle() {
		return "Choose an option";
	}

	@Override
	protected String getHeaderText() {
		return headerText;
	}

	@Override
	protected String getContentText() {
		return contentText;
	}

	@Override
	protected void configureDialog(
			final Alert dialog) {

		super.configureDialog(dialog);

		dialog.getButtonTypes().setAll(buttonTypes);
	}
}
