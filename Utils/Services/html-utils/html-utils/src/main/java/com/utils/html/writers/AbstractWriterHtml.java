package com.utils.html.writers;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.utils.html.sections.HtmlSection;
import com.utils.io.StreamUtils;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.io.ro_flag_clearers.FactoryReadOnlyFlagClearer;
import com.utils.log.Logger;
import com.utils.string.StrUtils;
import com.utils.xml.stax.XmlStAXWriter;

public abstract class AbstractWriterHtml implements WriterHtml {

	protected AbstractWriterHtml() {
	}

	@Override
	public void writeToFile(
			final String outputPathString) {

		FactoryFolderCreator.getInstance().createParentDirectories(outputPathString, false, true);
		FactoryReadOnlyFlagClearer.getInstance().clearReadOnlyFlagFile(outputPathString, false, true);

		try (OutputStream outputStream = StreamUtils.openBufferedOutputStream(outputPathString)) {
			write(outputStream);

		} catch (final Throwable throwable) {
			Logger.printError("failed to write HTML to file:" +
					System.lineSeparator() + outputPathString);
			Logger.printThrowable(throwable);
		}
	}

	@Override
	public String writeToString() {

		String str;
		try {
			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			write(byteArrayOutputStream);
			str = byteArrayOutputStream.toString(StandardCharsets.UTF_8);

		} catch (final Throwable throwable) {
			Logger.printError("failed to write HTML to string");
			Logger.printThrowable(throwable);
			str = "";
		}
		return str;
	}

	private void write(
			final OutputStream outputStream) {

		new AbstractXmlStAXWriterHtml(outputStream, "") {

			@Override
			public String createCssString() {

				return AbstractWriterHtml.this.createCssString();
			}

			@Override
			public String createTitle() {

				return AbstractWriterHtml.this.createTitle();
			}

			@Override
			public void writeBodyAttributes(
					final XmlStAXWriter xmlStAXWriter) {

				AbstractWriterHtml.this.writeBodyAttributes(xmlStAXWriter);
			}

			@Override
			protected void writeBody(
					final XmlStAXWriter xmlStAXWriter) {

				AbstractWriterHtml.this.writeBody(xmlStAXWriter);
			}

		}.writeXml();
	}

	@Override
	public String createCssString() {
		return null;
	}

	@Override
	public String createTitle() {
		return null;
	}

	@Override
	public void writeBodyAttributes(
			final XmlStAXWriter xmlStAXWriter) {
	}

	private void writeBody(
			final XmlStAXWriter xmlStAXWriter) {

		final List<HtmlSection> htmlSectionList = new ArrayList<>();
		fillBodyHtmlSectionList(htmlSectionList);

		for (final HtmlSection htmlSectionBody : htmlSectionList) {
			htmlSectionBody.write(xmlStAXWriter);
		}

		xmlStAXWriter.writePlainText(System.lineSeparator());
	}

	protected abstract void fillBodyHtmlSectionList(
			List<HtmlSection> htmlSectionList);

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
