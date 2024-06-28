package com.utils.gui.alerts;

import javafx.scene.control.Alert;

public class CustomAlertInfo extends AbstractCustomAlert {

	private final String headerText;
	private final String contentText;

	public CustomAlertInfo(
			final String headerText,
			final String contentText) {

		this.headerText = headerText;
		this.contentText = contentText;
	}

	@Override
	Alert.AlertType getAlertType() {
		return Alert.AlertType.INFORMATION;
	}

	@Override
	protected String getTitle() {
		return "Information";
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
