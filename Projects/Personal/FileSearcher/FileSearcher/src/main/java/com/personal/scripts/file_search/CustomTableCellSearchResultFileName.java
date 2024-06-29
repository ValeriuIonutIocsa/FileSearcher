package com.personal.scripts.file_search;

import com.personal.scripts.file_search.workers.search.SearchResult;
import com.utils.gui.clipboard.ClipboardUtils;
import com.utils.gui.objects.tables.table_view.AbstractCustomTableCell;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

class CustomTableCellSearchResultFileName extends AbstractCustomTableCell<SearchResult, Object> {

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
		}
		return contextMenu;
	}

	private static void copyFullPath(
			final SearchResult searchResult) {

		final String filePathString = searchResult.createFilePathString();
		ClipboardUtils.putStringInClipBoard(filePathString);
	}

	@Override
	protected Class<SearchResult> getRowDataClass() {
		return SearchResult.class;
	}
}
