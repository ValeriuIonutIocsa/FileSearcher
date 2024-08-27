package com.utils.gui.objects.tables.table_view;

import com.utils.gui.factories.BasicControlsFactories;

import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;

public class CustomTableCellSelectText<
		RowDataT,
		CellDataT>
		extends CustomTableCell<RowDataT, CellDataT> {

	private String text;

	private StackPane stackPane;

	@Override
	public void startEdit() {

		super.startEdit();

		final TextArea textArea = BasicControlsFactories.getInstance().createTextArea(text);
		textArea.setEditable(false);
		final double cellHeight = getHeight();
		textArea.setPrefHeight(cellHeight);
		final double cellWidth = getWidth();
		textArea.setPrefWidth(cellWidth);

		setGraphic(textArea);
	}

	@Override
	public void cancelEdit() {

		super.cancelEdit();

		setGraphic(stackPane);
	}

	@Override
	public void setText(
			final StackPane stackPane,
			final CellDataT item) {

		super.setText(stackPane, item);

		text = item.toString();
		this.stackPane = stackPane;
	}
}
