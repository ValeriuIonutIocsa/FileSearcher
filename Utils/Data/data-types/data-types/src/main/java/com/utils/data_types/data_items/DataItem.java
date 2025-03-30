package com.utils.data_types.data_items;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.utils.json.JsonWriter;
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
			JsonWriter jsonWriter);

	void writeToXml(
			XmlStAXWriter xmlStAXWriter,
			String columnTitleName);

	ObjectT createXlsxValue();

	void setIndent(
			short indent);

	short getIndent();
}
