package com.utils.html.sections;

import com.utils.xml.stax.XmlStAXWriter;

public class HtmlSectionText extends AbstractHtmlSection {

	private final String text;

	public HtmlSectionText(
			final Object obj) {
		this(String.valueOf(obj));
	}

	public HtmlSectionText(
			final String text) {

		this.text = text;
	}

	@Override
	public void write(
			final XmlStAXWriter xmlStAXWriter) {
		xmlStAXWriter.writeCharacters(text);
	}
}
