package com.utils.gui.objects.browse;

import java.io.File;
import java.util.List;

import com.utils.gui.factories.BasicControlsFactories;
import com.utils.io.IoUtils;
import com.utils.io.PathUtils;

import javafx.scene.Scene;
import javafx.stage.FileChooser;

class BrowsePathFile implements BrowsePath {

	private final BrowsePathFileMode mode;
	private final List<FileChooser.ExtensionFilter> extensionFilterList;

	BrowsePathFile(
			final BrowsePathFileMode mode,
			final List<FileChooser.ExtensionFilter> extensionFilterList) {

		this.mode = mode;
		this.extensionFilterList = extensionFilterList;
	}

	@Override
	public String browsePath(
			final String name,
			final String existingPathString,
			final String initialDirectoryPathString,
			final Scene scene) {

		String pathString = null;

		final String title = "Browse " + name + " file path:";
		final File initialDirectory = computeInitialDirectory(
				existingPathString, initialDirectoryPathString);
		final FileChooser fileChooser =
				BasicControlsFactories.getInstance().createFileChooser(title, initialDirectory);

		if (extensionFilterList != null) {

			fileChooser.getExtensionFilters().addAll(extensionFilterList);

			final FileChooser.ExtensionFilter lastExtensionFilter = extensionFilterList.getLast();
			fileChooser.setSelectedExtensionFilter(lastExtensionFilter);
		}

		final File file;
		if (mode == BrowsePathFileMode.OPEN) {
			file = fileChooser.showOpenDialog(scene.getWindow());
		} else if (mode == BrowsePathFileMode.SAVE) {
			file = fileChooser.showSaveDialog(scene.getWindow());
		} else {
			file = null;
		}

		if (file != null) {
			pathString = file.getAbsolutePath();
		}

		return pathString;
	}

	@Override
	public File computeInitialDirectory(
			final String existingPathString,
			final String initialDirectoryPathString) {

		File initialDirectory = null;
		final String parentPathString = PathUtils.computeParentPath(existingPathString);
		if (IoUtils.directoryExists(parentPathString)) {
			initialDirectory = new File(parentPathString);
		}
		if (initialDirectory == null) {
			initialDirectory = BrowsePath.computeInitialDirectory(initialDirectoryPathString);
		}
		return initialDirectory;
	}
}
