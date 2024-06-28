package com.utils.gui.objects.tree_view;

import com.utils.gui.GuiUtils;

import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.util.StringConverter;

public abstract class AbstractCustomEditableTreeCell<
		ObjectT> extends TextFieldTreeCell<ObjectT> {

	public AbstractCustomEditableTreeCell(
			final StringConverter<ObjectT> stringConverter) {
		
		super(stringConverter);
	}

	@Override
	public void updateItem(
			final ObjectT item,
			final boolean empty) {

		super.updateItem(item, empty);

		if (empty || item == null) {
			GuiUtils.updateEmptyCell(this);
		} else {
			updateNonEmptyItem(item);
		}
	}

	protected abstract void updateNonEmptyItem(
			ObjectT item);
}
