package com.personal.scripts.file_search.workers.search;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.personal.scripts.file_search.workers.search.engine.type.SearchEngineType;
import com.utils.log.Logger;
import com.utils.string.StrUtils;
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

		final int input = TestInputUtils.parseTestInputNumber("1");
		if (input == 1) {

			searchEngineType = SearchEngineType.RG;
			searchFolderPathString = "C:\\IVI";

			filePathPatternString = "**/*.gradle";
			caseSensitivePathPattern = true;

			searchText = "jarF";
			useRegex = false;
			caseSensitive = true;

		} else if (input == 2) {

			searchEngineType = SearchEngineType.RG;
			searchFolderPathString = "C:\\IVI\\Prog";

			filePathPatternString = "**/*.java";
			caseSensitivePathPattern = false;

			searchText = "Utils \\{\\w+";
			useRegex = true;
			caseSensitive = false;

		} else if (input == 3) {

			searchEngineType = SearchEngineType.RG;
			searchFolderPathString = "D:\\casdev\\gbe\\gbe_dev_repo\\" +
					"1TGBE-BUILD_PLATFORM\\src\\projects\\VWA22_0U0_B00\\build";

			filePathPatternString = "**/*.o";
			caseSensitivePathPattern = true;

			searchText = "cro_dbg_trace";
			useRegex = false;
			caseSensitive = true;

		} else {
			throw new RuntimeException();
		}

		final SearchData searchData = new SearchData(searchEngineType, rgExePathString,
				searchFolderPathString, filePathPatternString, caseSensitivePathPattern,
				searchText, useRegex, caseSensitive);

		final GuiWorkerSearch guiWorkerSearch = new GuiWorkerSearch(null, searchData, false, null);
		guiWorkerSearch.workL2();

		Logger.printNewLine();
		final List<SearchResult> searchResultList = guiWorkerSearch.getSearchResultList();
		Logger.printLine("result count: " +
				StrUtils.positiveIntToString(searchResultList.size(), true));
		for (final SearchResult searchResult : searchResultList) {

			final int occurrenceCount = searchResult.getOccurrenceCount();
			Logger.printLine(searchResult.createFilePathString() + "   " + occurrenceCount);
		}
	}
}
