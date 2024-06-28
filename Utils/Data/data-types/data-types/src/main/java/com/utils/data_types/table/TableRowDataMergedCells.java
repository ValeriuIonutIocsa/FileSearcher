package com.utils.data_types.table;

public interface TableRowDataMergedCells<
		TableRowDataMergedCellsT extends TableRowDataMergedCells<?>>
		extends TableRowData {

	boolean checkFirstCell(
			TableRowDataMergedCellsT beforeRowData);

	boolean checkLastCell(
			TableRowDataMergedCellsT afterRowData);
}
