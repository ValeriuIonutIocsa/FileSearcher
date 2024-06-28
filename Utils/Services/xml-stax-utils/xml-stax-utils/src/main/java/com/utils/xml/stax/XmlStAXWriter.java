package com.utils.xml.stax;

import javax.xml.namespace.QName;
import javax.xml.stream.events.XMLEvent;

import com.utils.annotations.ApiMethod;

public interface XmlStAXWriter {

	@ApiMethod
	boolean writeXml();

	@ApiMethod
	void writeStartDocument();

	@ApiMethod
	void writeStartDocument(
			boolean standalone);

	@ApiMethod
	void writeEndDocument();

	@ApiMethod
	void writeStartElement(
			String tagName);

	@ApiMethod
	void writeEndElement(
			String tagName);

	@ApiMethod
	void writeAttribute(
			String name,
			String value);

	@ApiMethod
	void writeAttribute(
			QName qName,
			String value);

	@ApiMethod
	void writeCharacters(
			String data);

	@ApiMethod
	void writeCData(
			String data);

	@ApiMethod
	void writeXmlEvent(
			XMLEvent xmlEvent);

	@ApiMethod
	void writePlainText(
			String text);

	@ApiMethod
	void flush();
}
