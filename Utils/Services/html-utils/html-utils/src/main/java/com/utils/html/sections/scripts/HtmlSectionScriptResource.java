package com.utils.html.sections.scripts;

import com.utils.io.ResourceFileUtils;

public class HtmlSectionScriptResource extends AbstractHtmlSectionScript {

	private final String resourceFilePathString;

	public HtmlSectionScriptResource(
			final String resourceFilePathString) {

		this.resourceFilePathString = resourceFilePathString;
	}

	@Override
	protected String createJsScriptContent() {

		final String jsScriptFileContent =
				ResourceFileUtils.resourceFileToString(resourceFilePathString);
		return System.lineSeparator() + jsScriptFileContent;
	}
}
