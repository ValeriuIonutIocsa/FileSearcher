package com.personal.scripts.file_search.text_find;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextFinderRegex implements TextFinder {

	private final Pattern searchPattern;

	public TextFinderRegex(
			final Pattern searchPattern) {

		this.searchPattern = searchPattern;
	}

	@Override
	public int countOccurrencesInString(
			final String string) {

		int occurrenceCount = 0;
		final Matcher matcher = searchPattern.matcher(string);
		while (matcher.find()) {
			occurrenceCount++;
		}
		return occurrenceCount;
	}

	@Override
	public int findIndexInString(
			final String string) {

		int index = -1;
		final Matcher matcher = searchPattern.matcher(string);
		if (matcher.find()) {
			index = matcher.start();
		}
		return index;
	}

	@Override
	public String createStringToPutInClipboard() {
		return "";
	}
}
