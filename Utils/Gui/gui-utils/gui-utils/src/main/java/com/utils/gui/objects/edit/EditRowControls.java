package com.utils.gui.objects.edit;

import org.apache.commons.lang3.StringUtils;

import com.utils.annotations.ApiMethod;
import com.utils.gui.GuiUtils;
import com.utils.gui.alerts.CustomAlertWarning;
import com.utils.gui.factories.BasicControlsFactories;
import com.utils.string.StrUtils;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class EditRowControls {

	private final String displayName;

	private final TextField textField;

	public EditRowControls(
			final String displayName,
			final boolean numberOnly) {

		this.displayName = displayName;

		if (numberOnly) {
			textField = BasicControlsFactories.getInstance().createNumberOnlyTextField(Double.NaN);
		} else {
			textField = BasicControlsFactories.getInstance().createTextField("");
		}
	}

	@ApiMethod
	public void configurePrefTextFieldWidth(
			final int prefWidth) {

		textField.setPrefWidth(prefWidth);
	}

	@ApiMethod
	public int addToGridPane(
			final GridPane gridPane,
			final int row) {

		final Label label = BasicControlsFactories.getInstance().createLabel(displayName + ":", "bold");
		GuiUtils.addToGridPane(gridPane, label, 0, row, 1, 1,
				Pos.CENTER_LEFT, Priority.NEVER, Priority.NEVER, 7, 7, 7, 7);

		GuiUtils.addToGridPane(gridPane, textField, 1, row, 1, 1,
				Pos.CENTER_LEFT, Priority.NEVER, Priority.ALWAYS, 7, 7, 7, 7);

		return row + 1;
	}

	@ApiMethod
	public void configureRawValue(
			final String value) {

		textField.setText(value);
	}

	@ApiMethod
	public void configureValueCheckNotBlankString(
			final String value) {

		if (StringUtils.isNotBlank(value)) {
			textField.setText(value);
		} else {
			textField.clear();
		}
	}

	@ApiMethod
	public void configureValueCheckNonNegativeInt(
			final int value) {

		if (value >= 0) {
			textField.setText(String.valueOf(value));
		} else {
			textField.clear();
		}
	}

	@ApiMethod
	public String computeRawValue() {

		return textField.getText();
	}

	@ApiMethod
	public String computeValueCheckNotBlankString() {

		final String value;
		final String tmpValue = textField.getText();
		if (StringUtils.isBlank(tmpValue)) {

			new CustomAlertWarning("Blank " + displayName + "!",
					"Please choose a valid value for the " + displayName + "!").showAndWait();
			value = null;

		} else {
			value = tmpValue;
		}
		return value;
	}

	@ApiMethod
	public int computeValueCheckNonNegativeInt() {

		final int value;
		final String valueString = textField.getText();
		if (StringUtils.isBlank(valueString)) {

			new CustomAlertWarning("Blank " + displayName + "!",
					"Please choose a valid value for the " + displayName + "!").showAndWait();
			value = -1;

		} else {
			final int tmpValue = StrUtils.tryParsePositiveInt(valueString);
			if (tmpValue < 0) {
				new CustomAlertWarning("Invalid " + displayName + "!",
						"Please choose a valid value for the " + displayName + "!").showAndWait();
				value = -1;

			} else {
				value = tmpValue;
			}
		}
		return value;
	}
}
