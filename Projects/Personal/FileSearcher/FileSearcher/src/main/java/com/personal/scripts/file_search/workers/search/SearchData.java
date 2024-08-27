package com.personal.scripts.file_search.workers.search;

import com.personal.scripts.file_search.workers.search.engine.type.SearchEngineType;
import com.utils.string.StrUtils;

public record SearchData(
		SearchEngineType searchEngineType,
		String rgExePathString,

		String searchFolderPathString,

		String filePathPatternString,
		boolean caseSensitivePathPattern,

		String searchText,
		boolean useRegex,
		boolean caseSensitive) {

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
