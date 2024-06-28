package com.utils.gui.objects.browse;

import java.io.File;
import java.util.List;

import com.utils.gui.GuiUtils;
import com.utils.gui.factories.BasicControlsFactories;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;

class BrowsePathFileOrFolder implements BrowsePath {

	private final BrowsePathFileMode mode;
	private final List<FileChooser.ExtensionFilter> extensionFilterList;

	private final ComboBox<InputType> inputTypeComboBox;

	BrowsePathFileOrFolder(
			final InputType initialInputType,
			final BrowsePathFileMode mode,
			final List<FileChooser.ExtensionFilter> extensionFilterList) {

		this.mode = mode;
		this.extensionFilterList = extensionFilterList;

		inputTypeComboBox = BasicControlsFactories.getInstance().createComboBox();
		inputTypeComboBox.getItems().addAll(InputType.values());
		inputTypeComboBox.getSelectionModel().select(initialInputType);
	}

	@Override
	public void createInputTypeControls(
            final HBox hBoxRoot) {

		final Label inputTypeLabel =
				BasicControlsFactories.getInstance().createLabel("input type:", "bold");
		GuiUtils.addToHBox(hBoxRoot, inputTypeLabel,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 0);

		GuiUtils.addToHBox(hBoxRoot, inputTypeComboBox,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 7, 0, 7);
	}

	@Override
	public String browsePath(
			final String name,
			final String existingPathString,
			final String initialDirectoryPathString,
			final Scene scene) {

		String pathString = null;
		final InputType inputType = inputTypeComboBox.getSelectionModel().getSelectedItem();
		if (inputType == InputType.FILE) {
			pathString = new BrowsePathFile(mode, extensionFilterList).browsePath(name, existingPathString,
					initialDirectoryPathString, scene);
		} else if (inputType == InputType.FOLDER) {
			pathString = new BrowsePathFolder().browsePath(name, existingPathString,
					initialDirectoryPathString, scene);
		}
		return pathString;
	}

	@Override
	public File computeInitialDirectory(
			final String existingPathString,
			final String initialDirectoryPathString) {

		File initialDirectory = null;
		final InputType inputType = inputTypeComboBox.getSelectionModel().getSelectedItem();
		if (inputType == InputType.FILE) {

			initialDirectory = new BrowsePathFile(mode, extensionFilterList).computeInitialDirectory(
					existingPathString, initialDirectoryPathString);

		} else if (inputType == InputType.FOLDER) {

			initialDirectory = new BrowsePathFolder().computeInitialDirectory(
					existingPathString, initialDirectoryPathString);
		}
		return initialDirectory;
	}
}
