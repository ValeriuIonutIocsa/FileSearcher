package com.personal.scripts.file_search.workers.search.engine.data;

import com.utils.string.StrUtils;

public record FirstOccurrenceData(
		int firstOccurrenceRow,
		int firstOccurrenceCol) {

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
