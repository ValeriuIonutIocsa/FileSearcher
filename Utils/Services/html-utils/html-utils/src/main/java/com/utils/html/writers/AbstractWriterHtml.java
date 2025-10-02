package com.utils.html.writers;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import com.utils.html.sections.HtmlSection;
import com.utils.io.StreamUtils;
import com.utils.io.folder_creators.FactoryFolderCreator;
import com.utils.io.ro_flag_clearers.FactoryReadOnlyFlagClearer;
import com.utils.log.Logger;
import com.utils.string.StrUtils;

public abstract class AbstractWriterHtml implements WriterHtml, WriterHtmlAbstr {

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

		new XmlStAXWriterHtml(outputStream, "", this).writeXml();
	}

	@Override
	public String createTitle() {
		return null;
	}

	@Override
	public void fillSpecificHeadHtmlSectionList(
			final List<HtmlSection> htmlSectionList) {
	}

	@Override
	public String createCssString() {
		return null;
	}

	@Override
	public void fillBodyAttributesMap(
			final Map<String, String> bodyAttributesMap) {
	}

	protected abstract void fillBodyHtmlSectionList(
			List<HtmlSection> htmlSectionList);

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
