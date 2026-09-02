package com.utils.html.sections.parents;

import com.utils.annotations.ApiMethod;

public class HtmlSectionButton extends AbstractHtmlSectionParent {

	public HtmlSectionButton() {

		super("button");
	}

	@ApiMethod
	public HtmlSectionParent addAttributeOnClick(
			final String onClick) {

		return addAttribute("onclick", onClick);
	}
}
