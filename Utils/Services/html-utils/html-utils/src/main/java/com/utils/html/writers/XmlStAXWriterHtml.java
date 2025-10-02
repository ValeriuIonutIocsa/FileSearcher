package com.utils.html.writers;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.utils.html.sections.HtmlSection;
import com.utils.html.sections.HtmlSectionPlainText;
import com.utils.xml.stax.AbstractXmlStAXWriter;
import com.utils.xml.stax.XmlStAXWriter;

class XmlStAXWriterHtml extends AbstractXmlStAXWriter {

	private final AbstractWriterHtml abstractWriterHtml;

	XmlStAXWriterHtml(
			final OutputStream outputStream,
			final String indentString,
			AbstractWriterHtml abstractWriterHtml) {

		super(outputStream, indentString);

		this.abstractWriterHtml = abstractWriterHtml;
	}

	@Override
	protected void write() {

		writePlainText("<!-- saved from url=(0014)about:internet -->");
		final String htmlTagName = "html";
		writeStartElement(htmlTagName);
		writeAttribute("lang", "en");

		final String headTagName = "head";
		writeStartElement(headTagName);
		writeHead(this);
		writeEndElement(headTagName);

		final String bodyTagName = "body";
		writeStartElement(bodyTagName);
		writeBody(this);
		writeEndElement(bodyTagName);

		writeEndElement(htmlTagName);
	}

	private void writeHead(
			final XmlStAXWriter xmlStAXWriter) {

		final String title = abstractWriterHtml.createTitle();
		if (StringUtils.isNotBlank(title)) {

			final String titleTagName = "title";
			writeStartElement(titleTagName);
			new HtmlSectionPlainText(title).write(xmlStAXWriter);

			writeEndElement(titleTagName);
		}

		final String metaTagName = "meta";

		xmlStAXWriter.writeStartElement(metaTagName);
		xmlStAXWriter.writeAttribute("http-equiv", "X-UA-Compatible");
		xmlStAXWriter.writeAttribute("content", "IE=10; IE=9; IE=8; IE=7; IE=EDGE");
		xmlStAXWriter.writeEndElement(metaTagName);

		xmlStAXWriter.writeStartElement(metaTagName);
		xmlStAXWriter.writeAttribute("http-equiv", "Content-Type");
		xmlStAXWriter.writeAttribute("content", "text/html; charset=UTF-8");
		xmlStAXWriter.writeEndElement(metaTagName);

		writeSpecificHead(xmlStAXWriter);

		final String cssString = abstractWriterHtml.createCssString();
		if (StringUtils.isNotBlank(cssString)) {

			final String styleTagName = "style";
			xmlStAXWriter.writeStartElement(styleTagName);
			xmlStAXWriter.writeAttribute("xml:space", "preserve");
			new HtmlSectionPlainText(cssString).write(xmlStAXWriter);

			xmlStAXWriter.writeEndElement(styleTagName);
		}
	}

	private void writeSpecificHead(
			final XmlStAXWriter xmlStAXWriter) {

		final List<HtmlSection> htmlSectionList = new ArrayList<>();
		abstractWriterHtml.fillSpecificHeadHtmlSectionList(htmlSectionList);

		for (final HtmlSection htmlSection : htmlSectionList) {
			htmlSection.write(xmlStAXWriter);
		}
	}

	private void writeBody(
			XmlStAXWriter xmlStAXWriter) {

		final Map<String, String> bodyAttributesMap = new LinkedHashMap<>();
		abstractWriterHtml.fillBodyAttributesMap(bodyAttributesMap);

		for (final Map.Entry<String, String> mapEntry : bodyAttributesMap.entrySet()) {

			final String key = mapEntry.getKey();
			final String value = mapEntry.getValue();
			xmlStAXWriter.writeAttribute(key, value);
		}

		final List<HtmlSection> htmlSectionList = new ArrayList<>();
		abstractWriterHtml.fillBodyHtmlSectionList(htmlSectionList);

		for (final HtmlSection htmlSectionBody : htmlSectionList) {
			htmlSectionBody.write(xmlStAXWriter);
		}

		xmlStAXWriter.writePlainText(System.lineSeparator());
	}
}
