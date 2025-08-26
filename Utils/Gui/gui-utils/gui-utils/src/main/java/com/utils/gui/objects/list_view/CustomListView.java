package com.utils.gui.objects.list_view;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.utils.gui.clipboard.ClipboardUtils;

import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.KeyCode;

public class CustomListView<
		ObjectT> extends ListView<ObjectT> {

	public CustomListView(
			final boolean editable,
			final boolean multipleSelection) {

		super();

		setEditable(editable);

		final SelectionMode selectionMode;
		if (multipleSelection) {
			selectionMode = SelectionMode.MULTIPLE;
		} else {
			selectionMode = SelectionMode.SINGLE;
		}
		getSelectionModel().setSelectionMode(selectionMode);

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
		final List<ObjectT> selectedItems = getSelectionModel().getSelectedItems();
		final int selectedItemCount = selectedItems.size();
		for (int i = 0; i < selectedItemCount; i++) {

			final ObjectT selectedItem = selectedItems.get(i);
			final String selectedItemText = selectedItem.toString();
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
