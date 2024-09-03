package com.personal.scripts.file_search.workers.jump;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.personal.scripts.file_search.text_find.TextFinder;
import com.personal.scripts.file_search.workers.search.engine.SearchEngine;
import com.personal.scripts.file_search.workers.search.engine.data.FirstOccurrenceData;
import com.utils.gui.alerts.CustomAlertError;
import com.utils.gui.clipboard.ClipboardUtils;
import com.utils.gui.workers.AbstractGuiWorker;
import com.utils.gui.workers.ControlDisablerAll;
import com.utils.log.Logger;

import javafx.scene.Scene;

public class GuiWorkerJumpToFirstOccurrence extends AbstractGuiWorker {

	private final String nppExePathString;

	private final String filePathString;
	private final SearchEngine searchEngine;
	private final TextFinder textFinder;

	public GuiWorkerJumpToFirstOccurrence(
			final Scene scene,
			final String nppExePathString,
			final String filePathString,
			final SearchEngine searchEngine,
			final TextFinder textFinder) {

		super(scene, new ControlDisablerAll(scene));

		this.nppExePathString = nppExePathString;

		this.filePathString = filePathString;
		this.searchEngine = searchEngine;
		this.textFinder = textFinder;
	}

	@Override
	protected void work() throws Exception {

		Logger.printNewLine();
		Logger.printProgress("jumping to first occurrence in file:");
		Logger.printLine(filePathString);

		if (textFinder != null) {

			final String stringToPutInClipboard = textFinder.createStringToPutInClipboard();
			if (StringUtils.isNotBlank(stringToPutInClipboard)) {
				ClipboardUtils.putStringInClipBoard(stringToPutInClipboard);
			}
		}

		final FirstOccurrenceData firstOccurrenceData;
		if (textFinder != null) {
			firstOccurrenceData = searchEngine.parseFirstOccurrenceData(filePathString, textFinder);
		} else {
			firstOccurrenceData = new FirstOccurrenceData(0, 0);
		}
		final int firstOccurrenceRow = firstOccurrenceData.firstOccurrenceRow();
		final int firstOccurrenceCol = firstOccurrenceData.firstOccurrenceCol();

		final List<String> commandPartList = new ArrayList<>();
		Collections.addAll(commandPartList, "cmd", "/c", "start", nppExePathString, filePathString,
				"-n" + firstOccurrenceRow, "-c" + firstOccurrenceCol);

		Logger.printProgress("executing command:");
		Logger.printLine(StringUtils.join(commandPartList, ' '));

		final Process process = new ProcessBuilder()
				.command(commandPartList)
				.inheritIO()
				.start();
		process.waitFor();
	}

	@Override
	protected void error() {

		new CustomAlertError("failed to jump to first occurrence", "an error occurred " +
				"when trying to open Notepad++ to the first occurrence of the text " +
				"in file:" + System.lineSeparator() + filePathString).showAndWait();
	}

	@Override
	protected void finish() {
	}
}
