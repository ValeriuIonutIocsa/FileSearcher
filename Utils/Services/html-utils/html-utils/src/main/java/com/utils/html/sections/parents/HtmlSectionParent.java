package com.utils.html.sections.parents;

import com.utils.html.sections.HtmlSection;
import com.utils.html.sections.HtmlSectionWithAttributes;

public interface HtmlSectionParent extends HtmlSectionWithAttributes<HtmlSectionParent> {

	HtmlSectionParent addHtmlSection(
			HtmlSection htmlSection);
}
