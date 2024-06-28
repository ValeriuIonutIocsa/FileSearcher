package com.personal.scripts.file_search.workers.search;

import java.io.Serial;
import java.time.Instant;

import com.utils.data_types.data_items.DataItem;
import com.utils.data_types.data_items.di_int.FactoryDataItemUInt;
import com.utils.data_types.data_items.objects.FactoryDataItemObjectComparable;
import com.utils.data_types.data_items.objects.instant.FactoryDataItemInstant;
import com.utils.data_types.table.TableColumnData;
import com.utils.data_types.table.TableRowData;
import com.utils.io.PathUtils;
import com.utils.string.StrUtils;

public class SearchResult implements TableRowData {

	@Serial
	private static final long serialVersionUID = 5792448166898402974L;

	public static final TableColumnData[] TABLE_COLUMN_DATA_ARRAY = {
			new TableColumnData("File Name", "File Name", 0.2),
			new TableColumnData("Folder Path", "FolderPath", 0.5),
			new TableColumnData("Ext", "Ext", 0.06),
			new TableColumnData("Last Modified", "LastModified", 0.15),
			new TableColumnData("Size", "Size", 0.06),
			new TableColumnData("Count", "Count", 0.06)
	};

	@Override
	public DataItem<?>[] getTableViewDataItemArray() {

		return new DataItem<?>[] {
				FactoryDataItemObjectComparable.newInstance(fileName),
				FactoryDataItemObjectComparable.newInstance(folderPathString),
				FactoryDataItemObjectComparable.newInstance(extension),
				FactoryDataItemInstant.newInstance(lastModifiedInstant),
				FactoryDataItemObjectComparable.newInstance(fileSizeString),
				FactoryDataItemUInt.newInstance(count)
		};
	}

	private final String fileName;
	private final String folderPathString;
	private final String extension;
	private final Instant lastModifiedInstant;
	private final String fileSizeString;
	private final int count;

	SearchResult(
			final String fileName,
			final String folderPathString,
			final String extension,
			final Instant lastModifiedInstant,
			final String fileSizeString,
			final int count) {

		this.fileName = fileName;
		this.folderPathString = folderPathString;
		this.extension = extension;
		this.lastModifiedInstant = lastModifiedInstant;
		this.fileSizeString = fileSizeString;
		this.count = count;
	}

	public String createDetailsString() {

		return "file name: " + fileName + System.lineSeparator() +
				"folder path: " + folderPathString + System.lineSeparator() +
				"extension: " + extension + System.lineSeparator() +
				"file size: " + fileSizeString + System.lineSeparator() +
				"count: " + StrUtils.positiveIntToString(count, true);
	}

	public String createFilePathString() {

		return PathUtils.computePath(folderPathString, fileName);
	}

	boolean checkHasOccurrences() {

		return count > 0;
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	String getFileName() {
		return fileName;
	}

	String getFolderPathString() {
		return folderPathString;
	}

	int getCount() {
		return count;
	}
}
