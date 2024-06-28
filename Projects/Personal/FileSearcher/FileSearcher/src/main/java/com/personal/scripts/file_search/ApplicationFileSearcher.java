package com.personal.scripts.file_search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.personal.scripts.file_search.app_info.FileSearcherAppInfoUtils;
import com.personal.scripts.file_search.hist.SavedHistoryFile;
import com.utils.app_info.AppInfo;
import com.utils.cli.CliUtils;
import com.utils.gui.GuiUtils;
import com.utils.gui.stages.StageUtils;
import com.utils.gui.styles.StyleUtils;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ApplicationFileSearcher extends Application {

	private final List<SelectionItem> searchPathSelectionItemList;
	private final List<SelectionItem> filePathPatternSelectionItemList;
	private final List<SelectionItem> searchTextSelectionItemList;

	private String searchFolderPathString;
	private String nppExePathString;

	public ApplicationFileSearcher() {

		searchPathSelectionItemList = new ArrayList<>();
		filePathPatternSelectionItemList = new ArrayList<>();
		searchTextSelectionItemList = new ArrayList<>();
	}

	@Override
	public void init() {

		final List<String> argList = getParameters().getRaw();
		final String[] argArray = argList.toArray(new String[] {});

		final Map<String, String> cliArgsByNameMap = new HashMap<>();
		CliUtils.fillCliArgsByNameMap(argArray, cliArgsByNameMap);

		searchFolderPathString = cliArgsByNameMap.get("search_folder");
		nppExePathString = cliArgsByNameMap.get("npp_exe_path");

		fillSelectionItemList(SavedHistoryFile.SEARCH_PATH_SAVED_HISTORY_FILE,
				searchPathSelectionItemList);
		fillSelectionItemList(SavedHistoryFile.FILE_PATH_PATTERN_SAVED_HISTORY_FILE,
				filePathPatternSelectionItemList);
		fillSelectionItemList(SavedHistoryFile.SEARCH_TEXT_SAVED_HISTORY_FILE,
				searchTextSelectionItemList);
	}

	private static void fillSelectionItemList(
			final SavedHistoryFile savedHistoryFile,
			final List<SelectionItem> selectionItemList) {

		savedHistoryFile.parse();
		final List<String> savedHistoryEntryList = savedHistoryFile.getSavedHistoryEntryList();
		for (final String savedHistoryEntry : savedHistoryEntryList) {

			final SelectionItem selectionItem = new SelectionItem(savedHistoryEntry);
			selectionItemList.add(selectionItem);
		}
	}

	@Override
	public void start(
			final Stage primaryStage) {

		GuiUtils.setupCustomTooltipBehavior();

		final AppInfo appInfo = FileSearcherAppInfoUtils.createAppInfo();
		final String appTitleAndVersion = appInfo.getAppTitleAndVersion();
		primaryStage.setTitle(appTitleAndVersion);

		primaryStage.setHeight(750);
		primaryStage.setWidth(1_250);
		StageUtils.centerOnScreen(primaryStage);

		final VBoxFileSearcher vBoxFileSearcher = new VBoxFileSearcher(searchFolderPathString, nppExePathString,
				searchPathSelectionItemList, filePathPatternSelectionItemList, searchTextSelectionItemList);

		final Scene primaryScene = new Scene(vBoxFileSearcher.getRoot());
		primaryStage.setScene(primaryScene);

		StyleUtils.configureStyle(primaryScene, "com/personal/scripts/file_search/style.css", "");

		primaryStage.setOnHidden(event -> System.exit(0));

		primaryStage.show();
		primaryStage.getScene().getRoot().requestFocus();
	}
}
