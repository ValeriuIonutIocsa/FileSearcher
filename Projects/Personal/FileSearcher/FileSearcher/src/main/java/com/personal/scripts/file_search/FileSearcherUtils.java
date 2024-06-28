package com.personal.scripts.file_search;

import com.utils.io.PathUtils;

public final class FileSearcherUtils {

	private FileSearcherUtils() {
	}

	public static String createAppFolderPathString() {

		return PathUtils.computePath(System.getProperty("user.home"), "FileSearcher");
	}
}
