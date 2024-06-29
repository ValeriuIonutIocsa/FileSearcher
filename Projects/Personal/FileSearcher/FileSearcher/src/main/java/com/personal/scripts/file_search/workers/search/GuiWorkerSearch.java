package com.personal.scripts.file_search.workers.search;

import java.io.BufferedReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.personal.scripts.file_search.FileSearcherUtils;
import com.personal.scripts.file_search.VBoxFileSearcher;
import com.personal.scripts.file_search.hist.SavedHistoryFile;
import com.personal.scripts.file_search.text_find.TextFinder;
import com.personal.scripts.file_search.text_find.TextFinderRegex;
import com.personal.scripts.file_search.text_find.TextFinderRegular;
import com.utils.gui.alerts.CustomAlertError;
import com.utils.gui.workers.AbstractGuiWorker;
import com.utils.gui.workers.ControlDisablerAll;
import com.utils.io.FileSizeUtils;
import com.utils.io.IoUtils;
import com.utils.io.ListFileUtils;
import com.utils.io.PathUtils;
import com.utils.io.ReaderUtils;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.log.Logger;
import com.utils.string.regex.RegexUtils;

import javafx.scene.Scene;

public class GuiWorkerSearch extends AbstractGuiWorker {

	private final String searchFolderPathString;
	private final String filePathPatternString;
	private final String searchText;
	private final boolean useRegex;
	private final boolean caseSensitive;

	private final boolean saveHistory;
	private final VBoxFileSearcher vBoxFileSearcher;

	private final List<SearchResult> searchResultList;
	private int fileCount;
	private int fileContainingTextCount;
	private TextFinder textFinder;

	public GuiWorkerSearch(
			final Scene scene,
			final String searchFolderPathString,
			final String filePathPatternString,
			final String searchText,
			final boolean useRegex,
			final boolean caseSensitive,
			final boolean saveHistory,
			final VBoxFileSearcher vBoxFileSearcher) {

		super(scene, new ControlDisablerAll(scene));

		this.searchFolderPathString = searchFolderPathString;
		this.filePathPatternString = filePathPatternString;
		this.searchText = searchText;
		this.useRegex = useRegex;
		this.caseSensitive = caseSensitive;
		this.saveHistory = saveHistory;

		this.vBoxFileSearcher = vBoxFileSearcher;

		searchResultList = new ArrayList<>();
	}

	@Override
	protected void work() {

		if (saveHistory) {

			final String appFolderPathString = FileSearcherUtils.createAppFolderPathString();
			final boolean createDirectoriesSuccess = FactoryFolderCreator.getInstance()
					.createDirectories(appFolderPathString, false, true);
			if (createDirectoriesSuccess) {

				saveHistory(SavedHistoryFile.SEARCH_PATH_SAVED_HISTORY_FILE, searchFolderPathString);
				saveHistory(SavedHistoryFile.FILE_PATH_PATTERN_SAVED_HISTORY_FILE, filePathPatternString);
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

		if (StringUtils.isBlank(searchFolderPathString)) {
			new CustomAlertError("search path is blank",
					"search path is blank").showAndWait();

		} else {
			if (!IoUtils.directoryExists(searchFolderPathString)) {
				new CustomAlertError("search folder does not exist",
						"search folder does not exist").showAndWait();

			} else {
				if (StringUtils.isBlank(filePathPatternString)) {
					new CustomAlertError("file path pattern is blank",
							"file path pattern is blank").showAndWait();

				} else {
					final String filePathPatternString = this.filePathPatternString
							.replace("*.", ".*\\.").replace("*", ".*");
					final Pattern filePathPattern =
							RegexUtils.tryCompile(filePathPatternString, false);
					if (filePathPattern == null) {
						new CustomAlertError("invalid file path pattern",
								"the file path pattern is not a valid pattern").showAndWait();

					} else {
						workL3(searchFolderPathString, filePathPattern);
					}
				}
			}
		}
	}

	private void workL3(
			final String searchFolderPathString,
			final Pattern filePathPattern) {

		final List<String> dirPathStringList = new ArrayList<>();
		final List<String> filePathStringList = new ArrayList<>();
		ListFileUtils.visitFilesRecursively(searchFolderPathString,
				dirPath -> {
					final String dirPathString = dirPath.toString();
					if (filePathPattern.matcher(dirPathString).matches()) {
						dirPathStringList.add(dirPathString);
					}
				},
				filePath -> {
					final String filePathString = filePath.toString();
					if (filePathPattern.matcher(filePathString).matches()) {
						filePathStringList.add(filePathString);
					}
				});

		for (final String dirPathString : dirPathStringList) {
			addSearchResult(dirPathString, true, null);
		}

		textFinder = createTextFinder();
		for (final String filePathString : filePathStringList) {
			addSearchResult(filePathString, false, textFinder);
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
		if (StringUtils.isNotBlank(searchText)) {

			if (useRegex) {

				final Pattern searchPattern = RegexUtils.tryCompile(searchText, caseSensitive);
				if (searchPattern == null) {
					new CustomAlertError("invalid search text pattern",
							"the pattern for searching text is not a valid regex pattern").showAndWait();

				} else {
					textFinder = new TextFinderRegex(searchPattern);
				}

			} else {
				final String searchString;
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
			final TextFinder textFinder) {

		final String fileName = PathUtils.computeFileName(filePathString);
		final String folderPathString = PathUtils.computeParentPath(filePathString);
		final String extension = PathUtils.computeExtension(fileName);
		final long lastModifiedTime = IoUtils.computeFileLastModifiedTime(filePathString);
		final Instant lastModifiedInstant = Instant.ofEpochMilli(lastModifiedTime);

		String fileSizeString = null;
		if (!dir) {
			fileSizeString = FileSizeUtils.readableFileSize(filePathString);
		}

		int count = -1;
		if (!dir && textFinder != null) {
			count = computeOccurrenceCount(filePathString, textFinder);
		}

		final SearchResult searchResult = new SearchResult(fileName, folderPathString, extension,
				lastModifiedInstant, fileSizeString, count);
		searchResultList.add(searchResult);
	}

	private static int computeOccurrenceCount(
			final String filePathString,
			final TextFinder textFinder) {

		int occurrenceCount = 0;
		try (BufferedReader bufferedReader = ReaderUtils.openBufferedReader(filePathString)) {

			String line;
			while ((line = bufferedReader.readLine()) != null) {

				final int lineOccurrenceCount = textFinder.countOccurrencesInString(line);
				occurrenceCount += lineOccurrenceCount;
			}

		} catch (final Exception exc) {
			Logger.printError("failed to compute occurrence count in file" +
					System.lineSeparator() + filePathString);
			Logger.printException(exc);
		}
		return occurrenceCount;
	}

	@Override
	protected void error() {

		new CustomAlertError("error", "error occurred while searching").showAndWait();
	}

	@Override
	protected void finish() {

		vBoxFileSearcher.updateSearchResults(searchResultList,
				fileCount, fileContainingTextCount, textFinder);
	}

	List<SearchResult> getSearchResultList() {
		return searchResultList;
	}
}
