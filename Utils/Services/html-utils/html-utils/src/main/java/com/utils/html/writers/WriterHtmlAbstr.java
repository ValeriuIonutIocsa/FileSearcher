package com.utils.html.writers;

import java.util.List;
import java.util.Map;

import com.utils.html.sections.HtmlSection;

public interface WriterHtmlAbstr {

	String createTitle();

	void fillSpecificHeadHtmlSectionList(
			List<HtmlSection> htmlSectionList);

	String createCssString();

	void fillBodyAttributesMap(
			Map<String, String> bodyAttributesMap);
}
