package com.personal.scripts.file_search.text_find;

public interface TextFinder {

	int countOccurrencesInString(
			String string);

	int findIndexInString(
			String string);

	String createStringToPutInClipboard();
}
