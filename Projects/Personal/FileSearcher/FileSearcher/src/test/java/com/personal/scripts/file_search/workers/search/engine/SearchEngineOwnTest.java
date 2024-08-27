package com.personal.scripts.file_search.workers.search.engine;

import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;

import com.utils.log.Logger;
import com.utils.test.TestInputUtils;

class SearchEngineOwnTest {

	@Test
	void testDetectCharset() {

		final String filePathString;
		final int input = TestInputUtils.parseTestInputNumber("1");
		if (input == 1) {
			filePathString = "D:\\IVI_MISC\\Tmp\\FileSearcher\\iso_input.h";
		} else if (input == 2) {
			filePathString = "D:\\IVI_MISC\\Tmp\\FileSearcher\\utf8_input.h";
		} else {
			throw new RuntimeException();
		}

		final Charset charset = SearchEngineOwn.detectCharset(filePathString);
		Logger.printLine("charset: " + charset);
	}
}
