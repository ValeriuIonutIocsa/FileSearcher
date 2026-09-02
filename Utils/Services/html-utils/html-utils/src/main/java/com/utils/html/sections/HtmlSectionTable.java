package com.utils.html.sections;

import java.util.ArrayList;
import java.util.List;

import com.utils.xml.stax.XmlStAXWriter;

public class HtmlSectionTable extends AbstractHtmlSectionWithAttributes<HtmlSectionTable> {

	private final List<HtmlSection> htmlSectionHeadList;
	private final List<HtmlSection> htmlSectionBodyList;

	public HtmlSectionTable() {

		super("table");

		htmlSectionHeadList = new ArrayList<>();
		htmlSectionBodyList = new ArrayList<>();
	}

	@Override
	protected void writeL2(
			final XmlStAXWriter xmlStAXWriter) {

		final String theadTagName = "thead";
		xmlStAXWriter.writeStartElement(theadTagName);
		for (final HtmlSection htmlSectionHead : htmlSectionHeadList) {
			htmlSectionHead.write(xmlStAXWriter);
		}
		xmlStAXWriter.writeEndElement(theadTagName);

		final String tbodyTagName = "tbody";
		xmlStAXWriter.writeStartElement(tbodyTagName);
		for (final HtmlSection htmlSectionBody : htmlSectionBodyList) {
			htmlSectionBody.write(xmlStAXWriter);
		}
		xmlStAXWriter.writeEndElement(tbodyTagName);
	}

	public HtmlSectionTable addHtmlSectionHead(
			final HtmlSection htmlSection) {

		htmlSectionHeadList.add(htmlSection);
		return this;
	}

	public HtmlSectionTable addHtmlSectionBody(
			final HtmlSection htmlSection) {

		htmlSectionBodyList.add(htmlSection);
		return this;
	}
}
