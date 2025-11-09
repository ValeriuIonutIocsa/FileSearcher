package com.personal.scripts.file_search.workers.search;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.personal.scripts.file_search.workers.search.engine.type.SearchEngineType;
import com.utils.log.Logger;
import com.utils.test.TestInputUtils;

class GuiWorkerSearchTest {

	private static SearchData searchData;

	@BeforeAll
	static void beforeAll() {

		final SearchEngineType searchEngineType;
		final int inputSearchEngineType = TestInputUtils.parseTestInputNumber("1");
		if (inputSearchEngineType == 1) {
			searchEngineType = SearchEngineType.RG;
		} else if (inputSearchEngineType == 2) {
			searchEngineType = SearchEngineType.OWN;
		} else {
			throw new RuntimeException();
		}

		searchData = configureSearchData(searchEngineType);
	}

	private static SearchData configureSearchData(
			final SearchEngineType searchEngineType) {

		final String rgExePathString = "C:\\IVI\\Apps\\RipGrep\\rg.exe";

		final String searchFolderPathString;

		final String filePathPatternString;
		final boolean caseSensitivePathPattern;

		final String searchText;
		final boolean useRegex;
		final boolean caseSensitive;
		final boolean multiline;
		final boolean winStyleLineEndings;
		final boolean searchInBinary;

		final int input = TestInputUtils.parseTestInputNumber("3");
		if (input == 1) {

			searchFolderPathString = "C:\\IVI";

			filePathPatternString = "**/*.gradle";
			caseSensitivePathPattern = true;

			searchText = "jarF";
			useRegex = false;
			caseSensitive = true;
			multiline = false;
			winStyleLineEndings = false;
			searchInBinary = true;

		} else if (input == 2) {

			searchFolderPathString = "C:\\IVI\\Prog";

			filePathPatternString = "**/*.java";
			caseSensitivePathPattern = false;

			searchText = "Utils \\{\\w+";
			useRegex = true;
			caseSensitive = false;
			multiline = false;
			winStyleLineEndings = false;
			searchInBinary = true;

		} else if (input == 3) {

			searchFolderPathString = "D:\\gbe\\damda_t14000_spa310\\DAMDA_000U0_000";

			filePathPatternString = "**\\t1*.c" + System.lineSeparator() + "**\\t1*.h";
			caseSensitivePathPattern = false;

			searchText = "cro_dbg_trace";
			useRegex = false;
			caseSensitive = false;
			multiline = false;
			winStyleLineEndings = false;
			searchInBinary = false;

		} else if (input == 11) {

			searchFolderPathString = "D:\\IVI\\Tmp\\FileSearcher\\folder with spaces";

			filePathPatternString = "**/*.h";
			caseSensitivePathPattern = false;

			searchText = "s    */";
			useRegex = false;
			caseSensitive = false;
			multiline = false;
			winStyleLineEndings = false;
			searchInBinary = true;

		} else if (input == 12) {

			searchFolderPathString = "D:\\IVI\\Tmp\\FileSearcher\\folder with spaces";

			filePathPatternString = "**/*.h";
			caseSensitivePathPattern = false;

			searchText = "echo > \"abc\"";
			useRegex = false;
			caseSensitive = false;
			multiline = false;
			winStyleLineEndings = false;
			searchInBinary = true;

		} else if (input == 13) {

			searchFolderPathString = "D:\\IVI\\Tmp\\FileSearcher\\files_with_spaces";

			filePathPatternString = "**\\*8 in*" + System.lineSeparator() + "**\\*bc bc*";
			caseSensitivePathPattern = false;

			searchText = "";
			useRegex = false;
			caseSensitive = false;
			multiline = false;
			winStyleLineEndings = false;
			searchInBinary = false;

		} else if (input == 22) {

			searchFolderPathString = "D:\\gbe\\DAMDA_000U0_000\\work";

			filePathPatternString = "**/*.kts";
			caseSensitivePathPattern = false;

			searchText = ".putInternal(\"PATH\", System.getenv(\"PATH\"))";
			useRegex = false;
			caseSensitive = false;
			multiline = false;
			winStyleLineEndings = false;
			searchInBinary = true;

		} else if (input == 31) {

			searchFolderPathString = "D:\\gbe\\damda_t14000_spa310\\DAMDA_000U0_000";

			filePathPatternString = "**/*as*.src";
			caseSensitivePathPattern = false;

			searchText = "mov\td2,d10\r\n\tret";
			useRegex = false;
			caseSensitive = false;
			multiline = true;
			winStyleLineEndings = true;
			searchInBinary = true;

		} else if (input == 101) {

			searchFolderPathString = "D:\\gbe\\_gbe_dev_repo\\" +
					"1TGBE-BUILD_PLATFORM\\src\\projects\\VWA22_0U0_B00\\build";

			filePathPatternString = "**/*t1*.o";
			caseSensitivePathPattern = true;

			searchText = "cro_dbg_trace";
			useRegex = false;
			caseSensitive = true;
			multiline = false;
			winStyleLineEndings = false;
			searchInBinary = true;

		} else if (input == 102) {

			searchFolderPathString = "D:\\gbe\\_gbe_dev_repo\\" +
					"1TGBE-BUILD_PLATFORM\\src\\projects\\VWA22_0U0_B00\\build";

			filePathPatternString = "**/*t1*.o";
			caseSensitivePathPattern = true;

			searchText = "cro_dbg_trace";
			useRegex = false;
			caseSensitive = true;
			multiline = false;
			winStyleLineEndings = false;
			searchInBinary = false;

		} else {
			throw new RuntimeException();
		}

		return new SearchData(searchEngineType, rgExePathString,
				searchFolderPathString, filePathPatternString, caseSensitivePathPattern,
				searchText, useRegex, caseSensitive, multiline, winStyleLineEndings, searchInBinary);
	}

	@Test
	void testWorkL2() {

		final GuiWorkerSearch guiWorkerSearch = new GuiWorkerSearch(null, searchData, false,
				new RunningProcesses(), null);
		guiWorkerSearch.workL2();

		final List<SearchResult> searchResultList = guiWorkerSearch.getSearchResultList();

		Logger.printNewLine();

		final int matchingFileCount = searchResultList.size();
		Logger.printLine("matching file count: " + matchingFileCount);

		int resultFileCount = 0;
		for (final SearchResult searchResult : searchResultList) {

			final int occurrenceCount = searchResult.getOccurrenceCount();
			if (occurrenceCount > 0) {
				resultFileCount++;
			}
		}
		Logger.printLine("result file count: " + resultFileCount);

		Logger.printNewLine();
		Logger.printLine("detailed results:");
		for (int i = 0; i < searchResultList.size(); i++) {

			final SearchResult searchResult = searchResultList.get(i);
			final int occurrenceCount = searchResult.getOccurrenceCount();
			Logger.printLine(searchResult.createFilePathString() + "   " + occurrenceCount);

			if (i >= 100) {

				Logger.printLine("...");
				break;
			}
		}
	}
}
