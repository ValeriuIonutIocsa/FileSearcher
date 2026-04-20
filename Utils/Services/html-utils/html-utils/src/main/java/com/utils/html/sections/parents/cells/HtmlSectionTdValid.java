package com.utils.html.sections.parents.cells;

import com.utils.html.sections.HtmlSectionPlainText;

public class HtmlSectionTdValid extends HtmlSectionTd {

	public HtmlSectionTdValid(
			boolean valid) {

		if (valid) {

			addAttributeClass("td_green_check");
			addHtmlSection(new HtmlSectionPlainText("&#10003;"));

		} else {
			addAttributeClass("td_red_cross");
			addHtmlSection(new HtmlSectionPlainText("&#10006;"));
		}
	}
}
