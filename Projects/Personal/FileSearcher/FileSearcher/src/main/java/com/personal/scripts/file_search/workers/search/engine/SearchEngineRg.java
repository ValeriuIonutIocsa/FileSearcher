package com.personal.scripts.file_search.workers.search.engine;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.personal.scripts.file_search.text_find.TextFinder;
import com.personal.scripts.file_search.workers.search.RunningProcesses;
import com.personal.scripts.file_search.workers.search.SearchData;
import com.personal.scripts.file_search.workers.search.engine.data.FirstOccurrenceData;
import com.utils.gui.alerts.CustomAlertError;
import com.utils.gui.alerts.CustomAlertException;
import com.utils.io.processes.InputStreamReaderThread;
import com.utils.io.processes.ReadBytesHandlerLinesPrint;
import com.utils.log.Logger;

public class SearchEngineRg implements SearchEngine {

	private final SearchData searchData;

	public SearchEngineRg(
			final SearchData searchData) {

		this.searchData = searchData;
	}

	@Override
	public void parseFilePaths(
			final List<String> dirPathStringList,
			final List<String> filePathStringList,
			final RunningProcesses runningProcesses) {

		try {
			final List<String> commandPartList = new ArrayList<>();
			final List<String> escapedCommandPartList = new ArrayList<>();

			final String rgExePathString = searchData.rgExePathString();
			commandPartList.add(rgExePathString);
			escapedCommandPartList.add(rgExePathString);

			final String globOption;
			final boolean caseSensitivePathPattern = searchData.caseSensitivePathPattern();
			if (caseSensitivePathPattern) {
				globOption = "--glob";
			} else {
				globOption = "--iglob";
			}

			final String filePathPatternString = searchData.filePathPatternString();
			final String[] globPatternStringArray = StringUtils.split(filePathPatternString.trim(), ' ');

			for (final String globPatternString : globPatternStringArray) {

				Collections.addAll(commandPartList, globOption, globPatternString);
				Collections.addAll(escapedCommandPartList, globOption, "\"" + globPatternString + "\"");
			}

			Collections.addAll(commandPartList, "--unrestricted", "--hidden", "--files");
			Collections.addAll(escapedCommandPartList, "--unrestricted", "--hidden", "--files");

			final String searchFolderPathString = searchData.searchFolderPathString();
			commandPartList.add(searchFolderPathString);
			final String escapedSearchFolderPathString = "\"" + searchFolderPathString + "\"";
			escapedCommandPartList.add(escapedSearchFolderPathString);

			Logger.printProgress("executing command:");
			Logger.printLine(StringUtils.join(escapedCommandPartList, ' '));

			final Process process = new ProcessBuilder()
					.command(commandPartList)
					.start();
			runningProcesses.setRunningProcess(process);

			final InputStreamReaderThread inputStreamReaderThread = new InputStreamReaderThread(
					"rg parse file paths input stream reader", process.getInputStream(),
					StandardCharsets.UTF_8, new ReadBytesHandlerLinesRgParseFilePaths(
							searchFolderPathString, filePathStringList));
			inputStreamReaderThread.start();

			final InputStreamReaderThread errorInputStreamReaderThread = new InputStreamReaderThread(
					"rg parse file paths error input stream reader", process.getErrorStream(),
					StandardCharsets.UTF_8, new ReadBytesHandlerLinesPrint());
			errorInputStreamReaderThread.start();

			final int exitCode = process.waitFor();

			inputStreamReaderThread.join();
			errorInputStreamReaderThread.join();

			if (exitCode != 0) {

				final boolean processStopped = runningProcesses.isProcessStopped();
				if (!processStopped) {

					new CustomAlertError("failed to parse file paths",
							"parse file paths command exited with non-zero code").showAndWait();
				}
			}

		} catch (final Exception exc) {
			Logger.printException(exc);
			new CustomAlertException("failed to parse file paths",
					"error occurred while parsing file paths", exc).showAndWait();

		} finally {
			runningProcesses.setRunningProcess(null);
		}
	}

