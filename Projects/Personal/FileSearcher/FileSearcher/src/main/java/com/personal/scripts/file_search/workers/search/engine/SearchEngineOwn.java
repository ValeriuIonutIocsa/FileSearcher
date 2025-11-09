package com.personal.scripts.file_search.workers.search.engine;

import java.io.BufferedReader;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.CharsetMatch;

import com.personal.scripts.file_search.text_find.TextFinder;
import com.personal.scripts.file_search.workers.search.RunningProcesses;
import com.personal.scripts.file_search.workers.search.SearchData;
import com.personal.scripts.file_search.workers.search.engine.data.FirstOccurrenceData;
import com.utils.gui.alerts.CustomAlertError;
import com.utils.gui.alerts.CustomAlertThrowable;
import com.utils.io.ListFileUtils;
import com.utils.io.ReaderUtils;
import com.utils.io.StreamUtils;
import com.utils.log.Logger;
import com.utils.string.regex.RegexUtils;

public class SearchEngineOwn implements SearchEngine {

	private final SearchData searchData;

	public SearchEngineOwn(
			final SearchData searchData) {

		this.searchData = searchData;
	}

	@Override
	public void parseFilePaths(
			final List<String> dirPathStringList,
			final List<String> filePathStringList,
			final RunningProcesses runningProcesses) {

		final List<PathMatcher> pathMatcherList = new ArrayList<>();
		fillPathMatcherList(pathMatcherList);
		if (pathMatcherList.isEmpty()) {
			new CustomAlertError("invalid file path pattern",
					"the file path pattern is not a valid pattern").showAndWait();

		} else {
			final String searchFolderPathString = searchData.searchFolderPathString();
			ListFileUtils.visitFilesRecursively(searchFolderPathString,
					dirPath -> {
						for (final PathMatcher pathMatcher : pathMatcherList) {

							if (pathMatcher.matches(dirPath)) {

								final String dirPathString = dirPath.toString();
								dirPathStringList.add(dirPathString);
								break;
							}
						}
					},
					filePath -> {
						for (final PathMatcher pathMatcher : pathMatcherList) {

							if (pathMatcher.matches(filePath)) {

								final String filePathString = filePath.toString();
								filePathStringList.add(filePathString);
								break;
							}
						}
					});
		}
	}

	private void fillPathMatcherList(
			final List<PathMatcher> pathMatcherList) {

		final String filePathPatternString = searchData.filePathPatternString();
		final String[] globPatternStringArray = RegexUtils.NEW_LINE_PATTERN.split(filePathPatternString);
		for (final String globPatternString : globPatternStringArray) {

			final PathMatcher pathMatcher = tryCreatePathMatcher(globPatternString);
			if (pathMatcher != null) {
				pathMatcherList.add(pathMatcher);
			}
		}
	}

	private static PathMatcher tryCreatePathMatcher(
			final String filePathPatternString) {

		PathMatcher pathMatcher = null;
		try {
			String pathMatcherString = "glob:" + filePathPatternString;
			pathMatcherString = StringUtils.replaceChars(pathMatcherString, '\\', '/');
			pathMatcher = FileSystems.getDefault().getPathMatcher(pathMatcherString);

		} catch (final Exception ignored) {
		}
		return pathMatcher;
	}

	@Override
	public void searchText(
			final List<String> filePathStringList,
			final TextFinder textFinder,
			final Map<String, Integer> filePathStringToOccurrenceCountMap,
			final RunningProcesses runningProcesses) {

		for (final String filePathString : filePathStringList) {

			int occurrenceCount = 0;
			final Charset charset = detectCharset(filePathString);
			try (BufferedReader bufferedReader = ReaderUtils.openBufferedReader(filePathString, charset)) {

				String line;
				while ((line = bufferedReader.readLine()) != null) {

					final int lineOccurrenceCount = textFinder.countOccurrencesInString(line);
					occurrenceCount += lineOccurrenceCount;
				}

			} catch (final Throwable throwable) {
				Logger.printError("failed to compute occurrence count in file:" +
						System.lineSeparator() + filePathString);
				Logger.printThrowable(throwable);
			}
			filePathStringToOccurrenceCountMap.put(filePathString, occurrenceCount);
		}
	}

	static Charset detectCharset(
			final String filePathString) {

		String charsetName = null;
		try (InputStream inputStream = StreamUtils.openBufferedInputStream(filePathString)) {

			final CharsetDetector charsetDetector =
					new CharsetDetector().setText(inputStream);
			final CharsetMatch charsetMatch = charsetDetector.detect();
			charsetName = charsetMatch.getName();

		} catch (final Throwable throwable) {
			Logger.printError("failed to detect charset for file:" +
					System.lineSeparator() + filePathString);
			Logger.printThrowable(throwable);
		}

		final Charset charset;
		if (Strings.CS.startsWith(charsetName, "ISO-") ||
				Strings.CS.startsWith(charsetName, "windows-")) {
			charset = StandardCharsets.ISO_8859_1;
		} else {
			charset = StandardCharsets.UTF_8;
		}
		return charset;
	}

	@Override
	public FirstOccurrenceData parseFirstOccurrenceData(
			final String filePathString,
			final TextFinder textFinder,
			final RunningProcesses runningProcesses) {

		int firstOccurrenceRow = 0;
		int firstOccurrenceCol = 0;
		final Charset charset = detectCharset(filePathString);
		try (BufferedReader bufferedReader = ReaderUtils.openBufferedReader(filePathString, charset)) {

			String line;
			int fileRow = 1;
			while ((line = bufferedReader.readLine()) != null) {

				final int index = textFinder.findIndexInString(line);
				if (index >= 0) {

					firstOccurrenceRow = fileRow;
					firstOccurrenceCol = index + 1;
					break;
				}
				fileRow++;
			}

		} catch (final Throwable throwable) {
			Logger.printThrowable(throwable);
			new CustomAlertThrowable("failed to find first occurrence in file",
					"error occurred while searching for first text occurrence " +
							"in file:" + System.lineSeparator() + filePathString, throwable).showAndWait();
		}
		return new FirstOccurrenceData(firstOccurrenceRow, firstOccurrenceCol);
	}
}
