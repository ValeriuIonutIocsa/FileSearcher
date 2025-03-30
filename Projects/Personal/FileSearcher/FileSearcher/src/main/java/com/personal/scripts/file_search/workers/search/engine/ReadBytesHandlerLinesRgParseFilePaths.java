package com.personal.scripts.file_search.workers.search.engine;

import java.util.List;

import com.utils.io.processes.AbstractReadBytesHandlerLines;

class ReadBytesHandlerLinesRgParseFilePaths extends AbstractReadBytesHandlerLines {

	private final String searchFolderPathString;
	private final List<String> filePathStringList;

	ReadBytesHandlerLinesRgParseFilePaths(
			final String searchFolderPathString,
			final List<String> filePathStringList) {

		this.searchFolderPathString = searchFolderPathString;
		this.filePathStringList = filePathStringList;
	}

	@Override
	protected void handleLine(
			final String line) {

		if (line.startsWith(searchFolderPathString)) {
			filePathStringList.add(line);
		}
	}
}
