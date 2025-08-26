package com.utils.html.sections;

import org.apache.commons.lang3.Strings;

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
		final String noBreakText = Strings.CS.replace(text, " ", "&nbsp;");
		xmlStAXWriter.writePlainText(noBreakText);
	}
}
