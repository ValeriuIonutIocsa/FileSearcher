package com.utils.html.sections.scripts;

public class HtmlSectionScriptText extends AbstractHtmlSectionScript {

	private final String jsScriptContent;

	public HtmlSectionScriptText(
			final String jsScriptContent) {

		this.jsScriptContent = jsScriptContent;
	}

	@Override
	protected String createJsScriptContent() {
		return jsScriptContent;
	}
}
