package com.utils.xml.stax;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Stack;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.utils.io.StreamUtils;
import com.utils.log.Logger;
import com.utils.string.exc.SilentException;

public abstract class AbstractXmlStAXReader implements XmlStAXReader {

	private final InputStream inputStream;
	private final XMLEventReader xmlEventReader;

	protected AbstractXmlStAXReader(
			final InputStream inputStreamParam) {

		InputStream inputStream = null;
		XMLEventReader xmlEventReader = null;
		try {
			inputStream = new BufferedInputStream(inputStreamParam);

			System.setProperty(
					"javax.xml.stream.XMLInputFactory",
					"com.sun.xml.internal.stream.XMLInputFactoryImpl");
			final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
			xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, true);
			xmlEventReader = xmlInputFactory.createXMLEventReader(inputStream);

		} catch (final Throwable throwable) {
			Logger.printError("failed to create the XML reader");
			Logger.printThrowable(throwable);
		}
		this.inputStream = inputStream;
		this.xmlEventReader = xmlEventReader;
	}

	protected AbstractXmlStAXReader(
			final String xmlFilePathString) {

		InputStream inputStream = null;
		XMLEventReader xmlEventReader = null;
		try {
			inputStream = StreamUtils.openInputStream(xmlFilePathString);

			System.setProperty("javax.xml.stream.XMLInputFactory",
					"com.sun.xml.internal.stream.XMLInputFactoryImpl");
			final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
			xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, true);
			xmlEventReader = xmlInputFactory.createXMLEventReader(inputStream);

		} catch (final Throwable throwable) {
			Logger.printError("failed to create the XML reader");
			Logger.printThrowable(throwable);
		}
		this.inputStream = inputStream;
		this.xmlEventReader = xmlEventReader;
	}

	@Override
	public final boolean readXml() {

		boolean success = false;
		if (xmlEventReader != null) {

			try {
				final Stack<String> pathInXml = new Stack<>();
				while (xmlEventReader.hasNext()) {

					final XMLEvent xmlEvent = xmlEventReader.nextEvent();
					if (xmlEvent.isStartElement()) {

						final StartElement startElement = xmlEvent.asStartElement();
						final String localPart = startElement.getName().getLocalPart();
						pathInXml.push(localPart);
						final ParseXmlEventResult parseXmlEventResult = parseXmlEvent(pathInXml, startElement);
						if (parseXmlEventResult != ParseXmlEventResult.CONTINUE_READING) {
							break;
						}

					} else if (xmlEvent.isEndElement()) {

						final ParseXmlEventResult parseXmlEventResult = parseXmlEvent(pathInXml, xmlEvent);
						if (parseXmlEventResult != ParseXmlEventResult.CONTINUE_READING) {
							break;
						}
						pathInXml.pop();

					} else {
						final ParseXmlEventResult parseXmlEventResult = parseXmlEvent(pathInXml, xmlEvent);
						if (parseXmlEventResult != ParseXmlEventResult.CONTINUE_READING) {
							break;
						}
					}
				}
				success = true;

			} catch (final SilentException ignored) {
			} catch (final Throwable throwable) {
				Logger.printError("failed to read XML file");
				Logger.printThrowable(throwable);

			} finally {
				closeStreams();
			}
		}
		return success;
	}

	protected abstract ParseXmlEventResult parseXmlEvent(
			Stack<String> pathInXml,
			XMLEvent xmlEvent) throws SilentException;

	private void closeStreams() {

		try {
			xmlEventReader.close();
			inputStream.close();
		} catch (final Throwable ignored) {
		}
	}
}
