package com.utils.xml.stax;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang3.StringUtils;

import com.utils.io.StreamUtils;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.io.ro_flag_clearers.FactoryReadOnlyFlagClearer;
import com.utils.log.Logger;

public abstract class AbstractXmlStAXWriter implements XmlStAXWriter {

	private final String indentString;
	private final BufferedOutputStream bufferedOutputStream;
	private final XMLEventWriter xmlEventWriter;
	private final XMLEventFactory xmlEventFactory;

	private int lastEventType;
	private int indentLevel;

	protected AbstractXmlStAXWriter(
			final OutputStream outputStream,
			final String indentString) {

		this.indentString = Objects.toString(indentString, "");

		BufferedOutputStream bufferedOutputStream = null;
		XMLEventWriter xmlEventWriter = null;

		try {
			bufferedOutputStream = new BufferedOutputStream(outputStream);

			System.setProperty("javax.xml.stream.XMLOutputFactory",
					"com.sun.xml.internal.stream.XMLOutputFactoryImpl");
			final XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
			xmlEventWriter = xmlOutputFactory.createXMLEventWriter(
					bufferedOutputStream, StandardCharsets.UTF_8.name());

		} catch (final Throwable throwable) {
			Logger.printError("failed to create XML writer");
			Logger.printThrowable(throwable);
		}

		this.bufferedOutputStream = bufferedOutputStream;
		this.xmlEventWriter = xmlEventWriter;

		System.setProperty("javax.xml.stream.XMLEventFactory",
				"com.sun.xml.internal.stream.events.XMLEventFactoryImpl");
		xmlEventFactory = XMLEventFactory.newInstance();
	}

	protected AbstractXmlStAXWriter(
			final String xmlFilePathString,
			final String indentString) {

		this.indentString = Objects.toString(indentString, "");

		BufferedOutputStream bufferedOutputStream = null;
		XMLEventWriter xmlEventWriter = null;

		try {
			FactoryFolderCreator.getInstance().createParentDirectories(xmlFilePathString, false, true);
			FactoryReadOnlyFlagClearer.getInstance().clearReadOnlyFlagFile(xmlFilePathString, false, true);

			bufferedOutputStream = new BufferedOutputStream(StreamUtils.openOutputStream(xmlFilePathString));

			System.setProperty("javax.xml.stream.XMLOutputFactory",
					"com.sun.xml.internal.stream.XMLOutputFactoryImpl");
			final XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
			xmlEventWriter = xmlOutputFactory.createXMLEventWriter(
					bufferedOutputStream, StandardCharsets.UTF_8.name());

		} catch (final Throwable throwable) {
			Logger.printError("failed to create XML writer");
			Logger.printThrowable(throwable);
		}

		this.bufferedOutputStream = bufferedOutputStream;
		this.xmlEventWriter = xmlEventWriter;

		System.setProperty("javax.xml.stream.XMLEventFactory",
				"com.sun.xml.internal.stream.events.XMLEventFactoryImpl");
		xmlEventFactory = XMLEventFactory.newInstance();
	}

	@Override
	public final boolean writeXml() {

		boolean success = false;
		if (xmlEventWriter != null) {

			try {
				write();
				success = true;

			} catch (final Throwable throwable) {
				Logger.printError("failed to write XML file");
				Logger.printThrowable(throwable);

			} finally {
				closeStreams();
			}
		}
		return success;
	}

	@Override
	public boolean writeXmlWithoutClosing() {

		boolean success = false;
		if (xmlEventWriter != null) {

			try {
				write();
				success = true;

			} catch (final Throwable throwable) {
				Logger.printError("failed to write XML file");
				Logger.printThrowable(throwable);
			}
		}
		return success;
	}

	protected abstract void write();

	@Override
	public void closeStreams() {

		try {
			xmlEventWriter.close();
			bufferedOutputStream.close();
		} catch (final Throwable ignored) {
		}
	}

	@Override
	public void writeStartDocument() {
		writeStartDocument(false);
	}

	@Override
	public void writeStartDocument(
			final boolean standalone) {
		writeStartDocument(xmlEventFactory.createStartDocument(
				StandardCharsets.UTF_8.name(), "1.0", standalone));
	}

	protected void writeStartDocument(
			final StartDocument startDocument) {

		if (startDocument.isStandalone()) {
			writePlainText("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
		} else {
			writeXmlEvent(startDocument);
		}
	}

	@Override
	public void writeEndDocument() {
		writeXmlEvent(xmlEventFactory.createEndDocument());
	}

	@Override
	public void writeStartElement(
			final String tagName) {

		if (StringUtils.isNotBlank(tagName)) {
			writeXmlEvent(xmlEventFactory.createStartElement("", null, tagName));
		}
	}

	@Override
	public void writeEndElement(
			final String tagName) {

		if (StringUtils.isNotBlank(tagName)) {
			writeXmlEvent(xmlEventFactory.createEndElement("", null, tagName));
		}
	}

	@Override
	public void writeAttribute(
			final String name,
			final String valueParam) {

		final String value = Objects.toString(valueParam, "");
		writeXmlEvent(xmlEventFactory.createAttribute(name, value));
	}

	@Override
	public void writeAttribute(
			final QName qName,
			final String valueParam) {

		if (qName != null) {

			final String value = Objects.toString(valueParam, "");
			writeXmlEvent(xmlEventFactory.createAttribute(qName, value));
		}
	}

	@Override
	public void writeCharacters(
			final String data) {
		writeXmlEvent(xmlEventFactory.createCharacters(data));
	}

	@Override
	public void writeCData(
			final String data) {
		writeXmlEvent(xmlEventFactory.createCData(data));
	}

	@Override
	public void writeXmlEvent(
			final XMLEvent xmlEvent) {

		if (xmlEvent != null) {

			try {
				final int xmlEventType = xmlEvent.getEventType();
				if (xmlEventType == XMLEvent.START_ELEMENT) {
					writeIndent();
					indentLevel++;

				} else if (xmlEventType == XMLEvent.END_ELEMENT) {
					indentLevel--;
					if (lastEventType == XMLEvent.END_ELEMENT) {
						writeIndent();
					}
				}

				xmlEventWriter.add(xmlEvent);
				lastEventType = xmlEventType;

			} catch (final Throwable throwable) {
				Logger.printError("failed to add element to the XML file");
				Logger.printThrowable(throwable);
			}
		}
	}

	private void writeIndent() {
		writeCharacters(System.lineSeparator() + indentString.repeat(indentLevel));
	}

	@Override
	public void writePlainText(
			final String text) {

		if (StringUtils.isNotEmpty(text)) {

			try {
				bufferedOutputStream.write(text.getBytes(StandardCharsets.UTF_8));

			} catch (final Throwable throwable) {
				Logger.printError("failed to write plain text to the XML file");
				Logger.printThrowable(throwable);
			}
		}
	}

	@Override
	public void flush() {

		try {
			bufferedOutputStream.flush();

		} catch (final Throwable throwable) {
			Logger.printError("failed to flush XML output stream");
			Logger.printThrowable(throwable);
		}
	}
}
