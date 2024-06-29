package com.personal.scripts.file_search.workers.search;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.utils.log.Logger;
import com.utils.string.StrUtils;
import com.utils.test.TestInputUtils;

class GuiWorkerSearchTest {

	@Test
	void testWorkL2() {

		final String searchFolderPathString;
		final String filePathPatternString;
		final String searchText;
		final boolean useRegex;
		final boolean caseSensitive;
		final int input = TestInputUtils.parseTestInputNumber("2");
		if (input == 1) {
			searchFolderPathString = "C:\\IVI";
			filePathPatternString = "*.gradle";
			searchText = "jarF";
			useRegex = false;
			caseSensitive = true;

		} else if (input == 2) {
			searchFolderPathString = "C:\\IVI\\Prog";
			filePathPatternString = "*.java";
			searchText = "Utils \\{\\w+";
			useRegex = true;
			caseSensitive = false;

		} else {
			throw new RuntimeException();
		}

		final GuiWorkerSearch guiWorkerSearch =
				new GuiWorkerSearch(null, searchFolderPathString, filePathPatternString, searchText,
						useRegex, caseSensitive, false, null);
		guiWorkerSearch.workL2();

		Logger.printNewLine();
		final List<SearchResult> searchResultList = guiWorkerSearch.getSearchResultList();
		Logger.printLine("result count: " +
				StrUtils.positiveIntToString(searchResultList.size(), true));
		for (final SearchResult searchResult : searchResultList) {
			Logger.printLine(searchResult.createFilePathString() + "   " + searchResult.getCount());
		}
	}
}
