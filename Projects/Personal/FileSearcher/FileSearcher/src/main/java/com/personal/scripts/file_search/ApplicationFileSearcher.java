package com.personal.scripts.file_search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.personal.scripts.file_search.app_info.FileSearcherAppInfoUtils;
import com.personal.scripts.file_search.hist.SavedHistoryFile;
import com.personal.scripts.file_search.hist.SavedOptionsFile;
import com.utils.app_info.AppInfo;
import com.utils.cli.CliUtils;
import com.utils.gui.GuiUtils;
import com.utils.gui.stages.StageUtils;
import com.utils.gui.styles.StyleUtils;
import com.utils.log.Logger;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ApplicationFileSearcher extends Application {

	private String searchFolderPathString;
	private String rgExePathString;
	private String nppExePathString;

	@Override
	public void init() {

		final List<String> argList = getParameters().getRaw();
		final String[] argArray = argList.toArray(new String[] {});

		final Map<String, String> cliArgsByNameMap = new HashMap<>();
		CliUtils.fillCliArgsByNameMap(argArray, cliArgsByNameMap);

		searchFolderPathString = cliArgsByNameMap.get("search_folder");
		rgExePathString = cliArgsByNameMap.get("rg_exe_path");
		nppExePathString = cliArgsByNameMap.get("npp_exe_path");

		final String debugString = cliArgsByNameMap.get("debug");
		final boolean debug = Boolean.parseBoolean(debugString);
		if (debug) {
			Logger.setDebugMode(true);
		}

		SavedOptionsFile.INSTANCE.parse();

		SavedHistoryFile.SEARCH_PATH_SAVED_HISTORY_FILE.parse();
		SavedHistoryFile.FILE_PATH_PATTERN_SAVED_HISTORY_FILE.parse();
		SavedHistoryFile.SEARCH_TEXT_SAVED_HISTORY_FILE.parse();
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

		GuiUtils.setAppIcon(primaryStage, ImagesFileSearcher.IMAGE_APP);

		final VBoxFileSearcher vBoxFileSearcher =
				new VBoxFileSearcher(searchFolderPathString, rgExePathString, nppExePathString);

		final Scene primaryScene = new Scene(vBoxFileSearcher.getRoot());
		primaryStage.setScene(primaryScene);

		StyleUtils.configureStyle(primaryScene, "com/personal/scripts/file_search/style.css", "");

		primaryStage.setOnHidden(event -> System.exit(0));

		primaryStage.show();
		primaryStage.getScene().getRoot().requestFocus();
	}
}
