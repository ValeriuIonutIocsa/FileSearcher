package com.utils.gui.objects.tree_view;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.utils.annotations.ApiMethod;
import com.utils.gui.clipboard.ClipboardUtils;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;

public final class TreeViewFactory {

	private TreeViewFactory() {
	}

	@ApiMethod
	public static <
			ObjectT> TreeView<ObjectT> createTreeView(
					final boolean editable,
					final boolean multipleSelection,
					final boolean showRoot) {

		final TreeView<ObjectT> treeView = new TreeView<>();
		treeView.setEditable(editable);

		final SelectionMode selectionMode;
		if (multipleSelection) {
			selectionMode = SelectionMode.MULTIPLE;
		} else {
			selectionMode = SelectionMode.SINGLE;
		}
		treeView.getSelectionModel().setSelectionMode(selectionMode);
		treeView.setShowRoot(showRoot);

		treeView.setOnKeyPressed(keyEvent -> {

			if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.C) {
				TreeViewFactory.copyTreeViewSelectionToClipboard(treeView);
			}
		});

		return treeView;
	}

	@ApiMethod
	public static <
			ObjectT> void copyTreeViewSelectionToClipboard(
					final TreeView<ObjectT> treeView) {

		final StringBuilder stringBuilder = new StringBuilder();
		final List<TreeItem<ObjectT>> selectedItems = treeView.getSelectionModel().getSelectedItems();
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
}
