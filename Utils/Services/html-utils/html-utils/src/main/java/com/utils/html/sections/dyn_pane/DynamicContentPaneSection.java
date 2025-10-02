package com.utils.html.sections.dyn_pane;

import com.utils.html.sections.HtmlSection;
import com.utils.string.StrUtils;

final class DynamicContentPaneSection {

	private boolean active;
	private final String title;
	private final HtmlSection htmlSection;

	DynamicContentPaneSection(
			final boolean active,
			final String title,
			final HtmlSection htmlSection) {

		this.active = active;
		this.title = title;
		this.htmlSection = htmlSection;
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	void setActive(
			final boolean active) {
		this.active = active;
	}

	boolean isActive() {
		return active;
	}

	String getTitle() {
		return title;
	}

	HtmlSection getHtmlSection() {
		return htmlSection;
	}
}
