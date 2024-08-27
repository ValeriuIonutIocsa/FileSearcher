package com.personal.scripts.file_search;

import java.nio.charset.StandardCharsets;

import com.personal.scripts.file_search.workers.search.SearchResult;
import com.utils.gui.clipboard.ClipboardUtils;
import com.utils.gui.objects.tables.table_view.CustomTableCell;
import com.utils.io.IoUtils;
import com.utils.io.PathUtils;
import com.utils.io.WriterUtils;
import com.utils.io.file_deleters.FactoryFileDeleter;
import com.utils.log.Logger;
import com.utils.string.StrUtils;

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

		String tmpBatFilePathString = null;
		try {
			final String appFolderPathString = FileSearcherUtils.createAppFolderPathString();
			final String pathDateTimeString = StrUtils.createPathDateTimeString();
			tmpBatFilePathString =
					PathUtils.computePath(appFolderPathString, pathDateTimeString + ".bat");

			final String filePathString = searchResult.createFilePathString();
			WriterUtils.tryStringToFile("start explorer /select,\"" + filePathString + "\"",
					StandardCharsets.UTF_8, tmpBatFilePathString);

			final Process process = new ProcessBuilder()
					.command("cmd", "/c", tmpBatFilePathString)
					.inheritIO()
					.start();
			process.waitFor();

		} catch (final Exception exc) {
			Logger.printError("failed to select file in explorer");
			Logger.printException(exc);

		} finally {
			if (IoUtils.fileExists(tmpBatFilePathString)) {
				FactoryFileDeleter.getInstance().deleteFile(tmpBatFilePathString, false, true);
			}
		}
	}
}
