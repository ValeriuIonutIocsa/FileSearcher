package com.utils.html.sections;

import org.apache.commons.lang3.StringUtils;

import com.utils.xml.stax.XmlStAXWriter;

public class HtmlSectionNoBreakText extends AbstractHtmlSection {

	private final String text;

	public HtmlSectionNoBreakText(
			final String text) {

		this.text = text;
	}

	@Override
	public void write(
			final XmlStAXWriter xmlStAXWriter) {

		xmlStAXWriter.writeCharacters("");
		final String noBreakText = StringUtils.replace(text, " ", "&nbsp;");
		xmlStAXWriter.writePlainText(noBreakText);
	}
}
