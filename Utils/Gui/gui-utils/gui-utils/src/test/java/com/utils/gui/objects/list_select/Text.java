package com.utils.gui.objects.list_select;

import java.io.Serial;

import com.utils.data_types.data_items.DataItem;
import com.utils.data_types.data_items.objects.FactoryDataItemObjectComparable;
import com.utils.data_types.table.TableColumnData;
import com.utils.data_types.table.TableRowData;
import com.utils.string.StrUtils;

record Text(
		String text) implements TableRowData {

	@Serial
	private static final long serialVersionUID = -1441603316586307547L;

	static final TableColumnData[] COLUMNS = {
			new TableColumnData("Text", "Text", 1, Double.NaN, Double.NaN)
	};

	@Override
	public DataItem<?>[] getTableViewDataItemArray() {
		return new DataItem<?>[] {
				FactoryDataItemObjectComparable.newInstance(text)
		};
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}
}
