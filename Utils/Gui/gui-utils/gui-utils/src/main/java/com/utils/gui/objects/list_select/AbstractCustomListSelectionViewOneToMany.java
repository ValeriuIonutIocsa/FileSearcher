package com.utils.gui.objects.list_select;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.utils.data_types.table.TableRowData;
import com.utils.gui.objects.tables.table_view.CustomTableView;

public abstract class AbstractCustomListSelectionViewOneToMany<
		TableRowDataTLeft extends TableRowData,
		TableRowDataTRight extends TableRowData>
		extends AbstractCustomListSelectionView<TableRowDataTLeft, TableRowDataTRight> {

	private final Comparator<TableRowDataTRight> rightItemComparator;

	protected AbstractCustomListSelectionViewOneToMany(
			final double leftSizeRatio,
			final List<TableRowDataTLeft> leftItemList,
			final List<TableRowDataTRight> rightItemList,
			final Comparator<TableRowDataTRight> rightItemComparator) {

		super(leftSizeRatio, leftItemList, rightItemList);

		this.rightItemComparator = rightItemComparator;
	}

	@Override
	void moveToRight() {

		final CustomTableView<TableRowDataTRight> customTableViewRight = getCustomTableViewRight();
		final List<TableRowDataTRight> rightItemList = customTableViewRight.getItems();
		final List<TableRowDataTRight> newRightItemList = new ArrayList<>(rightItemList);

		final CustomTableView<TableRowDataTLeft> customTableViewLeft = getCustomTableViewLeft();
		final List<TableRowDataTLeft> selectedLeftItemList =
				customTableViewLeft.getSelectionModel().getSelectedItems();

		final List<TableRowDataTLeft> unfilteredLeftItemList = customTableViewLeft.getUnfilteredItemList();
		for (final TableRowDataTLeft itemLeft : unfilteredLeftItemList) {

			if (selectedLeftItemList.contains(itemLeft)) {
				final TableRowDataTRight itemRight = convertLeftToRight(itemLeft);
				newRightItemList.add(itemRight);
			}
		}
		if (rightItemComparator != null) {
			newRightItemList.sort(rightItemComparator);
		}

		customTableViewRight.setItems(newRightItemList);
		updateButtons();
	}

	@Override
	void moveAllToRight() {

		final CustomTableView<TableRowDataTRight> customTableViewRight = getCustomTableViewRight();
		final List<TableRowDataTRight> rightItemList = customTableViewRight.getItems();
		final List<TableRowDataTRight> newRightItemList = new ArrayList<>(rightItemList);

		final CustomTableView<TableRowDataTLeft> customTableViewLeft = getCustomTableViewLeft();
		final List<TableRowDataTLeft> leftItemList = customTableViewLeft.getItems();

		final List<TableRowDataTLeft> unfilteredLeftItemList = customTableViewLeft.getUnfilteredItemList();
		for (final TableRowDataTLeft itemLeft : unfilteredLeftItemList) {

			if (leftItemList.contains(itemLeft)) {
				final TableRowDataTRight itemRight = convertLeftToRight(itemLeft);
				newRightItemList.add(itemRight);
			}
		}
		if (rightItemComparator != null) {
			newRightItemList.sort(rightItemComparator);
		}

		customTableViewRight.setItems(newRightItemList);
		updateButtons();
	}

	@Override
	void moveToLeft() {

		final List<TableRowDataTRight> newRightItemList = new ArrayList<>();

		final CustomTableView<TableRowDataTRight> customTableViewRight = getCustomTableViewRight();
		final List<TableRowDataTRight> selectedRightItemList =
				customTableViewRight.getSelectionModel().getSelectedItems();

		final List<TableRowDataTRight> unfilteredRightItemList =
				customTableViewRight.getUnfilteredItemList();
		for (final TableRowDataTRight itemRight : unfilteredRightItemList) {

			if (!selectedRightItemList.contains(itemRight)) {
				newRightItemList.add(itemRight);
			}
		}
		if (rightItemComparator != null) {
			newRightItemList.sort(rightItemComparator);
		}

		customTableViewRight.setItems(newRightItemList);
		updateButtons();
	}

	@Override
	void moveAllToLeft() {

		final List<TableRowDataTRight> newRightItemList = new ArrayList<>();

		final CustomTableView<TableRowDataTRight> customTableViewRight = getCustomTableViewRight();
		final List<TableRowDataTRight> rightItemList = customTableViewRight.getItems();

		final List<TableRowDataTRight> unfilteredRightItemList =
				customTableViewRight.getUnfilteredItemList();
		for (final TableRowDataTRight itemRight : unfilteredRightItemList) {

			if (!rightItemList.contains(itemRight)) {
				newRightItemList.add(itemRight);
			}
		}
		if (rightItemComparator != null) {
			newRightItemList.sort(rightItemComparator);
		}

		customTableViewRight.setItems(newRightItemList);
		updateButtons();
	}

	protected abstract TableRowDataTRight convertLeftToRight(
			TableRowDataTLeft leftItem);
}
