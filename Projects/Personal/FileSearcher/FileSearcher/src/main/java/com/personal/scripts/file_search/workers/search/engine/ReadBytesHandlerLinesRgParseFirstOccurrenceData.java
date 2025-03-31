package com.personal.scripts.file_search.workers.search.engine;

import org.apache.commons.lang3.StringUtils;

import com.utils.io.processes.AbstractReadBytesHandlerLines;
import com.utils.string.StrUtils;

class ReadBytesHandlerLinesRgParseFirstOccurrenceData extends AbstractReadBytesHandlerLines {

	private boolean foundFirstOccurrence;
	private int firstOccurrenceRow;
	private int firstOccurrenceCol;

	ReadBytesHandlerLinesRgParseFirstOccurrenceData() {
	}

	@Override
	protected void handleLine(
			final String line) {

		if (!foundFirstOccurrence) {

			final String[] splitPartArray = StringUtils.split(line, ':');
			if (splitPartArray.length >= 3) {

				final String firstOccurrenceRowString = splitPartArray[0];
				final int tmpFirstOccurrenceRow = StrUtils.tryParsePositiveInt(firstOccurrenceRowString);

				final String firstOccurrenceColString = splitPartArray[1];
				int tmpFirstOccurrenceCol = StrUtils.tryParsePositiveInt(firstOccurrenceColString);

				final String matchedLine = splitPartArray[2];
				final int tabOccurrenceCount = StringUtils.countMatches(matchedLine, '\t');
				tmpFirstOccurrenceCol += tabOccurrenceCount * 3;

				if (tmpFirstOccurrenceRow > 0 && tmpFirstOccurrenceCol > 0) {

					foundFirstOccurrence = true;
					firstOccurrenceRow = tmpFirstOccurrenceRow;
					firstOccurrenceCol = tmpFirstOccurrenceCol;
				}
			}
		}
	}

	int getFirstOccurrenceRow() {
		return firstOccurrenceRow;
	}

	int getFirstOccurrenceCol() {
		return firstOccurrenceCol;
	}
}
