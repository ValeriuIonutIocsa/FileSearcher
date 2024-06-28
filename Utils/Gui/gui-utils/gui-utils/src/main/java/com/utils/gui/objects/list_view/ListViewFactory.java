package com.utils.gui.objects.list_view;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.utils.annotations.ApiMethod;
import com.utils.gui.clipboard.ClipboardUtils;

import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.KeyCode;

public final class ListViewFactory {

	private ListViewFactory() {
	}

	@ApiMethod
	public static <
			ObjectT> ListView<ObjectT> createListView(
					final boolean editable,
					final boolean multipleSelection) {

		final ListView<ObjectT> listView = new ListView<>();
		listView.setEditable(editable);

		final SelectionMode selectionMode;
		if (multipleSelection) {
			selectionMode = SelectionMode.MULTIPLE;
		} else {
			selectionMode = SelectionMode.SINGLE;
		}
		listView.getSelectionModel().setSelectionMode(selectionMode);

		listView.setOnKeyPressed(keyEvent -> {

			if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.C) {
				ListViewFactory.copyListViewSelectionToClipboard(listView);
			}
		});

		return listView;
	}

	@ApiMethod
	public static <
			ObjectT> void copyListViewSelectionToClipboard(
					final ListView<ObjectT> listView) {

		final StringBuilder stringBuilder = new StringBuilder();
		final List<ObjectT> selectedItems = listView.getSelectionModel().getSelectedItems();
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
}
