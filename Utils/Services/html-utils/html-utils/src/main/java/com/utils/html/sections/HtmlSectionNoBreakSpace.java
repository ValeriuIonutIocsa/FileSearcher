package com.utils.html.sections;

public class HtmlSectionNoBreakSpace extends HtmlSectionPlainText {

	public HtmlSectionNoBreakSpace(
			final int noBreakSpaceCount) {

		super("&nbsp;".repeat(noBreakSpaceCount));
	}
}
