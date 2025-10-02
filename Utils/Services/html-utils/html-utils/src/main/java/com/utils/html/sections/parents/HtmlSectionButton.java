package com.utils.html.sections.parents;

public class HtmlSectionButton extends AbstractHtmlSectionParent {

	public HtmlSectionButton() {
		super("button");
	}

	public AbstractHtmlSectionParent addAttributeOnClick(
			final String onClick) {
		return addAttribute("onclick", onClick);
	}
}
