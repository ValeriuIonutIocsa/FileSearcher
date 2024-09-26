package com.personal.scripts.file_search;

import com.personal.scripts.file_search.workers.search.SearchResult;
import com.utils.gui.clipboard.ClipboardUtils;
import com.utils.gui.objects.tables.table_view.CustomTableCell;
import com.utils.io.IoUtils;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

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
}
