package com.utils.gui.objects.browse;

import java.io.File;

import com.utils.gui.factories.BasicControlsFactories;
import com.utils.io.IoUtils;

import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;

class BrowsePathFolder implements BrowsePath {

	BrowsePathFolder() {
	}

	@Override
	public String browsePath(
			final String name,
			final String existingPathString,
			final String initialDirectoryPathString,
			final Scene scene) {

		String pathString = null;

		final String title = "Browse " + name + " folder path:";
		final File initialDirectory = computeInitialDirectory(
				existingPathString, initialDirectoryPathString);
		final DirectoryChooser directoryChooser =
				BasicControlsFactories.getInstance().createDirectoryChooser(title, initialDirectory);

		final File file = directoryChooser.showDialog(scene.getWindow());
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
		if (IoUtils.directoryExists(existingPathString)) {
			initialDirectory = new File(existingPathString);
		}
		if (initialDirectory == null) {
			initialDirectory = BrowsePath.computeInitialDirectory(initialDirectoryPathString);
		}
		return initialDirectory;
	}
}
