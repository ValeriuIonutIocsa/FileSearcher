package com.personal.scripts.file_search.workers.search.engine;

import java.util.List;
import java.util.Map;

import com.personal.scripts.file_search.text_find.TextFinder;
import com.personal.scripts.file_search.workers.search.engine.data.FirstOccurrenceData;

public interface SearchEngine {

	void parseFilePaths(
			List<String> dirPathStringList,
			List<String> filePathStringList);

	void searchText(
			List<String> filePathStringList,
			TextFinder textFinder,
			Map<String, Integer> filePathStringToOccurrenceCountMap);

	FirstOccurrenceData parseFirstOccurrenceData(
			String filePathString,
			TextFinder textFinder);
}