	@Override
	public void searchText(
			final List<String> filePathStringList,
			final TextFinder textFinder,
			final Map<String, Integer> filePathStringToOccurrenceCountMap,
			final RunningProcesses runningProcesses) {

		try {
			final List<String> commandPartList = new ArrayList<>();
			final List<String> escapedCommandPartList = new ArrayList<>();

			final String rgExePathString = searchData.rgExePathString();
			commandPartList.add(rgExePathString);
			escapedCommandPartList.add(rgExePathString);

			final String globOption;
			final boolean caseSensitivePathPattern = searchData.caseSensitivePathPattern();
			if (caseSensitivePathPattern) {
				globOption = "--glob";
			} else {
				globOption = "--iglob";
			}

			final String filePathPatternString = searchData.filePathPatternString();
			final String[] globPatternStringArray = StringUtils.split(filePathPatternString.trim(), ' ');

			for (final String globPatternString : globPatternStringArray) {

				Collections.addAll(commandPartList, globOption, globPatternString);
				Collections.addAll(escapedCommandPartList, globOption, "\"" + globPatternString + "\"");
			}

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

			Collections.addAll(commandPartList, "--unrestricted", "--hidden",
					regexOption, caseSensitiveOption, "--count-matches", "--text");
			Collections.addAll(escapedCommandPartList, "--unrestricted", "--hidden",
					regexOption, caseSensitiveOption, "--count-matches", "--text");

			String searchText = searchData.searchText();
			searchText = searchText.replace("\"", "\"\"");
			commandPartList.add(searchText);
			final String escapedSearchText = "\"" + searchText + "\"";
			escapedCommandPartList.add(escapedSearchText);

			final String searchFolderPathString = searchData.searchFolderPathString();
			commandPartList.add(searchFolderPathString);
			final String escapedSearchFolderPathString = "\"" + searchFolderPathString + "\"";
			escapedCommandPartList.add(escapedSearchFolderPathString);

			Logger.printProgress("executing command:");
			Logger.printLine(StringUtils.join(escapedCommandPartList, ' '));

			final Process process = new ProcessBuilder()
					.command(commandPartList)
					.start();
			runningProcesses.setRunningProcess(process);

			final InputStreamReaderThread inputStreamReaderThread = new InputStreamReaderThread(
					"rg search text input stream reader", process.getInputStream(),
					StandardCharsets.UTF_8, new ReadBytesHandlerLinesRgSearchText(
							filePathStringToOccurrenceCountMap));
			inputStreamReaderThread.start();

			final InputStreamReaderThread errorInputStreamReaderThread = new InputStreamReaderThread(
					"rg search text error input stream reader", process.getErrorStream(),
					StandardCharsets.UTF_8, new ReadBytesHandlerLinesPrint());
			errorInputStreamReaderThread.start();

			final int exitCode = process.waitFor();

			inputStreamReaderThread.join();
			errorInputStreamReaderThread.join();

			if (exitCode != 0) {

				final boolean processStopped = runningProcesses.isProcessStopped();
				if (!processStopped) {

					new CustomAlertError("failed to search text in files",
							"search text in files command exited with non-zero code").showAndWait();
				}
			}

		} catch (final Exception exc) {
			Logger.printException(exc);
			new CustomAlertException("failed to search text in files",
					"error occurred while searching for text in files", exc).showAndWait();

		} finally {
			runningProcesses.setRunningProcess(null);
		}
	}

	@Override
	public FirstOccurrenceData parseFirstOccurrenceData(
			final String filePathString,
			final TextFinder textFinder,
			final RunningProcesses runningProcesses) {

		int firstOccurrenceRow = 0;
		int firstOccurrenceCol = 0;
		try {
			final List<String> commandPartList = new ArrayList<>();
			final List<String> escapedCommandPartList = new ArrayList<>();

			final String rgExePathString = searchData.rgExePathString();
			commandPartList.add(rgExePathString);
			escapedCommandPartList.add(rgExePathString);

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

			Collections.addAll(commandPartList, regexOption, caseSensitiveOption,
					"--text", "--line-number", "--column");
			Collections.addAll(escapedCommandPartList, regexOption, caseSensitiveOption,
					"--text", "--line-number", "--column");

			String searchText = searchData.searchText();
			searchText = searchText.replace("\"", "\"\"");
			commandPartList.add(searchText);
			final String escapedSearchText = "\"" + searchText + "\"";
			commandPartList.add(escapedSearchText);

			commandPartList.add(filePathString);
			final String escapedFilePathString = "\"" + filePathString + "\"";
			escapedCommandPartList.add(escapedFilePathString);

			Logger.printProgress("executing command:");
			Logger.printLine(StringUtils.join(escapedCommandPartList, ' '));

			final Process process = new ProcessBuilder()
					.command(commandPartList)
					.start();
			runningProcesses.setRunningProcess(process);

			final ReadBytesHandlerLinesRgParseFirstOccurrenceData readBytesHandlerLinesRgParseFirstOccurrenceData =
					new ReadBytesHandlerLinesRgParseFirstOccurrenceData();

			final InputStreamReaderThread inputStreamReaderThread = new InputStreamReaderThread(
					"rg search text input stream reader", process.getInputStream(),
					StandardCharsets.UTF_8, readBytesHandlerLinesRgParseFirstOccurrenceData);
			inputStreamReaderThread.start();

			final InputStreamReaderThread errorInputStreamReaderThread = new InputStreamReaderThread(
					"rg search text error input stream reader", process.getErrorStream(),
					StandardCharsets.UTF_8, new ReadBytesHandlerLinesPrint());
			errorInputStreamReaderThread.start();

			final int exitCode = process.waitFor();

			inputStreamReaderThread.join();
			errorInputStreamReaderThread.join();

			firstOccurrenceRow = readBytesHandlerLinesRgParseFirstOccurrenceData.getFirstOccurrenceRow();
			firstOccurrenceCol = readBytesHandlerLinesRgParseFirstOccurrenceData.getFirstOccurrenceCol();

			if (exitCode != 0) {

				final boolean processStopped = runningProcesses.isProcessStopped();
				if (!processStopped) {

					new CustomAlertError("failed to find first occurrence in file",
							"find first occurrence in file command exited with non-zero code").showAndWait();
				}
			}

		} catch (final Exception exc) {
			Logger.printException(exc);
			new CustomAlertException("failed to find first occurrence in file",
					"error occurred while searching for first text occurrence " +
							"in file:" + System.lineSeparator() + filePathString, exc).showAndWait();

		} finally {
			runningProcesses.setRunningProcess(null);
		}

		return new FirstOccurrenceData(firstOccurrenceRow, firstOccurrenceCol);
	}
}
