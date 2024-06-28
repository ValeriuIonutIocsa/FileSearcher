package com.utils.gui.objects.tables.tree_table;

import com.utils.data_types.table.TableRowData;
import com.utils.string.StrUtils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;

public class ChangeListenerTreeTableShownItemCount<
		TableRowDataT extends TableRowData,
		NumberT extends Number>
		implements ChangeListener<NumberT> {

	private final CustomTreeTableView<TableRowDataT> customTreeTableView;

	private final Label shownItemsLabel;

	public ChangeListenerTreeTableShownItemCount(
			final CustomTreeTableView<TableRowDataT> customTreeTableView,
			final Label shownItemsLabel) {

		this.customTreeTableView = customTreeTableView;

		this.shownItemsLabel = shownItemsLabel;
	}

	@Override
	public void changed(
			final ObservableValue<? extends NumberT> observable,
			final NumberT oldValue,
			final NumberT newValue) {

		final int itemCount = newValue.intValue();
		final int totalItemCount = customTreeTableView.computeTotalItemCount();
		changed(itemCount, totalItemCount);
	}

	public void changed(
			final int itemCount,
			final int totalItemCount) {

		final String title = "shown: " + StrUtils.positiveIntToString(itemCount, true) +
				" / " + StrUtils.positiveIntToString(totalItemCount, true);
		shownItemsLabel.setText(title);
	}
}
