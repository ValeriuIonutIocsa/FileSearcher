package com.utils.html.sections;

import com.utils.html.HtmlUtils;
import com.utils.xml.stax.XmlStAXWriter;

public class HtmlSectionImage extends AbstractHtmlSection {

	private final byte[] imageFileBytes;

	public HtmlSectionImage(
			final byte[] imageFileBytes) {

		this.imageFileBytes = imageFileBytes;
	}

	@Override
	public void write(
			final XmlStAXWriter xmlStAXWriter) {
		HtmlUtils.embedImage(xmlStAXWriter, imageFileBytes);
	}
}
