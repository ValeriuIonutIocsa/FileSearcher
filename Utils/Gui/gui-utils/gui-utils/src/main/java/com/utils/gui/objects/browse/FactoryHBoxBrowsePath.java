package com.utils.gui.objects.browse;

import java.util.List;

import com.utils.annotations.ApiMethod;

import javafx.stage.FileChooser;

public final class FactoryHBoxBrowsePath {

	private FactoryHBoxBrowsePath() {
	}

	@ApiMethod
	public static HBoxBrowsePath createHBoxBrowsePathFile(
			final String name,
			final String initialValue,
			final String initialDirectoryPathString,
			final BrowsePathFileMode mode,
			final List<FileChooser.ExtensionFilter> extensionFilterList) {

		final BrowsePath browsePath = new BrowsePathFile(mode, extensionFilterList);
		return new HBoxBrowsePath(name, initialValue, initialDirectoryPathString, browsePath);
	}

	@ApiMethod
	public static HBoxBrowsePath createHBoxBrowsePathFolder(
			final String name,
			final String initialValue,
			final String initialDirectoryPathString) {

		final BrowsePath browsePath = new BrowsePathFolder();
		return new HBoxBrowsePath(name, initialValue, initialDirectoryPathString, browsePath);
	}

	@ApiMethod
	public static HBoxBrowsePath createHBoxBrowsePathFileOrFolder(
			final String name,
			final String initialValue,
			final String initialDirectoryPathString,
			final InputType initialInputType,
			final BrowsePathFileMode mode,
			final List<FileChooser.ExtensionFilter> extensionFilterList) {

		final BrowsePath browsePath = new BrowsePathFileOrFolder(initialInputType, mode, extensionFilterList);
		return new HBoxBrowsePath(name, initialValue, initialDirectoryPathString, browsePath);
	}
}
