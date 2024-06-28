package com.utils.html.sections;

import com.utils.xml.stax.XmlStAXWriter;

public class HtmlSectionCheckBox extends AbstractHtmlSection {

	private final boolean value;

	public HtmlSectionCheckBox(
            final boolean value) {

		this.value = value;
	}

	@Override
	public void write(
            final XmlStAXWriter xmlStAXWriter) {

		final String inputTagName = "input";
		xmlStAXWriter.writeStartElement(inputTagName);
		xmlStAXWriter.writeAttribute("type", "checkbox");
		xmlStAXWriter.writeAttribute("disabled", "disabled");
		if (value) {
            xmlStAXWriter.writeAttribute("checked", "checked");
        }

		xmlStAXWriter.writeEndElement(inputTagName);
	}
}
