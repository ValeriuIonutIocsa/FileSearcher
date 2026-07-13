package com.personal.scripts.file_search.workers.search.engine;

import java.util.List;
import java.util.Map;

import com.personal.scripts.file_search.text_find.TextFinder;
import com.personal.scripts.file_search.workers.search.RunningProcesses;
import com.personal.scripts.file_search.workers.search.engine.data.FirstOccurrenceData;
import javafx.scene.Scene;

public interface SearchEngine {

	void parseFilePaths(
			Scene scene,
			List<String> dirPathStringList,
			List<String> filePathStringList,
			RunningProcesses runningProcesses);

	void searchText(
			Scene scene,
			List<String> filePathStringList,
			TextFinder textFinder,
			Map<String, Integer> filePathStringToOccurrenceCountMap,
			RunningProcesses runningProcesses);

	FirstOccurrenceData parseFirstOccurrenceData(
			Scene scene,
			String filePathString,
			TextFinder textFinder,
			RunningProcesses runningProcesses);
}
