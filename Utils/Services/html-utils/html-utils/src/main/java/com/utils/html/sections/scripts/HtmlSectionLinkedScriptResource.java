package com.utils.html.sections.scripts;

import com.utils.io.ResourceFileUtils;

public class HtmlSectionLinkedScriptResource extends HtmlSectionLinkedScript {

	public HtmlSectionLinkedScriptResource(
			final String scriptResourceFilePath) {

		super(ResourceFileUtils.resourceFileToUrl(scriptResourceFilePath).toExternalForm());
	}
}
