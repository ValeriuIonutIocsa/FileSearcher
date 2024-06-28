package com.utils.data_types.data_items;

import java.io.PrintStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.utils.xml.stax.XmlStAXWriter;

public interface DataItem<
		ObjectT> {

	void serializeToDataBase(
			int index,
			PreparedStatement preparedStatement) throws SQLException;

	ObjectT getValue();

	String createCsvString();

	String createCopyString();

	void writeToJson(
			String columnName,
			boolean notLastAttribute,
			int indentCount,
			PrintStream printStream);

	void writeToXml(
			XmlStAXWriter xmlStAXWriter,
			String columnTitleName);

	ObjectT createXlsxValue();
}
