package com.personal.scripts.file_search.workers.search;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.personal.scripts.file_search.workers.search.engine.type.SearchEngineType;
import com.utils.log.Logger;
import com.utils.test.TestInputUtils;

class GuiWorkerSearchTest {

	@Test
	void testWorkL2() {

		final SearchEngineType searchEngineType;
		final String rgExePathString = "C:\\IVI\\Apps\\RipGrep\\rg.exe";

		final String searchFolderPathString;

		final String filePathPatternString;
		final boolean caseSensitivePathPattern;

		final String searchText;
		final boolean useRegex;
		final boolean caseSensitive;
		final boolean searchInBinary;

		final int input = TestInputUtils.parseTestInputNumber("101");
		if (input == 1) {

			searchEngineType = SearchEngineType.RG;
			searchFolderPathString = "C:\\IVI";

			filePathPatternString = "**/*.gradle";
			caseSensitivePathPattern = true;

			searchText = "jarF";
			useRegex = false;
			caseSensitive = true;
			searchInBinary = true;

		} else if (input == 2) {

			searchEngineType = SearchEngineType.RG;
			searchFolderPathString = "C:\\IVI\\Prog";

			filePathPatternString = "**/*.java";
			caseSensitivePathPattern = false;

			searchText = "Utils \\{\\w+";
			useRegex = true;
			caseSensitive = false;
			searchInBinary = true;

		} else if (input == 11) {

			searchEngineType = SearchEngineType.RG;
			searchFolderPathString = "D:\\IVI_MISC\\Tmp\\FileSearcher\\folder with spaces";

			filePathPatternString = "**/*.h";
			caseSensitivePathPattern = false;

			searchText = "s    */";
			useRegex = false;
			caseSensitive = false;
			searchInBinary = true;

		} else if (input == 21) {

			searchEngineType = SearchEngineType.RG;
			searchFolderPathString = "D:\\IVI_MISC\\Tmp\\FileSearcher\\folder with spaces";

			filePathPatternString = "**/*.h";
			caseSensitivePathPattern = false;

			searchText = "echo > \"abc\"";
			useRegex = false;
			caseSensitive = false;
			searchInBinary = true;

		} else if (input == 22) {

			searchEngineType = SearchEngineType.RG;
			searchFolderPathString = "D:\\gbe\\DAMDA_000U0_000\\work";

			filePathPatternString = "**/*.kts";
			caseSensitivePathPattern = false;

			searchText = ".putInternal(\"PATH\", System.getenv(\"PATH\"))";
			useRegex = false;
			caseSensitive = false;
			searchInBinary = true;

		} else if (input == 101) {

			searchEngineType = SearchEngineType.RG;
			searchFolderPathString = "D:\\gbe\\_gbe_dev_repo\\" +
					"1TGBE-BUILD_PLATFORM\\src\\projects\\VWA22_0U0_B00\\build";

			filePathPatternString = "**/*t1*.o";
			caseSensitivePathPattern = true;

			searchText = "cro_dbg_trace";
			useRegex = false;
			caseSensitive = true;
			searchInBinary = true;

		} else if (input == 102) {

			searchEngineType = SearchEngineType.RG;
			searchFolderPathString = "D:\\gbe\\_gbe_dev_repo\\" +
					"1TGBE-BUILD_PLATFORM\\src\\projects\\VWA22_0U0_B00\\build";

			filePathPatternString = "**/*t1*.o";
			caseSensitivePathPattern = true;

			searchText = "cro_dbg_trace";
			useRegex = false;
			caseSensitive = true;
			searchInBinary = false;

		} else {
			throw new RuntimeException();
		}

		final SearchData searchData = new SearchData(searchEngineType, rgExePathString,
				searchFolderPathString, filePathPatternString, caseSensitivePathPattern,
				searchText, useRegex, caseSensitive, searchInBinary);

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
