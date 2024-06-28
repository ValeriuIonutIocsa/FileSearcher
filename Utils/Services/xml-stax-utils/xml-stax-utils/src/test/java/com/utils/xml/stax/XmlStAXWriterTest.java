package com.utils.xml.stax;

import java.util.Stack;

import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.junit.jupiter.api.Test;

import com.utils.io.PathUtils;
import com.utils.io.ReaderUtils;
import com.utils.io.file_deleters.FactoryFileDeleter;
import com.utils.log.Logger;

class XmlStAXWriterTest {

	@Test
	void testWrite() throws Exception {

		final String tempXmlFilePathString =
				PathUtils.computePath(PathUtils.createRootPath(), "tmp", "xml_stax_writer_test.xml");
		new AbstractXmlStAXWriter(tempXmlFilePathString, "    ") {

			@Override
			protected void write() {

				writeStartDocument();

				final String test1TagName = "test1";
				writeStartElement(test1TagName);

				final String test2TagName = "test2";
				writeStartElement(test2TagName);
				writeAttribute("attribute", "a>b<c");

				final String test3TagName = "test3";
				writeStartElement(test3TagName);
				writeAttribute("before_attribute", "before");
				writeAttribute("after_attribute", "after");

				writeEndElement(test3TagName);

				final String test4TagName = "test4";
				writeStartElement(test4TagName);

				writeEndElement(test4TagName);

				writeEndElement(test2TagName);

				writeEndElement(test1TagName);

				writeEndDocument();
			}

		}.writeXml();

		final String fileToString = ReaderUtils.fileToString(tempXmlFilePathString);
		Logger.printLine(fileToString);

		new AbstractXmlStAXReader(tempXmlFilePathString) {

			@Override
			protected ParseXmlEventResult parseXmlEvent(
					final Stack<String> pathInXml,
					final XMLEvent xmlEvent) {

				if (xmlEvent.isStartElement()) {

					final String lastElement = pathInXml.lastElement();
					if ("test2".equals(lastElement)) {

						final StartElement startElement = xmlEvent.asStartElement();
						final String attributeValue =
								XmlStAXReader.getAttribute(startElement, "attribute");
						Logger.printLine("attribute value: " + attributeValue);
					}
				}
				return ParseXmlEventResult.CONTINUE_READING;
			}
		}.readXml();

		FactoryFileDeleter.getInstance().deleteFile(tempXmlFilePathString, false, true);
	}
}
