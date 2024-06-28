package com.utils.html.sections.containers;

import java.util.List;

import com.utils.html.sections.HtmlSection;
import com.utils.html.sections.HtmlSectionPlainText;
import com.utils.html.sections.HtmlSectionUpArrow;
import com.utils.html.sections.parents.HtmlSectionH3;

public class HtmlSectionEndUpArrow extends AbstractHtmlSectionContainer {

	@Override
	protected void fillHtmlSectionList(
			final List<HtmlSection> htmlSectionList) {

		htmlSectionList.add(new HtmlSectionPlainText(System.lineSeparator()));

		htmlSectionList.add(new HtmlSectionH3()
				.addHtmlSection(new HtmlSectionUpArrow()));
	}
}
