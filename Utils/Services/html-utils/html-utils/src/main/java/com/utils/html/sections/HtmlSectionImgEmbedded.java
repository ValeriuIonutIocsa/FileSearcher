package com.utils.html.sections;

import com.utils.html.HtmlUtils;
import com.utils.xml.stax.XmlStAXWriter;

public class HtmlSectionImgEmbedded extends AbstractHtmlSectionWithAttributes<HtmlSectionImgEmbedded> {

	private final byte[] imageFileByteArray;

	public HtmlSectionImgEmbedded(
			final byte[] imageFileByteArray) {

		super("img");

		this.imageFileByteArray = imageFileByteArray;
	}

	@Override
	protected void writeL2(
			final XmlStAXWriter xmlStAXWriter) {

		final String src = HtmlUtils.createImgSrc(imageFileByteArray);
		xmlStAXWriter.writeAttribute("src", src);
	}

	@Override
	protected HtmlSectionImgEmbedded self() {
		return this;
	}
}
