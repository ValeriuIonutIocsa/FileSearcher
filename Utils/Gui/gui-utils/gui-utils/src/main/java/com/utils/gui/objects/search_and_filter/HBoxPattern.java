package com.utils.gui.objects.search_and_filter;

import org.apache.commons.lang3.StringUtils;

import com.utils.gui.AbstractCustomControl;
import com.utils.gui.GuiUtils;
import com.utils.gui.factories.BasicControlsFactories;
import com.utils.gui.factories.LayoutControlsFactories;
import com.utils.string.regex.custom_patterns.patterns.CustomPattern;
import com.utils.string.regex.custom_patterns.patterns.CustomPatternGlobRegex;
import com.utils.string.regex.custom_patterns.patterns.CustomPatternSimple;
import com.utils.string.regex.custom_patterns.patterns.CustomPatternUnixRegex;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

class HBoxPattern extends AbstractCustomControl<HBox> {

	private final TextField patternTextField;
	private final ToggleButton negateToggleButton;
	private final ToggleButton caseSensitiveToggleButton;

	HBoxPattern(
			final boolean focus,
			final EventHandler<ActionEvent> textFieldEventHandler) {

		patternTextField = createPatternTextField(focus, textFieldEventHandler);
		negateToggleButton = createNegateToggleButton();
		caseSensitiveToggleButton = createCaseSensitiveToggleButton();
	}

	private static TextField createPatternTextField(
			final boolean focus,
			final EventHandler<ActionEvent> textFieldEventHandler) {

		final TextField patternTextField = BasicControlsFactories.getInstance().createTextField("");
		patternTextField.setMinWidth(60);
		if (focus) {
			Platform.runLater(patternTextField::requestFocus);
		}
		patternTextField.setOnAction(textFieldEventHandler);
		return patternTextField;
	}

	private static ToggleButton createNegateToggleButton() {

		final ToggleButton negateToggleButton =
				BasicControlsFactories.getInstance().createToggleNode(" ! ", "bold");
		negateToggleButton.setTooltip(BasicControlsFactories.getInstance().createTooltip(
				"negate the pattern (show only what doesn't match)"));
		GuiUtils.setDefaultBorder(negateToggleButton);
		return negateToggleButton;
	}

	private static ToggleButton createCaseSensitiveToggleButton() {

		final ToggleButton caseSensitiveToggleButton =
				BasicControlsFactories.getInstance().createToggleNode("Aa", "bold");
		caseSensitiveToggleButton.setTooltip(
				BasicControlsFactories.getInstance().createTooltip("case sensitive pattern"));
		GuiUtils.setDefaultBorder(caseSensitiveToggleButton);
		return caseSensitiveToggleButton;
	}

	@Override
	protected HBox createRoot() {

		final HBox hBoxRoot = LayoutControlsFactories.getInstance().createHBox();

		GuiUtils.addToHBox(hBoxRoot, patternTextField,
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 0, 0, 0);

		GuiUtils.addToHBox(hBoxRoot, negateToggleButton,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 3, 0, 3);

		GuiUtils.addToHBox(hBoxRoot, caseSensitiveToggleButton,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 0);

		return hBoxRoot;
	}

	CustomPattern createPattern(
			final int patternTypeIndex) {

		CustomPattern customPattern = null;
		final String pattern = patternTextField.getText();
		if (StringUtils.isNotBlank(pattern)) {

			final boolean negate = negateToggleButton.isSelected();
			final boolean caseSensitive = caseSensitiveToggleButton.isSelected();
			if (patternTypeIndex == 0) {
				customPattern = new CustomPatternSimple(pattern, negate, caseSensitive);
			} else if (patternTypeIndex == 1) {
				customPattern = new CustomPatternGlobRegex(pattern, negate, caseSensitive);
			} else if (patternTypeIndex == 2) {
				customPattern = new CustomPatternUnixRegex(pattern, negate, caseSensitive);
			}
		}
		return customPattern;
	}
}
