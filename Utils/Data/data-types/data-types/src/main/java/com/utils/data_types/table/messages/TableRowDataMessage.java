package com.utils.data_types.table.messages;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import com.utils.data_types.DataInfo;
import com.utils.data_types.data_items.DataItem;
import com.utils.data_types.data_items.di_boolean.FactoryDataItemBoolean;
import com.utils.data_types.data_items.objects.FactoryDataItemObjectComparable;
import com.utils.data_types.table.TableColumnData;
import com.utils.data_types.table.TableRowData;
import com.utils.log.messages.MessageType;
import com.utils.string.StrUtils;

public class TableRowDataMessage implements TableRowData {

	@Serial
    private static final long serialVersionUID = -4482668257460205647L;

	public static final String MESSAGES_COLUMN_NAME = "Messages";

	private static final TableColumnData[] COLUMNS_DATA = {
			new TableColumnData("Category", "Category", 0.1),
			new TableColumnData("Type", "Type", 0.2),
			new TableColumnData("Text", "Text", 0.7)
	};

	@Override
	public DataItem<?>[] getDataItemArray() {
		return new DataItem<?>[] {
				FactoryDataItemBoolean.newInstance(category),
				FactoryDataItemObjectComparable.newInstance(type),
				FactoryDataItemObjectComparable.newInstance(text)
		};
	}

	private static final TableColumnData[] COLUMNS_TABLE = {
			new TableColumnData(MESSAGES_COLUMN_NAME, "Messages", 1.0)
	};

	@Override
	public DataItem<?>[] getTableViewDataItemArray() {
		return new DataItem<?>[] {
				FactoryDataItemObjectComparable.newInstance(text)
		};
	}

	public static final DataInfo DATA_INFO = new DataInfo(
			"-messages", "Messages",
			"Messages", "Message", -1, COLUMNS_DATA, COLUMNS_TABLE);

	private final boolean category;
	private final MessageType type;
	private final String text;

	private final List<TableRowDataMessage> childTableRowDataMessageList;

	public TableRowDataMessage(
			final boolean category,
			final MessageType type,
			final String text) {

		this.category = category;
		this.type = type;
		this.text = text;

		childTableRowDataMessageList = new ArrayList<>();
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	public boolean isCategory() {
		return category;
	}

	public MessageType getType() {
		return type;
	}

	public List<TableRowDataMessage> getChildTableRowDataMessageList() {
		return childTableRowDataMessageList;
	}
}
