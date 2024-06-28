package com.utils.html.sections.scripts;

public class HtmlSectionScriptTextFloatTableHead extends HtmlSectionScriptText {

	public HtmlSectionScriptTextFloatTableHead(
			final String tableId) {
		super(createJsFloatTableHead(tableId));
	}

	private static String createJsFloatTableHead(
			final String tableId) {

		return System.lineSeparator() +
				"$(function(){" +
				System.lineSeparator() +
				"    $('#" + tableId + "').floatThead();" +
				System.lineSeparator() +
				"});" +
				System.lineSeparator();
	}
}
