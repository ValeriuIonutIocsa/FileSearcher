package com.utils.data_types.db;

import com.utils.data_types.table.TableRowData;

public interface TableRowDataWithId extends TableRowData {

	void setId(
			int id);

	int getId();
}
