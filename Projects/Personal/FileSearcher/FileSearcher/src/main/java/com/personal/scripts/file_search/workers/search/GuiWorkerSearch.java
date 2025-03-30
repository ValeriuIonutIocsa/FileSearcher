package com.personal.scripts.file_search.workers.search;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.personal.scripts.file_search.ControlDisablerFileSearcher;
import com.personal.scripts.file_search.FileSearcherUtils;
import com.personal.scripts.file_search.VBoxFileSearcher;
import com.personal.scripts.file_search.hist.SavedHistoryFile;
import com.personal.scripts.file_search.hist.SavedOptionsFile;
import com.personal.scripts.file_search.text_find.TextFinder;
import com.personal.scripts.file_search.text_find.TextFinderRegex;
import com.personal.scripts.file_search.text_find.TextFinderRegular;
import com.personal.scripts.file_search.workers.search.engine.SearchEngine;
import com.personal.scripts.file_search.workers.search.engine.SearchEngineOwn;
import com.personal.scripts.file_search.workers.search.engine.SearchEngineRg;
import com.personal.scripts.file_search.workers.search.engine.type.SearchEngineType;
import com.utils.gui.alerts.CustomAlertError;
import com.utils.gui.workers.AbstractGuiWorker;
import com.utils.io.FileSizeUtils;
import com.utils.io.IoUtils;
import com.utils.io.PathUtils;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.log.Logger;
import com.utils.string.regex.RegexUtils;

import javafx.scene.Scene;

public class GuiWorkerSearch extends AbstractGuiWorker {

	private final SearchData searchData;
	private final RunningProcesses runningProcesses;
	private final boolean saveHistory;

	private final VBoxFileSearcher vBoxFileSearcher;

	private List<SearchResult> searchResultList;
	private int fileCount;
	private int fileContainingTextCount;
	private SearchEngine searchEngine;
	private TextFinder textFinder;

	public GuiWorkerSearch(
			final Scene scene,
			final SearchData searchData,
			final boolean saveHistory,
			final RunningProcesses runningProcesses,
			final VBoxFileSearcher vBoxFileSearcher) {

		super(scene, new ControlDisablerFileSearcher(scene, vBoxFileSearcher));

		this.searchData = searchData;
		this.runningProcesses = runningProcesses;
		this.saveHistory = saveHistory;

		this.vBoxFileSearcher = vBoxFileSearcher;
	}

	@Override
	protected void work() {

		Logger.printNewLine();
		Logger.printProgress("starting search");

		final SearchEngineType searchEngineType = searchData.searchEngineType();
		SavedOptionsFile.INSTANCE.setSearchEngineType(searchEngineType);

		final boolean caseSensitivePathPattern = searchData.caseSensitivePathPattern();
		SavedOptionsFile.INSTANCE.setCaseSensitivePathPattern(caseSensitivePathPattern);

		final boolean useRegex = searchData.useRegex();
		SavedOptionsFile.INSTANCE.setUseRegex(useRegex);

		final boolean caseSensitive = searchData.caseSensitive();
		SavedOptionsFile.INSTANCE.setCaseSensitive(caseSensitive);

		SavedOptionsFile.INSTANCE.save();

		if (saveHistory) {

			final String appFolderPathString = FileSearcherUtils.createAppFolderPathString();
			final boolean createDirectoriesSuccess = FactoryFolderCreator.getInstance()
					.createDirectories(appFolderPathString, false, true);
			if (createDirectoriesSuccess) {

				final String searchFolderPathString = searchData.searchFolderPathString();
				saveHistory(SavedHistoryFile.SEARCH_PATH_SAVED_HISTORY_FILE, searchFolderPathString);

				final String filePathPatternString = searchData.filePathPatternString();
				saveHistory(SavedHistoryFile.FILE_PATH_PATTERN_SAVED_HISTORY_FILE, filePathPatternString);

				final String searchText = searchData.searchText();
				saveHistory(SavedHistoryFile.SEARCH_TEXT_SAVED_HISTORY_FILE, searchText);
			}
		}

		workL2();
	}

	private static void saveHistory(
			final SavedHistoryFile savedHistoryFile,
			final String savedHistoryEntry) {

		savedHistoryFile.addEntry(savedHistoryEntry);
		savedHistoryFile.save();
	}

	void workL2() {

		final String searchFolderPathString = searchData.searchFolderPathString();
		if (StringUtils.isBlank(searchFolderPathString)) {
			new CustomAlertError("search path is blank",
					"search path is blank").showAndWait();

		} else {
			if (!IoUtils.directoryExists(searchFolderPathString)) {
				new CustomAlertError("search folder does not exist",
						"search folder does not exist").showAndWait();

			} else {
				final String filePathPatternString = searchData.filePathPatternString();
				if (StringUtils.isBlank(filePathPatternString)) {
					new CustomAlertError("file path pattern is blank",
							"file path pattern is blank").showAndWait();

				} else {
					workL3();
				}
			}
		}
	}

