package com.utils.html.sections;

import java.util.LinkedHashMap;
import java.util.Map;

import com.utils.xml.stax.XmlStAXWriter;

public abstract class AbstractHtmlSectionWithAttributes<
		HtmlSectionT extends HtmlSection>
		implements HtmlSectionWithAttributes<HtmlSectionT> {

	private final String tagName;

	private final Map<String, String> attributeMap;

	protected AbstractHtmlSectionWithAttributes(
			final String tagName) {

		this.tagName = tagName;

		attributeMap = new LinkedHashMap<>();
	}

	@Override
	public void write(
			final XmlStAXWriter xmlStAXWriter) {

		xmlStAXWriter.writeStartElement(tagName);
		writeAttributes(xmlStAXWriter);
		writeL2(xmlStAXWriter);
		xmlStAXWriter.writeEndElement(tagName);
	}

	protected abstract void writeL2(
			XmlStAXWriter xmlStAXWriter);

	protected void writeAttributes(
			final XmlStAXWriter xmlStAXWriter) {

		for (final Map.Entry<String, String> mapEntry : attributeMap.entrySet()) {

			final String name = mapEntry.getKey();
			final String value = mapEntry.getValue();
			if (value != null) {
				xmlStAXWriter.writeAttribute(name, value);
			}
		}
	}

	@Override
	public HtmlSectionT addAttributeId(
			final String attributeId) {

		return addAttribute("id", attributeId);
	}

	@Override
	public HtmlSectionT addAttributeClass(
			final String attributeClass) {

		return addAttribute("class", attributeClass);
	}

	@Override
	public HtmlSectionT addAttribute(
			final String name,
			final String value) {

		attributeMap.put(name, value);
		return self();
	}

	protected abstract HtmlSectionT self();

	@Override
	public String computeAttribute(
			final String name) {

		return attributeMap.get(name);
	}
}
