package com.personal.scripts.file_search;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.personal.scripts.file_search.workers.search.SearchResult;
import com.utils.gui.alerts.CustomAlertException;
import com.utils.gui.clipboard.ClipboardUtils;
import com.utils.gui.objects.tables.table_view.CustomTableCell;
import com.utils.io.IoUtils;
import com.utils.io.file_deleters.FactoryFileDeleter;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

class CustomTableCellSearchResultFileName extends CustomTableCell<SearchResult, Object> {

	CustomTableCellSearchResultFileName() {
	}

	@Override
	public ContextMenu createContextMenu(
			final Object item) {

		ContextMenu contextMenu = null;
		final SearchResult searchResult = getRowData();
		if (searchResult != null) {

			contextMenu = new ContextMenu();

			final MenuItem copyFullPathMenuItem = new MenuItem("copy full path");
			copyFullPathMenuItem.setOnAction(event -> copyFullPath(searchResult));
			contextMenu.getItems().add(copyFullPathMenuItem);

			final MenuItem selectInExplorerMenuItem = new MenuItem("select in explorer");
			selectInExplorerMenuItem.setOnAction(event -> selectInExplorer(searchResult));
			contextMenu.getItems().add(selectInExplorerMenuItem);

			final MenuItem deleteMenuItem = new MenuItem("delete");
			deleteMenuItem.setOnAction(event -> delete());
			contextMenu.getItems().add(deleteMenuItem);

			final MenuItem copyMenuItem = new MenuItem("copy");
			copyMenuItem.setOnAction(event -> copy());
			contextMenu.getItems().add(copyMenuItem);
		}
		return contextMenu;
	}

	private static void copyFullPath(
			final SearchResult searchResult) {

		final String filePathString = searchResult.createFilePathString();
		ClipboardUtils.putStringInClipBoard(filePathString);
	}

	private static void selectInExplorer(
			final SearchResult searchResult) {

		final String filePathString = searchResult.createFilePathString();
		final String appFolderPathString = FileSearcherUtils.createAppFolderPathString();
		IoUtils.selectFileInExplorer(filePathString, appFolderPathString);
	}

	private void delete() {

		try {
			final TableView<SearchResult> tableView = getTableView();
			final List<SearchResult> selectedItemList = tableView.getSelectionModel().getSelectedItems();
			for (final SearchResult selectedItem : selectedItemList) {

				final String filePathString = selectedItem.createFilePathString();
				FactoryFileDeleter.getInstance().deleteFile(filePathString, true, true);
			}

		} catch (final Exception exc) {
			new CustomAlertException("error", "error occurred while deleting files", exc).showAndWait();
		}
	}

	private void copy() {

		try {
			final List<File> fileList = new ArrayList<>();
			final TableView<SearchResult> tableView = getTableView();
			final List<SearchResult> selectedItemList = tableView.getSelectionModel().getSelectedItems();
			for (final SearchResult selectedItem : selectedItemList) {

				final String filePathString = selectedItem.createFilePathString();
				final File file = new File(filePathString);
				fileList.add(file);
			}

			final ClipboardContent clipboardContent = new ClipboardContent();
			clipboardContent.putFiles(fileList);

			final Clipboard clipboard = Clipboard.getSystemClipboard();
			clipboard.setContent(clipboardContent);

		} catch (final Exception exc) {
			new CustomAlertException("error", "error occurred while copying files", exc).showAndWait();
		}
	}
}
