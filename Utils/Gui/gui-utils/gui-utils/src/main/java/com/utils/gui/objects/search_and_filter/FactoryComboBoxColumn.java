package com.utils.gui.objects.search_and_filter;

import java.util.Collection;

import com.utils.gui.factories.BasicControlsFactories;

import javafx.scene.control.ComboBox;

final class FactoryComboBoxColumn {

	private FactoryComboBoxColumn() {
	}

	public static ComboBox<String> newInstance(
			final SearchAndFilterTable searchAndFilterTable,
			final int columnIndex) {

		final ComboBox<String> comboBoxColumn = BasicControlsFactories.getInstance().createComboBox();

		comboBoxColumn.setMaxWidth(200);
		final Collection<String> columnNames = searchAndFilterTable.getTableColumnNames();
		comboBoxColumn.getItems().addAll(columnNames);
		comboBoxColumn.getSelectionModel().select(columnIndex);

		return comboBoxColumn;
	}
}