	private void workL3() {

		final List<String> dirPathStringList = new ArrayList<>();
		final List<String> filePathStringList = new ArrayList<>();

		final SearchEngineType searchEngineType = searchData.searchEngineType();
		if (searchEngineType == SearchEngineType.RG) {
			searchEngine = new SearchEngineRg(searchData);
		} else {
			searchEngine = new SearchEngineOwn(searchData);
		}
		searchEngine.parseFilePaths(dirPathStringList, filePathStringList, runningProcesses);

		final Map<String, Integer> filePathStringToOccurrenceCountMap = new HashMap<>();
		textFinder = createTextFinder();
		if (textFinder != null) {

			searchEngine.searchText(filePathStringList, textFinder,
					filePathStringToOccurrenceCountMap, runningProcesses);
		}

		searchResultList = new ArrayList<>();
		for (final String dirPathString : dirPathStringList) {
			addSearchResult(dirPathString, true, filePathStringToOccurrenceCountMap);
		}
		for (final String filePathString : filePathStringList) {
			addSearchResult(filePathString, false, filePathStringToOccurrenceCountMap);
		}

		searchResultList.sort(
				Comparator.comparing(SearchResult::checkHasOccurrences, Comparator.reverseOrder())
						.thenComparing(SearchResult::getFolderPathString)
						.thenComparing(SearchResult::getFileName));

		fileCount = searchResultList.size();
		fileContainingTextCount = computeFileContainingTextCount(searchResultList);
	}

	private static int computeFileContainingTextCount(
			final List<SearchResult> searchResultList) {

		int fileContainingTextCount = 0;
		for (final SearchResult searchResult : searchResultList) {

			final boolean hasOccurrences = searchResult.checkHasOccurrences();
			if (hasOccurrences) {
				fileContainingTextCount++;
			}
		}
		return fileContainingTextCount;
	}

	private TextFinder createTextFinder() {

		TextFinder textFinder = null;
		final String searchText = searchData.searchText();
		if (StringUtils.isNotBlank(searchText)) {

			final boolean useRegex = searchData.useRegex();
			if (useRegex) {

				final boolean caseSensitive = searchData.caseSensitive();
				final Pattern searchPattern = RegexUtils.tryCompile(searchText, caseSensitive);
				if (searchPattern == null) {
					new CustomAlertError("invalid search text pattern",
							"the pattern for searching text is not a valid regex pattern").showAndWait();

				} else {
					textFinder = new TextFinderRegex(searchPattern);
				}

			} else {
				final String searchString;
				final boolean caseSensitive = searchData.caseSensitive();
				if (caseSensitive) {
					searchString = searchText;
				} else {
					searchString = searchText.toLowerCase(Locale.US);
				}
				textFinder = new TextFinderRegular(searchString, caseSensitive);
			}
		}
		return textFinder;
	}

	private void addSearchResult(
			final String filePathString,
			final boolean dir,
			final Map<String, Integer> filePathStringToOccurrenceCountMap) {

		final String fileName = PathUtils.computeFileName(filePathString);
		final String folderPathString = PathUtils.computeParentPath(filePathString);
		final String extension = PathUtils.computeExtension(fileName);

		final long lastModifiedTime = IoUtils.computeFileLastModifiedTime(filePathString);
		final Instant lastModifiedInstant = Instant.ofEpochMilli(lastModifiedTime);

		String fileSizeString = null;
		if (!dir) {
			fileSizeString = FileSizeUtils.readableFileSize(filePathString);
		}

		int occurrenceCount = -1;
		if (!dir) {

			final Integer occurrenceCountInteger =
					filePathStringToOccurrenceCountMap.get(filePathString);
			if (occurrenceCountInteger != null) {
				occurrenceCount = occurrenceCountInteger;
			}
		}

		final SearchResult searchResult = new SearchResult(fileName, folderPathString, extension,
				lastModifiedInstant, fileSizeString, occurrenceCount);
		searchResultList.add(searchResult);
	}

	@Override
	protected void error() {

		new CustomAlertError("error", "error occurred while searching").showAndWait();
	}

	@Override
	protected void finish() {

		vBoxFileSearcher.updateSearchResults(searchResultList,
				fileCount, fileContainingTextCount, searchEngine, textFinder);
	}

	List<SearchResult> getSearchResultList() {
		return searchResultList;
	}
}
