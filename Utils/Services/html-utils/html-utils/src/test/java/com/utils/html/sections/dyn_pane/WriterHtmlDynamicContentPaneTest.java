package com.utils.html.sections.dyn_pane;

import java.util.List;

import com.utils.html.sections.HtmlSection;
import com.utils.html.sections.HtmlSectionText;
import com.utils.html.sections.scripts.HtmlSectionScriptResource;
import com.utils.html.writers.AbstractWriterHtml;

class WriterHtmlDynamicContentPaneTest extends AbstractWriterHtml {

	WriterHtmlDynamicContentPaneTest() {
	}

	@Override
	public String createCssString() {

		return DynamicContentPaneUtils.createDynamicContentPaneCss(250);
	}

	@Override
	protected void fillBodyHtmlSectionList(
			final List<HtmlSection> htmlSectionList) {

		final HtmlSectionDynamicContentPane htmlSectionDynamicContentPane =
				new HtmlSectionDynamicContentPane("1");

		htmlSectionDynamicContentPane.addDynamicContentSection(
				false, "Section 1", new HtmlSectionText("This is the content of Section 1."));
		htmlSectionDynamicContentPane.addDynamicContentSection(
				true, "Section 2", new HtmlSectionText("This is the content of Section 2."));
		htmlSectionDynamicContentPane.addDynamicContentSection(
				false, "Section 3", new HtmlSectionText("This is the content of Section 3."));

		htmlSectionList.add(htmlSectionDynamicContentPane);

		htmlSectionList.add(new HtmlSectionScriptResource(
				"com/utils/html/sections/dyn_pane/dynamic_content_pane.js"));
	}
}
