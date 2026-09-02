package com.utils.html.sections;

public interface HtmlSectionWithAttributes<
		HtmlSectionT extends HtmlSection> extends HtmlSection {

	HtmlSectionT addAttributeId(
			String attributeId);

	HtmlSectionT addAttributeClass(
			String attributeClass);

	HtmlSectionT addAttribute(
			String name,
			String value);

	String computeAttribute(
			String name);
}
