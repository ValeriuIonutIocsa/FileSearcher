package com.utils.gui.alerts;

import javafx.scene.control.Alert;

public class CustomAlertWarning extends AbstractCustomAlert {

	private final String headerText;
	private final String contentText;

	public CustomAlertWarning(
			final String headerText,
			final String contentText) {

		this.headerText = headerText;
		this.contentText = contentText;
	}

	@Override
	Alert.AlertType getAlertType() {
		return Alert.AlertType.WARNING;
	}

	@Override
	protected String getTitle() {
		return "Warning";
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
