package com.utils.gui.factories;

import java.io.File;

import com.utils.annotations.ApiMethod;

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
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public interface BasicControlsFactory {

	Duration TOOLTIP_SHOW_DELAY = new Duration(100);
	Duration TOOLTIP_SHOW_TIME = new Duration(1_000_000);
	Duration TOOLTIP_CLOSE_DELAY = new Duration(100);

	@ApiMethod
	Tooltip createTooltip(
			String text,
			String... styleClassElements);

	@ApiMethod
	Label createLabel(
			String text,
			String... styleClassElements);

	@ApiMethod
	Text createText(
			String text,
			String... styleClassElements);

	@ApiMethod
	TextField createNumberOnlyTextField(
			double value);

	@ApiMethod
	TextField createReadOnlyTextField(
			String text,
			String... styleClassElements);

	@ApiMethod
	TextArea createReadOnlyTextArea(
			String text,
			String... styleClassElements);

	@ApiMethod
	TextField createTextField(
			String text,
			String... styleClassElements);

	@ApiMethod
	TextArea createTextArea(
			String text,
			String... styleClassElements);

	@ApiMethod
	Button createButton(
			String text,
			String... styleClassElements);

	@ApiMethod
	CheckBox createCheckBox(
			String text,
			String... styleClassElements);

	@ApiMethod
	<
			ObjectT> ChoiceBox<ObjectT> createChoiceBox(
					String... styleClassElements);

	@ApiMethod
	<
			ObjectT> ComboBox<ObjectT> createComboBox(
					String... styleClassElements);

	@ApiMethod
	ToggleButton createToggleButton(
			String text,
			String[] styleClassElements,
			ToggleGroup toggleGroup);

	@ApiMethod
	ToggleButton createToggleNode(
			String text,
			String... styleClassElements);

	@ApiMethod
	Separator createSeparator(
			Orientation orientation,
			String... styleClassElements);

	@ApiMethod
	FileChooser createFileChooser(
			String title);

	@ApiMethod
	FileChooser createFileChooser(
			String title,
			File initialDirectoryParam);

	/**
	 * @param extensionArray
	 *            case sensitive file extensions (ex: xml, csv, html)
	 * @return a FileChooser ExtensionFilter that matches files of the given extension
	 */
	@ApiMethod
	FileChooser.ExtensionFilter createExtensionFilter(
			String... extensionArray);

	/**
	 * @return a FileChooser ExtensionFilter that matches all file extensions
	 */
	@ApiMethod
	FileChooser.ExtensionFilter createAllExtensionFilter();

	@ApiMethod
	DirectoryChooser createDirectoryChooser(
			String title);

	@ApiMethod
	DirectoryChooser createDirectoryChooser(
			String title,
			File initialDirectoryParam);
}
