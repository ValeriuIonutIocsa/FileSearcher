package com.personal.scripts.file_search.hist;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Strings;

import com.personal.scripts.file_search.FileSearcherUtils;
import com.utils.io.IoUtils;
import com.utils.io.PathUtils;
import com.utils.io.ReaderUtils;
import com.utils.io.WriterUtils;
import com.utils.string.StrUtils;
import com.utils.string.regex.RegexUtils;

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
		final String filePathString = createFilePathString();
		if (IoUtils.fileExists(filePathString)) {

			final List<String> lineList =
					ReaderUtils.tryFileToLineList(filePathString, StandardCharsets.UTF_8);
			for (final String line : lineList) {

				final String savedHistoryEntry = Strings.CS.replace(line, "\\R", System.lineSeparator());
				savedHistoryEntryList.add(savedHistoryEntry);
			}
		}
	}

	public void save() {

		final String filePathString = createFilePathString();
		final List<String> lineList = new ArrayList<>();
		for (final String savedHistoryEntry : savedHistoryEntryList) {

			final String line = RegexUtils.NEW_LINE_PATTERN.matcher(savedHistoryEntry).replaceAll("\\\\R");
			lineList.add(line);
		}
		WriterUtils.tryLineListToFile(lineList, StandardCharsets.UTF_8, filePathString);
	}

	private String createFilePathString() {

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
