package com.utils.html.sections.parents.links;

import org.apache.commons.lang3.StringUtils;

import com.utils.html.sections.parents.AbstractHtmlSectionParent;

public class HtmlSectionLink extends AbstractHtmlSectionParent {

	public HtmlSectionLink(
			final String href,
			final String rel,
			final String type) {

		super("link");

		addAttribute("href", href);

		if (StringUtils.isNotBlank(rel)) {
			addAttribute("rel", rel);
		}

		if (StringUtils.isNotBlank(type)) {
			addAttribute("type", "text/css");
		}
	}
}
