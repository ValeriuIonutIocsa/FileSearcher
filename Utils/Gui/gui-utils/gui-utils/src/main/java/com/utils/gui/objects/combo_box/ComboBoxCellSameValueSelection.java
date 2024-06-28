package com.utils.gui.objects.combo_box;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;

public class ComboBoxCellSameValueSelection<
		ValueT> extends ListCell<ValueT> {

	public ComboBoxCellSameValueSelection(
			final ComboBox<ValueT> comboBox) {

		addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {

			comboBox.setValue(null);
			final ValueT item = getItem();
			comboBox.getSelectionModel().select(item);
			mouseEvent.consume();
		});
	}

	@Override
	public void updateItem(
			final ValueT item,
			final boolean empty) {

		super.updateItem(item, empty);
		if (!empty) {
			setText(String.valueOf(item));
		}
	}
}
