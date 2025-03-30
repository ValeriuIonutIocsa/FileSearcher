package com.personal.scripts.file_search.workers.search.engine;

import java.util.Map;

import com.utils.io.processes.AbstractReadBytesHandlerLines;
import com.utils.string.StrUtils;

class ReadBytesHandlerLinesRgSearchText extends AbstractReadBytesHandlerLines {

	private final Map<String, Integer> filePathStringToOccurrenceCountMap;

	ReadBytesHandlerLinesRgSearchText(
			final Map<String, Integer> filePathStringToOccurrenceCountMap) {

		this.filePathStringToOccurrenceCountMap = filePathStringToOccurrenceCountMap;
	}

	@Override
	protected void handleLine(
			final String line) {

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
