package com.utils.json;

import java.io.PrintStream;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.function.TriConsumer;

import com.utils.annotations.ApiMethod;
import com.utils.string.StrUtils;

public class JsonWriter {

	private final String indent;
	private final String spacingIndent;
	private final String lineIndent;

	private final PrintStream printStream;

	public JsonWriter(
			final String indent,
			final String spacingIndent,
			final String lineIndent,
			final PrintStream printStream) {

		this.indent = indent;
		this.spacingIndent = spacingIndent;
		this.lineIndent = lineIndent;

		this.printStream = printStream;
	}

	@ApiMethod
	public <
			ObjectT> void writeObject(
					final ObjectT object,
					final int indentCount,
					final TriConsumer<ObjectT, Integer, JsonWriter> writeObjectTriConsumer) {

		StrUtils.printRepeatedString(indent, indentCount, printStream);
		printStream.print('{');
		printStream.print(lineIndent);

		if (object != null) {
			writeObjectTriConsumer.accept(object, indentCount, this);
		}

		StrUtils.printRepeatedString(indent, indentCount, printStream);
		printStream.print('}');
	}

	@ApiMethod
	public <
			ObjectT> void writeListAttribute(
					final String name,
					final Collection<ObjectT> objectCollection,
					final boolean notLastAttribute,
					final int indentCount,
					final TriConsumer<ObjectT, Integer, JsonWriter> writeObjectTriConsumer) {

		StrUtils.printRepeatedString(indent, indentCount + 1, printStream);

		if (StringUtils.isNotBlank(name)) {

			printStream.print('"');
			final String escapedName = escapeJsonString(name);
			printStream.print(escapedName);
			printStream.print("\":");
			printStream.print(spacingIndent);
		}

		if (objectCollection == null) {
			printStream.print("null");

		} else {
			printStream.print('[');
			if (!objectCollection.isEmpty()) {

				printStream.print(lineIndent);

				int index = 0;
				final int size = objectCollection.size();
				for (final ObjectT object : objectCollection) {

					writeObject(object, indentCount + 2, writeObjectTriConsumer);

					if (index < size - 1) {
						printStream.print(',');
					}
					printStream.print(lineIndent);
					index++;
				}

				StrUtils.printRepeatedString(indent, indentCount + 1, printStream);
			}
			printStream.print(']');
		}

		if (notLastAttribute) {
			printStream.print(',');
		}
		printStream.print(lineIndent);
	}

	@ApiMethod
	public <
			ObjectT> void writeObjectAttribute(
					final String name,
					final ObjectT object,
					final boolean notLastAttribute,
					final int indentCount,
					final TriConsumer<ObjectT, Integer, JsonWriter> writeObjectTriConsumer) {

		StrUtils.printRepeatedString(indent, indentCount + 1, printStream);

		if (StringUtils.isNotBlank(name)) {

			printStream.print('"');
			final String escapedName = escapeJsonString(name);
			printStream.print(escapedName);
			printStream.print("\":");
			printStream.print(spacingIndent);
		}

		if (object == null) {
			printStream.print("null");

		} else {
			printStream.print('{');
			printStream.print(lineIndent);

			writeObjectTriConsumer.accept(object, indentCount + 1, this);

			StrUtils.printRepeatedString(indent, indentCount + 1, printStream);
			printStream.print('}');
		}

		if (notLastAttribute) {
			printStream.print(',');
		}
		printStream.print(lineIndent);
	}

	@ApiMethod
	public void writeStringAttribute(
			final String name,
			final String value,
			final boolean notLastAttribute,
			final int indentCount) {

		StrUtils.printRepeatedString(indent, indentCount + 1, printStream);

		if (StringUtils.isNotBlank(name)) {

			printStream.print('"');
			final String escapedName = escapeJsonString(name);
			printStream.print(escapedName);
			printStream.print("\":");
			printStream.print(spacingIndent);
		}

		if (value == null) {
			printStream.print("null");

		} else {
			printStream.print('"');
			final String escapedValue = escapeJsonString(value);
			printStream.print(escapedValue);
			printStream.print('"');
		}

		if (notLastAttribute) {
			printStream.print(',');
		}
		printStream.print(lineIndent);
	}

	@ApiMethod
	public void writeNumberAttribute(
			final String name,
			final String valueString,
			final boolean notLastAttribute,
			final int indentCount) {

		StrUtils.printRepeatedString(indent, indentCount + 1, printStream);

		if (StringUtils.isNotBlank(name)) {

			printStream.print('"');
			final String escapedName = escapeJsonString(name);
			printStream.print(escapedName);
			printStream.print("\":");
			printStream.print(spacingIndent);
		}

		if (valueString == null) {
			printStream.print("null");

		} else {
			final String escapedValue = escapeJsonString(valueString);
			printStream.print(escapedValue);
		}

		if (notLastAttribute) {
			printStream.print(',');
		}
		printStream.print(lineIndent);
	}

	private static String escapeJsonString(
			final String jsonString) {

		String escapedJsonString = jsonString;
		if (escapedJsonString != null) {

			escapedJsonString = escapedJsonString.replace("\\", "\\\\");
			escapedJsonString = escapedJsonString.replace("\"", "\\\"");
			escapedJsonString = escapedJsonString.replace("\b", "\\b");
			escapedJsonString = escapedJsonString.replace("\f", "\\f");
			escapedJsonString = escapedJsonString.replace("\n", "\\n");
			escapedJsonString = escapedJsonString.replace("\r", "\\r");
			escapedJsonString = escapedJsonString.replace("\t", "\\t");
			escapedJsonString = escapedJsonString.replace("/", "\\/");
		}
		return escapedJsonString;
	}
}
