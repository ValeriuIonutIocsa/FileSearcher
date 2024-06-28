package com.utils.gui.alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

abstract class AbstractCustomAlert extends AbstractCustomDialog<ButtonType, Alert> {

	AbstractCustomAlert() {
	}

	@Override
	protected Alert createDialog() {

		final Alert.AlertType alertType = getAlertType();
		return new Alert(alertType);
	}

	abstract Alert.AlertType getAlertType();
}
