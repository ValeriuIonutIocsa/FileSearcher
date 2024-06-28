package com.utils.data_types.data_items;

import java.io.PrintStream;

import com.utils.json.JsonUtils;
import com.utils.xml.stax.XmlStAXWriter;

public abstract class AbstractDataItem<
		ObjectT> implements DataItem<ObjectT> {

	@Override
	public String createCsvString() {
		return createCopyString();
	}

	@Override
	public String createCopyString() {
		return toString();
	}

	@Override
	public void writeToJson(
			final String columnName,
			final boolean notLastAttribute,
			final int indentCount,
			final PrintStream printStream) {

		final String csvString = createCsvString();
		JsonUtils.writeStringAttribute(columnName, csvString, notLastAttribute, indentCount, printStream);
	}

	@Override
	public void writeToXml(
			final XmlStAXWriter xmlStAXWriter,
			final String columnTitleName) {

		final String string = createCopyString();
		xmlStAXWriter.writeAttribute(columnTitleName, string);
	}

	@Override
	public ObjectT createXlsxValue() {
		return getValue();
	}
}
