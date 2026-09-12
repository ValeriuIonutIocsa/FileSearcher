package com.utils.html.sections.parents;

import java.util.ArrayList;
import java.util.List;

import com.utils.html.sections.AbstractHtmlSectionWithAttributes;
import com.utils.html.sections.HtmlSection;
import com.utils.xml.stax.XmlStAXWriter;

public abstract class AbstractHtmlSectionParent extends AbstractHtmlSectionWithAttributes<HtmlSectionParent>
		implements HtmlSectionParent {

	private final List<HtmlSection> htmlSectionList;

	protected AbstractHtmlSectionParent(
			final String tagName) {

		super(tagName);

		htmlSectionList = new ArrayList<>();
	}

	@Override
	protected void writeL2(
			final XmlStAXWriter xmlStAXWriter) {

		for (final HtmlSection htmlSection : htmlSectionList) {
			htmlSection.write(xmlStAXWriter);
		}
	}

	@Override
	public HtmlSectionParent addHtmlSection(
			final HtmlSection htmlSection) {

		htmlSectionList.add(htmlSection);
		return self();
	}

	@Override
	protected HtmlSectionParent self() {
		return this;
	}
}
