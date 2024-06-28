package com.utils.gui.factories;

import java.io.File;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.utils.annotations.ApiMethod;
import com.utils.gui.clipboard.ClipboardUtils;
import com.utils.gui.version.VersionDependentMethods;
import com.utils.io.PathUtils;
import com.utils.string.StrUtils;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class BasicControlsFactoryImpl implements BasicControlsFactory {

	@Override
	@ApiMethod
	public Tooltip createTooltip(
			final String text,
			final String... styleClassElements) {

		final Tooltip tooltip = VersionDependentMethods.createTooltip(text);
		if (styleClassElements != null) {
			tooltip.getStyleClass().addAll(styleClassElements);
		}
		return tooltip;
	}

	@Override
	@ApiMethod
	public Label createLabel(
			final String text,
			final String... styleClassElements) {

		final Label label = new Label(text);
		label.setMnemonicParsing(false);
		if (styleClassElements != null) {
			label.getStyleClass().addAll(styleClassElements);
		}
		label.setMinWidth(Region.USE_PREF_SIZE);
		label.setMinHeight(Region.USE_PREF_SIZE);
		return label;
	}

	@Override
	@ApiMethod
	public Text createText(
			final String text,
			final String... styleClassElements) {

		final Text textControl = new Text(text);
		if (styleClassElements != null) {
			textControl.getStyleClass().addAll(styleClassElements);
		}
		return textControl;
	}

	@Override
	@ApiMethod
	public TextField createNumberOnlyTextField(
			final double value) {

		final TextField textField = createTextField(StrUtils.doubleToString(value, 0, 2, false));
		textField.textProperty().addListener((
				observable,
				oldValue,
				newValue) -> {

			final StringBuilder sb = new StringBuilder();
			final String text = textField.getText();
			for (int i = 0; i < text.length(); i++) {

				final char ch = text.charAt(i);
				if (Character.isDigit(ch) || ch == '-' || ch == '.') {
					sb.append(ch);
				}
			}
			textField.setText(sb.toString());
		});
		return textField;
	}

	@Override
	@ApiMethod
	public TextField createReadOnlyTextField(
			final String text,
			final String... styleClassElements) {

		final TextField textField = createTextField(text);
		if (styleClassElements != null) {
			textField.getStyleClass().addAll(styleClassElements);
		}
		textField.getStylesheets().add("com/utils/gui/factories/read_only_text.css");
		textField.getStyleClass().add("text-read-only");
		textField.setEditable(false);
		return textField;
	}

	@Override
	@ApiMethod
	public TextArea createReadOnlyTextArea(
			final String text,
			final String... styleClassElements) {

		final TextArea textArea = createTextArea(text);
		if (styleClassElements != null) {
			textArea.getStyleClass().addAll(styleClassElements);
		}
		textArea.getStylesheets().add("com/utils/gui/factories/read_only_text.css");
		textArea.getStyleClass().add("text-read-only");
		textArea.setEditable(false);
		return textArea;
	}

	@Override
	@ApiMethod
	public TextField createTextField(
			final String text,
			final String... styleClassElements) {

		final TextField textField = new TextField(text);
		if (styleClassElements != null) {
			textField.getStyleClass().addAll(styleClassElements);
		}
		textField.setOnKeyPressed(keyEvent -> {

			if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.C) {

				final String selectedText = textField.getSelectedText();
				if (StringUtils.isNotBlank(selectedText)) {
					ClipboardUtils.putStringInClipBoard(selectedText);
				}
				keyEvent.consume();
			}
		});
		return textField;
	}

	@Override
	@ApiMethod
	public TextArea createTextArea(
			final String text,
			final String... styleClassElements) {

		final TextArea textArea = new TextArea(text);
		if (styleClassElements != null) {
			textArea.getStyleClass().addAll(styleClassElements);
		}
		textArea.setOnKeyPressed(keyEvent -> {

			if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.C) {

				final String selectedText = textArea.getSelectedText();
				if (StringUtils.isNotBlank(selectedText)) {
					ClipboardUtils.putStringInClipBoard(selectedText);
				}
				keyEvent.consume();
			}
		});
		return textArea;
	}

	@Override
	@ApiMethod
	public Button createButton(
			final String text,
			final String... styleClassElements) {

		final Button button = new Button(text);
		button.setMnemonicParsing(false);
		if (styleClassElements != null) {
			button.getStyleClass().addAll(styleClassElements);
		}
		button.setMinWidth(Region.USE_PREF_SIZE);
		button.setMinHeight(Region.USE_PREF_SIZE);
		return button;
	}

	@Override
	@ApiMethod
	public CheckBox createCheckBox(
			final String text,
			final String... styleClassElements) {

		final CheckBox checkBox = new CheckBox(text);
		checkBox.setMnemonicParsing(false);
		if (styleClassElements != null) {
			checkBox.getStyleClass().addAll(styleClassElements);
		}
		checkBox.setMinWidth(Region.USE_PREF_SIZE);
		checkBox.setMinHeight(Region.USE_PREF_SIZE);
		return checkBox;
	}

	@Override
	@ApiMethod
	public <
			ObjectT> ChoiceBox<ObjectT> createChoiceBox(
					final String... styleClassElements) {

		final ChoiceBox<ObjectT> choiceBox = new ChoiceBox<>();

		choiceBox.setMinWidth(0);
		choiceBox.setMaxWidth(Double.POSITIVE_INFINITY);

		if (styleClassElements != null) {
			choiceBox.getStyleClass().addAll(styleClassElements);
		}

		return choiceBox;
	}

	@Override
	@ApiMethod
	public <
			ObjectT> ComboBox<ObjectT> createComboBox(
					final String... styleClassElements) {

		final ComboBox<ObjectT> comboBox = new ComboBox<>();

		comboBox.setMinWidth(0);
		comboBox.setMaxWidth(Double.POSITIVE_INFINITY);

		if (styleClassElements != null) {
			comboBox.getStyleClass().addAll(styleClassElements);
		}

		final ComboBoxKeyEventHandler<ObjectT> keyHandler =
				new ComboBoxKeyEventHandler<>(comboBox);
		comboBox.setOnKeyReleased(keyHandler);
		comboBox.setOnMousePressed(mouseEvent -> keyHandler.clearText());

		return comboBox;
	}

	@Override
	@ApiMethod
	public ToggleButton createToggleButton(
			final String text,
			final String[] styleClassElements,
			final ToggleGroup toggleGroup) {

		final ToggleButton toggleButton = new ToggleButton();
		toggleButton.setMnemonicParsing(false);
		toggleButton.setText(text);
		if (styleClassElements != null) {
			toggleButton.getStyleClass().addAll(styleClassElements);
		}
		if (toggleGroup != null) {
			toggleButton.setToggleGroup(toggleGroup);
		}
		toggleButton.setMaxHeight(20);
		toggleButton.setPadding(new Insets(0));
		return toggleButton;
	}

	@Override
	@ApiMethod
	public ToggleButton createToggleNode(
			final String text,
			final String... styleClassElements) {

		final Label label = createLabel(text, styleClassElements);
		return new ToggleButton("", label);
	}

	@Override
	@ApiMethod
	public Separator createSeparator(
			final Orientation orientation,
			final String... styleClassElements) {

		final Separator separator = new Separator(orientation);
		if (styleClassElements != null) {
			separator.getStyleClass().addAll(styleClassElements);
		}
		return separator;
	}

	@Override
	@ApiMethod
	public FileChooser createFileChooser(
			final String title) {
		return createFileChooser(title, null);
	}

	@Override
	@ApiMethod
	public FileChooser createFileChooser(
			final String title,
			final File initialDirectoryParam) {

		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(title);
		final File initialDirectory = createInitialDirectory(initialDirectoryParam);
		fileChooser.setInitialDirectory(initialDirectory);
		return fileChooser;
	}

	@Override
	@ApiMethod
	public FileChooser.ExtensionFilter createExtensionFilter(
			final String... extensionArray) {

		final String firstDisplayName = Arrays.stream(extensionArray)
				.map(extension -> extension.toUpperCase(Locale.US))
				.collect(Collectors.joining(", "));

		final String secondDisplayName = Arrays.stream(extensionArray)
				.map(extension -> "*." + extension)
				.collect(Collectors.joining(", "));

		final String description = firstDisplayName + " file (" + secondDisplayName + ")";

		final String[] processedExtensionArray = Arrays.stream(extensionArray)
				.map(extension -> "*." + extension)
				.toArray(value -> new String[extensionArray.length]);

		return new FileChooser.ExtensionFilter(description, processedExtensionArray);
	}

	@Override
	@ApiMethod
	public FileChooser.ExtensionFilter createAllExtensionFilter() {
		return new FileChooser.ExtensionFilter("ALL files (*.*)", "*.*");
	}

	@Override
	@ApiMethod
	public DirectoryChooser createDirectoryChooser(
			final String title) {

		final File initialDirectory = new File(PathUtils.createRootPath());
		return createDirectoryChooser(title, initialDirectory);
	}

	@Override
	@ApiMethod
	public DirectoryChooser createDirectoryChooser(
			final String title,
			final File initialDirectoryParam) {

		final DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle(title);
		final File initialDirectory = createInitialDirectory(initialDirectoryParam);
		directoryChooser.setInitialDirectory(initialDirectory);
		return directoryChooser;
	}

	private static File createInitialDirectory(
			final File initialDirectoryParam) {

		final File initialDirectory;
		if (initialDirectoryParam != null && initialDirectoryParam.exists()) {
			initialDirectory = initialDirectoryParam;
		} else {
			initialDirectory = new File(PathUtils.createRootPath());
		}
		return initialDirectory;
	}
}
