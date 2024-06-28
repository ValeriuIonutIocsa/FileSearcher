package com.utils.data_types.table;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.utils.data_types.data_items.DataItem;
import com.utils.json.JsonUtils;
import com.utils.xml.stax.XmlStAXWriter;

public interface TableRowData extends Serializable {

	int XLSX_CELL_CHARACTER_LIMIT = 32_000;

	default DataItem<?>[] getDataItemArray() {
		return getTableViewDataItemArray();
	}

	DataItem<?>[] getTableViewDataItemArray();

	default void writeToXml(
			final String tagName,
			final TableColumnData[] columnsData,
			final XmlStAXWriter xmlStAXWriter) {

		xmlStAXWriter.writeStartElement(tagName);

		final DataItem<?>[] dataItemArray = getDataItemArray();

		for (int i = 0; i < columnsData.length; i++) {

			final TableColumnData tableColumnData = columnsData[i];
			final String columnName = tableColumnData.getSerializeName();
			if (StringUtils.isNotBlank(columnName)) {

				final DataItem<?> dataItem = dataItemArray[i];
				if (dataItem != null) {
					dataItem.writeToXml(xmlStAXWriter, columnName);
				} else {
					xmlStAXWriter.writeAttribute(columnName, "");
				}
			}
		}
		xmlStAXWriter.writeEndElement(tagName);
	}

	default void writeToCsv(
			final PrintStream printStream) {

		final List<List<String>> csvCellDataByColumnList = new ArrayList<>();
		final DataItem<?>[] dataItemArray = getDataItemArray();
		for (int i = 0; i < dataItemArray.length; i++) {
			csvCellDataByColumnList.add(new ArrayList<>());
		}

		for (int i = 0; i < dataItemArray.length; i++) {

			final DataItem<?> dataItem = dataItemArray[i];
			final List<String> csvCellDataList = csvCellDataByColumnList.get(i);

			if (dataItem != null) {

				final String csvString = dataItem.createCsvString();
				for (int j = 0; j < csvString.length(); j += TableRowData.XLSX_CELL_CHARACTER_LIMIT) {

					String csvStringPart = csvString.substring(j,
							Math.min(j + TableRowData.XLSX_CELL_CHARACTER_LIMIT, csvString.length()));
					csvStringPart = StringUtils.replaceChars(csvStringPart, ',', ';');
					csvStringPart = StringUtils.replace(csvStringPart, "\"", "\"\"");
					csvCellDataList.add(csvStringPart);
				}
			}
		}

		int maxRowCount = 1;
		for (final List<String> csvCellDataList : csvCellDataByColumnList) {
			maxRowCount = Math.max(maxRowCount, csvCellDataList.size());
		}

		for (int i = 0; i < maxRowCount; i++) {

			for (final List<String> csvCellDataList : csvCellDataByColumnList) {

				printStream.print('"');
				if (i < csvCellDataList.size()) {

					final String csvCellData = csvCellDataList.get(i);
					printStream.print(csvCellData);
				}
				printStream.print('"');
				if (i < dataItemArray.length - 1) {
					printStream.print(',');
				}
			}
			printStream.println();
		}
	}

	default void writeToJson(
			final TableColumnData[] columnsData,
			final int indentCount,
			final PrintStream printStream) {

		final Map<TableColumnData, DataItem<?>> notBlankColumnDataMap = new LinkedHashMap<>();
		final DataItem<?>[] dataItemArray = getDataItemArray();
		for (int i = 0; i < columnsData.length; i++) {

			final TableColumnData tableColumnData = columnsData[i];
			final DataItem<?> dataItem = dataItemArray[i];

			final String columnName = tableColumnData.getSerializeName();
			if (StringUtils.isNotBlank(columnName)) {
				notBlankColumnDataMap.put(tableColumnData, dataItem);
			}
		}

		int i = 0;
		for (final Map.Entry<TableColumnData, DataItem<?>> mapEntry : notBlankColumnDataMap.entrySet()) {

			final TableColumnData tableColumnData = mapEntry.getKey();
			final DataItem<?> dataItem = mapEntry.getValue();

			final String columnName = tableColumnData.getSerializeName();
			final boolean notLastAttribute = i < notBlankColumnDataMap.size() - 1;
			if (dataItem != null) {
				dataItem.writeToJson(columnName, notLastAttribute, indentCount, printStream);
			} else {
				JsonUtils.writeStringAttribute(columnName, "", notLastAttribute,
						indentCount, printStream);
			}
			i++;
		}
	}
}
