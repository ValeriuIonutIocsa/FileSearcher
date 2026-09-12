package com.personal.scripts.file_search.workers.search.engine;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import com.utils.log.Logger;

class SearchEngineOwnTest {

	@TestFactory
	List<DynamicTest> testDetectCharset() {

		final List<DynamicTest> dynamicTestList = new ArrayList<>();
		final List<Integer> testCaseList = Arrays.asList(0, 1, 2);
		if (testCaseList.contains(1)) {
			dynamicTestList.add(DynamicTest.dynamicTest("1", () -> {
				final String filePathString = "D:\\IVI\\Tmp\\FileSearcher\\iso_input.h";
				final Charset expectedCharset = StandardCharsets.ISO_8859_1;
				testDetectCharsetCommon(filePathString, expectedCharset);
			}));
		}
		if (testCaseList.contains(2)) {
			dynamicTestList.add(DynamicTest.dynamicTest("2", () -> {

				final String filePathString = "D:\\IVI\\Tmp\\FileSearcher\\utf_8_input.h";
				final Charset expectedCharset = StandardCharsets.UTF_8;
				testDetectCharsetCommon(filePathString, expectedCharset);
			}));
		}
		return dynamicTestList;
	}

	private static void testDetectCharsetCommon(
			final String filePathString,
			final Charset expectedCharset) {

		final Charset charset = SearchEngineOwn.detectCharset(filePathString);
		Assertions.assertEquals(expectedCharset, charset);

		Logger.printLine("charset: " + charset);
	}
}
