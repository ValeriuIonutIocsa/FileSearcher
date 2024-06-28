package com.utils.gui.objects.tables.table_view;

import com.utils.data_types.table.TableRowData;

public interface TableItemIndexUpdater<
		TableRowDataT extends TableRowData> {

	void updateIndex(
			TableRowDataT tableRowData,
			int index);
}
