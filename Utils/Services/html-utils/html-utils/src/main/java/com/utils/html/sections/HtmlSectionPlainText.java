package com.utils.html.sections;

import com.utils.xml.stax.XmlStAXWriter;

public class HtmlSectionPlainText extends AbstractHtmlSection {

	private final String text;

	public HtmlSectionPlainText(
			final String text) {

		this.text = text;
	}

	@Override
	public void write(
			final XmlStAXWriter xmlStAXWriter) {

		xmlStAXWriter.writeCharacters("");
		xmlStAXWriter.writePlainText(text);
	}
}
