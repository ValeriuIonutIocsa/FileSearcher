package com.utils.gui.objects.browse;

import java.io.File;
import java.util.Objects;

import com.utils.io.PathUtils;

import javafx.scene.Scene;
import javafx.scene.layout.HBox;

interface BrowsePath {

	/**
	 * @param hBoxRoot
	 *            hBoxRoot
	 */
	default void createInputTypeControls(
			final HBox hBoxRoot) {
	}

	String browsePath(
			String name,
			String existingPathString,
			String initialDirectoryPathString,
			Scene scene);

	static File computeInitialDirectory(
			final String initialDirectoryPathString) {

		final File initialDirectory;
		initialDirectory = new File(
				Objects.requireNonNullElseGet(initialDirectoryPathString, PathUtils::createRootPath));
		return initialDirectory;
	}

	File computeInitialDirectory(
			String existingPathString,
			String initialDirectoryPathString);
}
