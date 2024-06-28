package com.utils.xml.stax;

import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

import com.utils.annotations.ApiMethod;

public interface XmlStAXReader {

	@ApiMethod
	boolean readXml();

	@ApiMethod
	static String getAttribute(
			final StartElement startElement,
			final String attributeName) {

		String attributeValue = null;
		final Attribute attribute = startElement.getAttributeByName(new QName(attributeName));
		if (attribute != null) {
			attributeValue = attribute.getValue();
		}
		return attributeValue;
	}
}
