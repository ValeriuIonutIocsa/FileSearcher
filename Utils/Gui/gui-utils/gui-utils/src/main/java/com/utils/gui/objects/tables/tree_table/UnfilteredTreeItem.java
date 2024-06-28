package com.utils.gui.objects.tables.tree_table;

import java.util.ArrayList;
import java.util.List;

import com.utils.annotations.ApiMethod;
import com.utils.data_types.table.TableRowData;
import com.utils.string.StrUtils;

public class UnfilteredTreeItem<
		TableRowDataT extends TableRowData> {

	private final TableRowDataT value;
	private boolean expanded;

	private final List<UnfilteredTreeItem<TableRowDataT>> childrenList;

	public UnfilteredTreeItem(
			final TableRowDataT value,
			final boolean expanded) {

		this.value = value;
		this.expanded = expanded;

		childrenList = new ArrayList<>();
	}

	@Override
	public String toString() {
		return StrUtils.reflectionToString(this);
	}

	@ApiMethod
	public TableRowDataT getValue() {
		return value;
	}

	public void setExpanded(
			final boolean expanded) {
		this.expanded = expanded;
	}

	boolean isExpanded() {
		return expanded;
	}

	@ApiMethod
	public List<UnfilteredTreeItem<TableRowDataT>> getChildrenList() {
		return childrenList;
	}
}
