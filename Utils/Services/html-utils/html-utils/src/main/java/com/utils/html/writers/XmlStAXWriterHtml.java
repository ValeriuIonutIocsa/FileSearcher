package com.utils.html.writers;

import com.utils.xml.stax.XmlStAXWriter;

public interface XmlStAXWriterHtml extends XmlStAXWriter {

	String createCssString();

	String createTitle();

	void writeBodyAttributes(
			XmlStAXWriter xmlStAXWriter);
}
