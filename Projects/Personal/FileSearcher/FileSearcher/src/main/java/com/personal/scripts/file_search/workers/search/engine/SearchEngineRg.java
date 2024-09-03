package com.personal.scripts.file_search.workers.search.engine;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.personal.scripts.file_search.text_find.TextFinder;
import com.personal.scripts.file_search.workers.search.SearchData;
import com.personal.scripts.file_search.workers.search.engine.data.FirstOccurrenceData;
import com.utils.gui.alerts.CustomAlertException;
import com.utils.io.processes.InputStreamReaderThread;
import com.utils.io.processes.ReadBytesHandlerLinesCollect;
import com.utils.io.processes.ReadBytesHandlerLinesPrint;
import com.utils.log.Logger;
import com.utils.string.StrUtils;

public class SearchEngineRg implements SearchEngine {

	private final SearchData searchData;

	public SearchEngineRg(
			final SearchData searchData) {

		this.searchData = searchData;
	}

	@Override
	public void parseFilePaths(
			final List<String> dirPathStringList,
			final List<String> filePathStringList) {

		try {
			final String rgExePathString = searchData.rgExePathString();

			final String globOption;
			final boolean caseSensitivePathPattern = searchData.caseSensitivePathPattern();
			if (caseSensitivePathPattern) {
				globOption = "--glob";
			} else {
				globOption = "--iglob";
			}

			final String filePathPatternString = searchData.filePathPatternString();
			final String[] globPatternStringArray = StringUtils.split(filePathPatternString.trim(), ' ');

			final String searchFolderPathString = searchData.searchFolderPathString();

			final List<String> commandPartList = new ArrayList<>();
			Collections.addAll(commandPartList, rgExePathString);
			for (final String globPatternString : globPatternStringArray) {
				Collections.addAll(commandPartList, globOption, globPatternString);
			}
			Collections.addAll(commandPartList, "--unrestricted", "--hidden", "--files",
					searchFolderPathString);

			Logger.printProgress("executing command:");
			Logger.printLine(StringUtils.join(commandPartList, ' '));

			final Process process = new ProcessBuilder()
					.command(commandPartList)
					.start();

			final ReadBytesHandlerLinesCollect readBytesHandlerLinesCollect =
					new ReadBytesHandlerLinesCollect();

			final InputStreamReaderThread inputStreamReaderThread = new InputStreamReaderThread(
					"rg parse file paths input stream reader", process.getInputStream(),
					StandardCharsets.UTF_8, readBytesHandlerLinesCollect);
			inputStreamReaderThread.start();

			final InputStreamReaderThread errorInputStreamReaderThread = new InputStreamReaderThread(
					"rg parse file paths error input stream reader", process.getErrorStream(),
					StandardCharsets.UTF_8, new ReadBytesHandlerLinesPrint());
			errorInputStreamReaderThread.start();

			process.waitFor();

			final List<String> lineList = readBytesHandlerLinesCollect.getLineList();
			for (final String line : lineList) {

				if (line.startsWith(searchFolderPathString)) {
					filePathStringList.add(line);
				}
			}

		} catch (final Exception exc) {
			Logger.printException(exc);
		}
	}

	@Override
	public void searchText(
			final List<String> filePathStringList,
			final TextFinder textFinder,
			final Map<String, Integer> filePathStringToOccurrenceCountMap) {

		try {
			final String rgExePathString = searchData.rgExePathString();

			final String globOption;
			final boolean caseSensitivePathPattern = searchData.caseSensitivePathPattern();
			if (caseSensitivePathPattern) {
				globOption = "--glob";
			} else {
				globOption = "--iglob";
			}

			final String filePathPatternString = searchData.filePathPatternString();
			final String[] globPatternStringArray = StringUtils.split(filePathPatternString.trim(), ' ');

			final String regexOption;
			final boolean useRegex = searchData.useRegex();
			if (useRegex) {
				regexOption = "--regexp";
			} else {
				regexOption = "--fixed-strings";
			}

			final String caseSensitiveOption;
			final boolean caseSensitive = searchData.caseSensitive();
			if (caseSensitive) {
				caseSensitiveOption = "--case-sensitive";
			} else {
				caseSensitiveOption = "--ignore-case";
			}

			final String searchText = searchData.searchText();

			final String searchFolderPathString = searchData.searchFolderPathString();

			final List<String> commandPartList = new ArrayList<>();
			Collections.addAll(commandPartList, rgExePathString);
			for (final String globPatternString : globPatternStringArray) {
				Collections.addAll(commandPartList, globOption, globPatternString);
			}
			Collections.addAll(commandPartList, "--unrestricted", "--hidden",
					regexOption, caseSensitiveOption, "--count-matches", "--text", searchText,
					searchFolderPathString);

			Logger.printProgress("executing command:");
			Logger.printLine(StringUtils.join(commandPartList, ' '));

			final Process process = new ProcessBuilder()
					.command(commandPartList)
					.start();

			final ReadBytesHandlerLinesCollect readBytesHandlerLinesCollect =
					new ReadBytesHandlerLinesCollect();

			final InputStreamReaderThread inputStreamReaderThread = new InputStreamReaderThread(
					"rg search text input stream reader", process.getInputStream(),
					StandardCharsets.UTF_8, readBytesHandlerLinesCollect);
			inputStreamReaderThread.start();

			final InputStreamReaderThread errorInputStreamReaderThread = new InputStreamReaderThread(
					"rg search text error input stream reader", process.getErrorStream(),
					StandardCharsets.UTF_8, new ReadBytesHandlerLinesPrint());
			errorInputStreamReaderThread.start();

			process.waitFor();

			final List<String> lineList = readBytesHandlerLinesCollect.getLineList();
			fillFilePathStringToTextFinderDataMap(lineList, filePathStringToOccurrenceCountMap);

		} catch (final Exception exc) {
			Logger.printException(exc);
		}
	}

