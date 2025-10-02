package com.utils.html.sections.dyn_pane;

import org.junit.jupiter.api.Test;

import com.utils.io.PathUtils;
import com.utils.log.Logger;

class HtmlSectionDynamicContentPaneTest {

	@Test
	void testWriteHtml() {

		final String outputPathString = PathUtils.computePath(PathUtils.createRootPath(), "IVI", "tmp",
				"html-utils", "Outputs", "DynamicContentPaneTest.html");

		Logger.printProgress("writing output file:");
		Logger.printLine(outputPathString);

		new WriterHtmlDynamicContentPaneTest().writeToFile(outputPathString);
	}
}
