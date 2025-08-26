package com.utils.gui.objects.tree_view;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.utils.gui.clipboard.ClipboardUtils;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;

public class CustomTreeView<
		ObjectT> extends TreeView<ObjectT> {

	public CustomTreeView(
			final boolean editable,
			final boolean multipleSelection,
			final boolean showRoot) {

		super();

		setEditable(editable);

		final SelectionMode selectionMode;
		if (multipleSelection) {
			selectionMode = SelectionMode.MULTIPLE;
		} else {
			selectionMode = SelectionMode.SINGLE;
		}
		getSelectionModel().setSelectionMode(selectionMode);
		setShowRoot(showRoot);

		setOnKeyPressed(keyEvent -> {

			if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.MINUS ||
					keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.SUBTRACT) {
				deselectAllKeyCombinationPressed();

			} else if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.C) {
				copyKeyCombinationPressed();
			} else if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.V) {
				pasteKeyCombinationPressed();
			}
		});
	}

	protected void deselectAllKeyCombinationPressed() {

		getSelectionModel().clearSelection();
	}

	protected void copyKeyCombinationPressed() {

		final StringBuilder stringBuilder = new StringBuilder();
		final List<TreeItem<ObjectT>> selectedItems = getSelectionModel().getSelectedItems();
		final int selectedItemCount = selectedItems.size();
		for (int i = 0; i < selectedItemCount; i++) {

			final TreeItem<ObjectT> selectedItem = selectedItems.get(i);
			final String selectedItemText = selectedItem.getValue().toString();
			stringBuilder.append(selectedItemText);

			if (i < selectedItemCount - 1) {
				stringBuilder.append(System.lineSeparator());
			}
		}

		final String clipboardString = stringBuilder.toString();
		if (StringUtils.isNotBlank(clipboardString)) {
			ClipboardUtils.putStringInClipBoard(clipboardString);
		}
	}

	protected void pasteKeyCombinationPressed() {
	}
}