	private static void fillFilePathStringToTextFinderDataMap(
			final List<String> lineList,
			final Map<String, Integer> filePathStringToOccurrenceCountMap) {

		for (final String line : lineList) {

			final int indexOf = line.lastIndexOf(':');
			if (indexOf > 0) {

				final String filePathString = line.substring(0, indexOf);

				final String occurrenceCountString = line.substring(indexOf + 1);
				final int occurrenceCount = StrUtils.tryParsePositiveInt(occurrenceCountString);

				if (occurrenceCount > 0) {
					filePathStringToOccurrenceCountMap.put(filePathString, occurrenceCount);
				}
			}
		}
	}

	@Override
	public FirstOccurrenceData parseFirstOccurrenceData(
			final String filePathString,
			final TextFinder textFinder) {

		int firstOccurrenceRow = 0;
		int firstOccurrenceCol = 0;
		try {
			final String rgExePathString = searchData.rgExePathString();

			final String regexOption;
			final boolean useRegex = searchData.useRegex();
			if (useRegex) {
				regexOption = "--regexp";
			} else {
				regexOption = "--fixed-strings";
			}

			final String caseSensitiveOption;
			final boolean caseSensitive = searchData.caseSensitive();
			if (caseSensitive) {
				caseSensitiveOption = "--case-sensitive";
			} else {
				caseSensitiveOption = "--ignore-case";
			}

			final String searchText = searchData.searchText();

			final List<String> commandPartList = new ArrayList<>();
			Collections.addAll(commandPartList, rgExePathString, regexOption, caseSensitiveOption,
					"--text", "--line-number", "--column", searchText, filePathString);

			Logger.printProgress("executing command:");
			Logger.printLine(StringUtils.join(commandPartList, ' '));

			final Process process = new ProcessBuilder()
					.command(commandPartList)
					.start();

			final ReadBytesHandlerLinesCollect readBytesHandlerLinesCollect =
					new ReadBytesHandlerLinesCollect();

			final InputStreamReaderThread inputStreamReaderThread = new InputStreamReaderThread(
					"rg search text input stream reader", process.getInputStream(),
					StandardCharsets.UTF_8, readBytesHandlerLinesCollect);
			inputStreamReaderThread.start();

			final InputStreamReaderThread errorInputStreamReaderThread = new InputStreamReaderThread(
					"rg search text error input stream reader", process.getErrorStream(),
					StandardCharsets.UTF_8, new ReadBytesHandlerLinesPrint());
			errorInputStreamReaderThread.start();

			process.waitFor();

			final List<String> lineList = readBytesHandlerLinesCollect.getLineList();
			for (final String line : lineList) {

				final String[] splitPartArray = StringUtils.split(line, ':');
				if (splitPartArray.length >= 3) {

					final String firstOccurrenceRowString = splitPartArray[0];
					final int tmpFirstOccurrenceRow = StrUtils.tryParsePositiveInt(firstOccurrenceRowString);

					final String firstOccurrenceColString = splitPartArray[1];
					int tmpFirstOccurrenceCol = StrUtils.tryParsePositiveInt(firstOccurrenceColString);

					final String matchedLine = splitPartArray[2];
					final int tabOccurrenceCount = StringUtils.countMatches(matchedLine, '\t');
					tmpFirstOccurrenceCol += tabOccurrenceCount * 3;

					if (tmpFirstOccurrenceRow > 0 && tmpFirstOccurrenceCol > 0) {

						firstOccurrenceRow = tmpFirstOccurrenceRow;
						firstOccurrenceCol = tmpFirstOccurrenceCol;
						break;
					}
				}
			}

		} catch (final Exception exc) {
			new CustomAlertException("failed to find first occurrence in file",
					"error occurred while searching for first text occurrence " +
							"in file:" + System.lineSeparator() + filePathString, exc).showAndWait();
		}
		return new FirstOccurrenceData(firstOccurrenceRow, firstOccurrenceCol);
	}
}
