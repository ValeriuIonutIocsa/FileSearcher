package com.utils.xml.stax.safe;

import java.io.BufferedReader;
import java.util.Stack;

import org.apache.commons.lang3.StringUtils;

import com.utils.io.ReaderUtils;
import com.utils.log.Logger;

public final class XmlSafeParser {

	private XmlSafeParser() {
	}

	public static String work(
			final String xmlFilePathString) {

		String processedXmlFileContents = null;
		try {
			final StringBuilder sbRunnableMeasResultsXmlString = new StringBuilder();
			try (BufferedReader bufferedReader = ReaderUtils.openBufferedReader(xmlFilePathString)) {

				String line;
				final Stack<String> openElementTagNameStack = new Stack<>();
				while ((line = bufferedReader.readLine()) != null) {

					boolean appendLine = false;

					final int openingCharCount = StringUtils.countMatches(line, '<');
					final int closingCharCount = StringUtils.countMatches(line, '>');
					if (openingCharCount == 2 && closingCharCount == 2) {
						appendLine = true;

					} else {
						line = line.trim();
						if (line.startsWith("<") && line.endsWith(">")) {

							if (line.contains("<?") && line.contains("?>")) {
								appendLine = true;

							} else {
								if (line.startsWith("</")) {

									final String currentElementTagName = line.substring(2, line.length() - 1);
									while (!openElementTagNameStack.isEmpty()) {

										final String elementTagName = openElementTagNameStack.pop();
										if (currentElementTagName.equals(elementTagName)) {

											appendLine = true;
											break;

										} else {
											sbRunnableMeasResultsXmlString
													.append("</").append(elementTagName).append('>');
										}
									}

								} else {
									int indexOf = -1;
									final int spaceIndexOf = line.indexOf(' ');
									if (spaceIndexOf > 1) {
										indexOf = spaceIndexOf;

									} else {
										final int closingIndexOf = line.indexOf('>');
										if (closingIndexOf > 1) {
											indexOf = closingIndexOf;
										}
									}
									if (indexOf > 1) {

										final String elementTagName = line.substring(1, indexOf);
										openElementTagNameStack.add(elementTagName);
										appendLine = true;
									}
								}
							}
						}
					}

					if (appendLine) {
						sbRunnableMeasResultsXmlString.append(line).append(System.lineSeparator());
					}
				}
				while (!openElementTagNameStack.isEmpty()) {

					final String elementTagName = openElementTagNameStack.pop();
					sbRunnableMeasResultsXmlString.append("</").append(elementTagName).append('>');
				}
			}
			processedXmlFileContents = sbRunnableMeasResultsXmlString.toString();

		} catch (final Exception exc) {
			Logger.printError("failed to safe parse XML file:" +
					System.lineSeparator() + xmlFilePathString);
			Logger.printException(exc);
		}
		return processedXmlFileContents;
	}
}
