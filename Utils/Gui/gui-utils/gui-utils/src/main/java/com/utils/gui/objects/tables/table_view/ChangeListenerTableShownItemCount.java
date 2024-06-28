package com.utils.gui.objects.tables.table_view;

import com.utils.data_types.table.TableRowData;
import com.utils.string.StrUtils;

import javafx.collections.ListChangeListener;
import javafx.scene.control.Label;

public class ChangeListenerTableShownItemCount<
		TableRowDataT extends TableRowData>
		implements ListChangeListener<TableRowDataT> {

	private final CustomTableView<TableRowDataT> customTableView;

	private final Label shownItemsLabel;

	public ChangeListenerTableShownItemCount(
			final CustomTableView<TableRowDataT> customTableView,
			final Label shownItemsLabel) {

		this.customTableView = customTableView;

		this.shownItemsLabel = shownItemsLabel;
	}

	@Override
	public void onChanged(
			final Change<? extends TableRowDataT> change) {

		final int itemCount = change.getList().size();
		final int totalItemCount = customTableView.computeTotalItemCount();
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
