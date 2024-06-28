package com.personal.scripts.file_search.text_find;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

public class TextFinderRegular implements TextFinder {

	private final String searchString;
	private final boolean caseSensitive;

	public TextFinderRegular(
			final String searchString,
			final boolean caseSensitive) {

		this.searchString = searchString;
		this.caseSensitive = caseSensitive;
	}

	@Override
	public int countOccurrencesInString(
			final String string) {

		final int occurrenceCount;
		if (caseSensitive) {
			occurrenceCount = StringUtils.countMatches(string, searchString);
		} else {
			occurrenceCount =
					StringUtils.countMatches(string.toLowerCase(Locale.US), searchString);
		}
		return occurrenceCount;
	}

	@Override
	public int findIndexInString(
			final String string) {

		final int index;
		if (caseSensitive) {
			index = StringUtils.indexOfIgnoreCase(string, searchString);
		} else {
			index = StringUtils.indexOf(string, searchString);
		}
		return index;
	}

	@Override
	public String createStringToPutInClipboard() {
		return searchString;
	}
}
