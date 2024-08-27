package com.utils.gui.objects.tables.tree_table;

import com.utils.data_types.table.TableRowDataParentChild;

import javafx.css.PseudoClass;

public class CustomTreeTableCellParentChild<
		TableRowDataT extends TableRowDataParentChild>
		extends CustomTreeTableCell<TableRowDataT, Object> {

	private final static PseudoClass PSEUDO_CLASS_PARENT = PseudoClass.getPseudoClass("parent");

	@Override
	protected void updateItem(
			final Object item,
			final boolean empty) {

		super.updateItem(item, empty);

		final boolean parent = checkParent();
		pseudoClassStateChanged(PSEUDO_CLASS_PARENT, parent);
	}

	private boolean checkParent() {

		boolean parent = false;
		final TableRowDataT tableRowData = getRowData();
		if (tableRowData != null) {
			parent = tableRowData.isParent();
		}
		return parent;
	}
}
