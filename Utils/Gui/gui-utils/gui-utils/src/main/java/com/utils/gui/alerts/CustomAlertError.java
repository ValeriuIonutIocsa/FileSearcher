package com.utils.gui.alerts;

import javafx.scene.control.Alert;

public class CustomAlertError extends AbstractCustomAlert {

	private final String headerText;
	private final String contentText;

	public CustomAlertError(
			final String headerText,
			final String contentText) {

		this.headerText = headerText;
		this.contentText = contentText;
	}

	@Override
	Alert.AlertType getAlertType() {
		return Alert.AlertType.ERROR;
	}

	@Override
	protected String getTitle() {
		return "Error";
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
