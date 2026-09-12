package com.utils.html.sections;

import java.util.List;

import com.utils.html.sections.containers.AbstractHtmlSectionContainer;
import com.utils.html.sections.parents.HtmlSectionButton;

public class HtmlSectionUpArrow extends AbstractHtmlSectionContainer {

	public HtmlSectionUpArrow() {
	}

	@Override
	protected void fillHtmlSectionList(
			final List<HtmlSection> htmlSectionList) {

		final HtmlSectionButton htmlSectionButton = new HtmlSectionButton();
		htmlSectionButton.addHtmlSection(new HtmlSectionPlainText("&uarr;&nbsp;Top"));
		htmlSectionButton.addAttributeOnClick("window.scrollTo(0, 0)");
		htmlSectionButton.addAttributeClass("top");

		htmlSectionList.add(htmlSectionButton);
	}
}
