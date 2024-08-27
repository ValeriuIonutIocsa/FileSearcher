package com.personal.scripts.file_search;

import com.personal.scripts.file_search.workers.search.SearchResult;
import com.utils.gui.objects.tables.table_view.CustomTableCell;
import com.utils.log.Logger;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

class CustomTableCellSearchResultFolderName extends CustomTableCell<SearchResult, Object> {

	CustomTableCellSearchResultFolderName() {
	}

	@Override
	public ContextMenu createContextMenu(
			final Object item) {

		ContextMenu contextMenu = null;
		final SearchResult searchResult = getRowData();
		if (searchResult != null) {

			contextMenu = new ContextMenu();

			final MenuItem openInExplorerPathMenuItem = new MenuItem("open in explorer");
			openInExplorerPathMenuItem.setOnAction(event -> openInExplorer(searchResult));
			contextMenu.getItems().add(openInExplorerPathMenuItem);
		}
		return contextMenu;
	}

	private static void openInExplorer(
			final SearchResult searchResult) {

		try {
			final String folderPathString = searchResult.getFolderPathString();
			final Process process = new ProcessBuilder()
					.command("cmd", "/c", "start", "explorer", folderPathString)
					.inheritIO()
					.start();
			process.waitFor();

		} catch (final Exception exc) {
			Logger.printError("failed to open folder in explorer");
			Logger.printException(exc);
		}
	}
}
