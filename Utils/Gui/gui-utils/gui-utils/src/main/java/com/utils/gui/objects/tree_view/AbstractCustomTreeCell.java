package com.utils.gui.objects.tree_view;

import com.utils.gui.GuiUtils;

import javafx.scene.control.TreeCell;

public abstract class AbstractCustomTreeCell<
		ObjectT> extends TreeCell<ObjectT> {

	@Override
	protected void updateItem(
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
