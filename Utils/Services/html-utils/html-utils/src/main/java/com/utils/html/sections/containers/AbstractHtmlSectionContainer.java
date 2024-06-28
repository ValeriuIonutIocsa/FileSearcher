package com.utils.html.sections.containers;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.utils.html.sections.AbstractHtmlSection;
import com.utils.html.sections.HtmlSection;
import com.utils.xml.stax.AbstractXmlStAXWriter;
import com.utils.xml.stax.XmlStAXWriter;

public abstract class AbstractHtmlSectionContainer extends AbstractHtmlSection {

	protected AbstractHtmlSectionContainer() {
	}

	@Override
	public void write(
			final XmlStAXWriter xmlStAXWriter) {

		final List<HtmlSection> htmlSectionList = new ArrayList<>();
		fillHtmlSectionList(htmlSectionList);

		for (final HtmlSection htmlSection : htmlSectionList) {
			htmlSection.write(xmlStAXWriter);
		}
	}

	protected abstract void fillHtmlSectionList(
			List<HtmlSection> htmlSectionList);

	public String writeToString() {

		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		new AbstractXmlStAXWriter(byteArrayOutputStream, "") {

			@Override
			protected void write() {
				AbstractHtmlSectionContainer.this.write(this);
			}
		}.writeXml();
		return byteArrayOutputStream.toString();
	}
}
