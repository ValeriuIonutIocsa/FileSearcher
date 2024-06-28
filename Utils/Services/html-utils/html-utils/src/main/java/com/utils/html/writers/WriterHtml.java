package com.utils.html.writers;

import com.utils.xml.stax.XmlStAXWriter;

public interface WriterHtml {

	void writeToFile(
			String outputPathString);

	String writeToString();

	String createCssString();

	String createTitle();

	void writeBodyAttributes(
			XmlStAXWriter xmlStAXWriter);
}
