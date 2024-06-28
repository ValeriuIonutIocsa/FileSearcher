package com.utils.html.sections.scripts;

import com.utils.html.sections.AbstractHtmlSection;
import com.utils.html.sections.HtmlSectionPlainText;
import com.utils.xml.stax.XmlStAXWriter;

public abstract class AbstractHtmlSectionScript extends AbstractHtmlSection {

	AbstractHtmlSectionScript() {
	}

	@Override
	public void write(
			final XmlStAXWriter xmlStAXWriter) {

		final String scriptTagName = "script";
		xmlStAXWriter.writeStartElement(scriptTagName);
		xmlStAXWriter.writeAttribute("type", "text/javascript");

		final String jsScriptContent = createJsScriptContent();
		new HtmlSectionPlainText(jsScriptContent).write(xmlStAXWriter);

		xmlStAXWriter.writeEndElement(scriptTagName);
	}

	protected abstract String createJsScriptContent();
}
