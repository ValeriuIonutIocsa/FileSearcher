package com.utils.html.writers;

import java.io.OutputStream;

import org.apache.commons.lang3.StringUtils;

import com.utils.html.sections.HtmlSectionPlainText;
import com.utils.xml.stax.AbstractXmlStAXWriter;
import com.utils.xml.stax.XmlStAXWriter;

public abstract class AbstractXmlStAXWriterHtml
		extends AbstractXmlStAXWriter implements XmlStAXWriterHtml {

	protected AbstractXmlStAXWriterHtml(
			final String xmlFilePathString,
			final String indentString) {

		super(xmlFilePathString, indentString);
	}

	protected AbstractXmlStAXWriterHtml(
			final OutputStream outputStream,
			final String indentString) {

		super(outputStream, indentString);
	}

	@Override
	protected void write() {

		writePlainText("<!-- saved from url=(0014)about:internet -->");
		final String htmlTagName = "html";
		writeStartElement(htmlTagName);

		final String headTagName = "head";
		writeStartElement(headTagName);
		writeHead(this);
		writeEndElement(headTagName);

		final String bodyTagName = "body";
		writeStartElement(bodyTagName);
		writeBodyAttributes(this);
		writeBody(this);
		writeEndElement(bodyTagName);

		writeEndElement(htmlTagName);
	}

	private void writeHead(
			final XmlStAXWriter xmlStAXWriter) {

		final String metaTagName = "meta";
		xmlStAXWriter.writeStartElement(metaTagName);
		xmlStAXWriter.writeAttribute("http-equiv", "X-UA-Compatible");
		xmlStAXWriter.writeAttribute("content", "IE=10; IE=9; IE=8; IE=7; IE=EDGE");
		xmlStAXWriter.writeEndElement(metaTagName);

		final String cssString = createCssString();
		if (StringUtils.isNotBlank(cssString)) {

			final String styleTagName = "style";
			xmlStAXWriter.writeStartElement(styleTagName);
			xmlStAXWriter.writeAttribute("xml:space", "preserve");
			new HtmlSectionPlainText(cssString).write(xmlStAXWriter);

			xmlStAXWriter.writeEndElement(styleTagName);
		}

		final String title = createTitle();
		if (StringUtils.isNotBlank(title)) {

			final String titleTagName = "title";
			writeStartElement(titleTagName);
			new HtmlSectionPlainText(title).write(xmlStAXWriter);

			writeEndElement(titleTagName);
		}
	}

	@Override
	public String createCssString() {
		return null;
	}

	@Override
	public String createTitle() {
		return null;
	}

	@Override
	public void writeBodyAttributes(
			final XmlStAXWriter xmlStAXWriter) {
	}

	protected abstract void writeBody(
			XmlStAXWriter xmlStAXWriter);
}
