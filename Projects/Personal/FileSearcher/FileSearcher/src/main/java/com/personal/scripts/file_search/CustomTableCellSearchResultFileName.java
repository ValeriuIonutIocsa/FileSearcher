package com.personal.scripts.file_search;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.personal.scripts.file_search.workers.search.SearchResult;
import com.utils.gui.alerts.CustomAlertThrowable;
import com.utils.gui.clipboard.ClipboardUtils;
import com.utils.gui.objects.tables.table_view.CustomTableCell;
import com.utils.io.IoUtils;
import com.utils.io.file_deleters.FactoryFileDeleter;
import com.utils.log.Logger;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

class CustomTableCellSearchResultFileName extends CustomTableCell<SearchResult, Object> {

	private final VBoxFileSearcher vBoxFileSearcher;

	CustomTableCellSearchResultFileName(
			final VBoxFileSearcher vBoxFileSearcher) {

		this.vBoxFileSearcher = vBoxFileSearcher;
	}

	@Override
	public ContextMenu createContextMenu(
			final Object item) {

		ContextMenu contextMenu = null;
		final SearchResult searchResult = getRowData();
		if (searchResult != null) {

			contextMenu = new ContextMenu();

			final MenuItem copyFullPathMenuItem = new MenuItem("copy full path");
			copyFullPathMenuItem.setOnAction(event -> copyFullPath());
			contextMenu.getItems().add(copyFullPathMenuItem);

			final MenuItem selectInExplorerMenuItem = new MenuItem("select in explorer");
			selectInExplorerMenuItem.setOnAction(event -> selectInExplorer(searchResult));
			contextMenu.getItems().add(selectInExplorerMenuItem);

			final MenuItem openMenuItem = new MenuItem("open");
			openMenuItem.setOnAction(event -> open());
			contextMenu.getItems().add(openMenuItem);

			final MenuItem deleteMenuItem = new MenuItem("delete");
			deleteMenuItem.setOnAction(event -> delete());
			contextMenu.getItems().add(deleteMenuItem);

			final MenuItem copyMenuItem = new MenuItem("copy");
			copyMenuItem.setOnAction(event -> copy());
			contextMenu.getItems().add(copyMenuItem);
		}
		return contextMenu;
	}

	private void copyFullPath() {

		final List<String> filePathStringList = new ArrayList<>();
		fillFilePathStringList(filePathStringList);

		final StringBuilder sbToPutInClipboard = new StringBuilder();
		for (final String filePathString : filePathStringList) {

			if (!sbToPutInClipboard.isEmpty()) {
				sbToPutInClipboard.append(System.lineSeparator());
			}
			sbToPutInClipboard.append(filePathString);
		}
		final String toPutInClipboard = sbToPutInClipboard.toString();

		ClipboardUtils.putStringInClipBoard(toPutInClipboard);
	}

	private static void selectInExplorer(
			final SearchResult searchResult) {

		final String filePathString = searchResult.createFilePathString();
		final String appFolderPathString = FileSearcherUtils.createAppFolderPathString();
		IoUtils.selectFileInExplorer(filePathString, appFolderPathString);
	}

	private void open() {

		final List<String> filePathStringList = new ArrayList<>();
		fillFilePathStringList(filePathStringList);

		for (final String filePathString : filePathStringList) {
			vBoxFileSearcher.jumpToFirstOccurrenceL2(filePathString);
		}
	}

	private void delete() {

		try {
			final List<String> filePathStringList = new ArrayList<>();
			fillFilePathStringList(filePathStringList);

			for (final String filePathString : filePathStringList) {
				FactoryFileDeleter.getInstance().deleteFile(filePathString, true, true);
			}

		} catch (final Throwable throwable) {
			Logger.printThrowable(throwable);
			new CustomAlertThrowable("error", "error occurred while deleting files", throwable).showAndWait();
		}
	}

	private void copy() {

		try {
			final List<String> filePathStringList = new ArrayList<>();
			fillFilePathStringList(filePathStringList);

			final List<File> fileList = new ArrayList<>();
			for (final String filePathString : filePathStringList) {

				final File file = new File(filePathString);
				fileList.add(file);
			}

			final ClipboardContent clipboardContent = new ClipboardContent();
			clipboardContent.putFiles(fileList);

			final Clipboard clipboard = Clipboard.getSystemClipboard();
			clipboard.setContent(clipboardContent);

		} catch (final Throwable throwable) {
			Logger.printThrowable(throwable);
			new CustomAlertThrowable("error", "error occurred while copying files", throwable).showAndWait();
		}
	}

	private void fillFilePathStringList(
			final List<String> filePathStringList) {

		final TableView<SearchResult> tableView = getTableView();
		final List<SearchResult> selectedItemList = tableView.getSelectionModel().getSelectedItems();
		for (final SearchResult selectedItem : selectedItemList) {

			final String filePathString = selectedItem.createFilePathString();
			filePathStringList.add(filePathString);
		}
	}
}
