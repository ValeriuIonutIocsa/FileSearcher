package com.personal.scripts.file_search.hist;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.personal.scripts.file_search.FileSearcherUtils;
import com.utils.io.IoUtils;
import com.utils.io.PathUtils;
import com.utils.io.ReaderUtils;
import com.utils.io.WriterUtils;
import com.utils.string.StrUtils;

public final class SavedHistoryFile {

	public static final SavedHistoryFile SEARCH_PATH_SAVED_HISTORY_FILE =
			new SavedHistoryFile("SearchPath");
	public static final SavedHistoryFile FILE_PATH_PATTERN_SAVED_HISTORY_FILE =
			new SavedHistoryFile("FilePathPattern");
	public static final SavedHistoryFile SEARCH_TEXT_SAVED_HISTORY_FILE =
			new SavedHistoryFile("SearchText");

	private static final int MAX_SAVED_HISTORY_ENTRY_COUNT = 100;

	private final String name;

	private final List<String> savedHistoryEntryList;

	private SavedHistoryFile(
			final String name) {

		this.name = name;

		savedHistoryEntryList = new ArrayList<>();
	}

	public void parse() {

		savedHistoryEntryList.clear();
		final String savedHistoryFilePathString = computeSavedHistoryFilePathString();
		if (IoUtils.fileExists(savedHistoryFilePathString)) {

			final List<String> lineList =
					ReaderUtils.tryFileToLineList(savedHistoryFilePathString, StandardCharsets.UTF_8);
			savedHistoryEntryList.addAll(lineList);
		}
	}

	public void save() {

		final String savedHistoryFilePathString = computeSavedHistoryFilePathString();
		WriterUtils.tryLineListToFile(savedHistoryEntryList, StandardCharsets.UTF_8,
				savedHistoryFilePathString);
	}

	private String computeSavedHistoryFilePathString() {

		final String savedHistoryFolderPathString = FileSearcherUtils.createAppFolderPathString();
		return PathUtils.computePath(savedHistoryFolderPathString, name + "SavedHistory.txt");
	}

	public void addEntry(
			final String savedHistoryEntry) {

		savedHistoryEntryList.remove(savedHistoryEntry);
		savedHistoryEntryList.addFirst(savedHistoryEntry);
		if (savedHistoryEntryList.size() > MAX_SAVED_HISTORY_ENTRY_COUNT) {
			savedHistoryEntryList.removeLast();
		}
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	public List<String> getSavedHistoryEntryList() {
		return savedHistoryEntryList;
	}
}
