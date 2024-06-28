package com.utils.gui.objects.list_select;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.utils.data_types.table.TableRowData;
import com.utils.gui.objects.tables.table_view.CustomTableView;

public abstract class AbstractCustomListSelectionViewOneToOne<
		TableRowDataTLeft extends TableRowData,
		TableRowDataTRight extends TableRowData>
		extends AbstractCustomListSelectionView<TableRowDataTLeft, TableRowDataTRight> {

	private final Comparator<TableRowDataTLeft> leftItemComparator;
	private final Comparator<TableRowDataTRight> rightItemComparator;

	protected AbstractCustomListSelectionViewOneToOne(
			final double leftSizeRatio,
			final List<TableRowDataTLeft> leftItemList,
			final List<TableRowDataTRight> rightItemList,
			final Comparator<TableRowDataTLeft> leftItemComparator,
			final Comparator<TableRowDataTRight> rightItemComparator) {

		super(leftSizeRatio, leftItemList, rightItemList);

		this.leftItemComparator = leftItemComparator;
		this.rightItemComparator = rightItemComparator;
	}

	@Override
	void moveToRight() {

		final List<TableRowDataTLeft> newLeftItemList = new ArrayList<>();

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
			} else {
				newLeftItemList.add(itemLeft);
			}
		}
		if (rightItemComparator != null) {
			newRightItemList.sort(rightItemComparator);
		}

		customTableViewLeft.setItems(newLeftItemList);
		customTableViewRight.setItems(newRightItemList);
		updateButtons();
	}

	@Override
	void moveAllToRight() {

		final List<TableRowDataTLeft> newLeftItemList = new ArrayList<>();

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
			} else {
				newLeftItemList.add(itemLeft);
			}
		}
		if (rightItemComparator != null) {
			newRightItemList.sort(rightItemComparator);
		}

		customTableViewLeft.setItems(newLeftItemList);
		customTableViewRight.setItems(newRightItemList);
		updateButtons();
	}

	@Override
	void moveToLeft() {

		final List<TableRowDataTRight> newRightItemList = new ArrayList<>();

		final CustomTableView<TableRowDataTLeft> customTableViewLeft = getCustomTableViewLeft();
		final List<TableRowDataTLeft> leftItemList = customTableViewLeft.getItems();
		final List<TableRowDataTLeft> newLeftItemList = new ArrayList<>(leftItemList);

		final CustomTableView<TableRowDataTRight> customTableViewRight = getCustomTableViewRight();
		final List<TableRowDataTRight> selectedRightItemList =
				customTableViewRight.getSelectionModel().getSelectedItems();

		final List<TableRowDataTRight> unfilteredRightItemList = customTableViewRight.getUnfilteredItemList();
		for (final TableRowDataTRight itemRight : unfilteredRightItemList) {

			if (selectedRightItemList.contains(itemRight)) {
				final TableRowDataTLeft itemLeft = convertRightToLeft(itemRight);
				newLeftItemList.add(itemLeft);
			} else {
				newRightItemList.add(itemRight);
			}
		}
		if (leftItemComparator != null) {
			newLeftItemList.sort(leftItemComparator);
		}

		customTableViewLeft.setItems(newLeftItemList);
		customTableViewRight.setItems(newRightItemList);
		updateButtons();
	}

	@Override
	void moveAllToLeft() {

		final List<TableRowDataTRight> newRightItemList = new ArrayList<>();

		final CustomTableView<TableRowDataTLeft> customTableViewLeft = getCustomTableViewLeft();
		final List<TableRowDataTLeft> leftItemList = customTableViewLeft.getItems();
		final List<TableRowDataTLeft> newLeftItemList = new ArrayList<>(leftItemList);

		final CustomTableView<TableRowDataTRight> customTableViewRight = getCustomTableViewRight();
		final List<TableRowDataTRight> rightItemList = customTableViewRight.getItems();

		final List<TableRowDataTRight> unfilteredRightItemList = customTableViewRight.getUnfilteredItemList();
		for (final TableRowDataTRight itemRight : unfilteredRightItemList) {

			if (rightItemList.contains(itemRight)) {
				final TableRowDataTLeft itemLeft = convertRightToLeft(itemRight);
				newLeftItemList.add(itemLeft);
			} else {
				newRightItemList.add(itemRight);
			}
		}
		if (leftItemComparator != null) {
			newLeftItemList.sort(leftItemComparator);
		}

		customTableViewLeft.setItems(newLeftItemList);
		customTableViewRight.setItems(newRightItemList);
		updateButtons();
	}

	protected abstract TableRowDataTRight convertLeftToRight(
			TableRowDataTLeft leftItem);

	protected abstract TableRowDataTLeft convertRightToLeft(
			TableRowDataTRight rightItem);
}
