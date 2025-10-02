package com.utils.html.sections.parents.links;

import com.utils.io.ResourceFileUtils;

public class HtmlSectionLinkedCssResource extends HtmlSectionLink {

	public HtmlSectionLinkedCssResource(
			final String cssResourceFilePath) {

		super(createHref(cssResourceFilePath), "stylesheet", "text/css");
	}

	private static String createHref(
			final String cssResourceFilePath) {

		return ResourceFileUtils.resourceFileToUrl(cssResourceFilePath).toExternalForm();
	}
}
