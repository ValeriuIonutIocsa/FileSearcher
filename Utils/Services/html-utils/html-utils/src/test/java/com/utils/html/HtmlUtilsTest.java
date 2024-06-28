package com.utils.html;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.utils.io.ResourceFileUtils;
import com.utils.log.Logger;
import com.utils.xml.dom.XmlDomUtils;

class HtmlUtilsTest {

	@Test
	void testEmbedImage() throws Exception {

		final Document document = XmlDomUtils.createNewDocument();
		final Element documentElement = document.createElement("html_embed_image_test");
		document.appendChild(documentElement);

		final byte[] imageFileBytes = ResourceFileUtils.resourceFileToByteArray("com/utils/html/test.png");

		HtmlUtils.embedImage(documentElement, imageFileBytes);

		final String htmlFileContents = XmlDomUtils.saveXmlFile(document, false, 4);
		Logger.printStatus("Generated HTML file:");
		Logger.printLine(htmlFileContents);
	}
}
