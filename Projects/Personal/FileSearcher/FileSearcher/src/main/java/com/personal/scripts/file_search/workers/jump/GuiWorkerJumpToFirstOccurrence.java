package com.personal.scripts.file_search.workers.jump;

import org.apache.commons.lang3.StringUtils;

import com.personal.scripts.file_search.text_find.TextFinder;
import com.utils.gui.alerts.CustomAlertError;
import com.utils.gui.clipboard.ClipboardUtils;
import com.utils.gui.workers.AbstractGuiWorker;
import com.utils.gui.workers.ControlDisablerAll;

import javafx.scene.Scene;

public class GuiWorkerJumpToFirstOccurrence extends AbstractGuiWorker {

	private final String nppExePathString;

	private final String filePathString;
	private final int firstOccurrenceRow;
	private final int firstOccurrenceCol;
	private final TextFinder textFinder;

	public GuiWorkerJumpToFirstOccurrence(
			final Scene scene,
			final String nppExePathString,
			final String filePathString,
			final int firstOccurrenceRow,
			final int firstOccurrenceCol,
			final TextFinder textFinder) {

		super(scene, new ControlDisablerAll(scene));

		this.nppExePathString = nppExePathString;

		this.filePathString = filePathString;
		this.firstOccurrenceRow = firstOccurrenceRow;
		this.firstOccurrenceCol = firstOccurrenceCol;
		this.textFinder = textFinder;
	}

	@Override
	protected void work() throws Exception {

		if (textFinder != null) {

			final String stringToPutInClipboard = textFinder.createStringToPutInClipboard();
			if (StringUtils.isNotBlank(stringToPutInClipboard)) {
				ClipboardUtils.putStringInClipBoard(stringToPutInClipboard);
			}
		}

		final Process process = new ProcessBuilder()
				.command("cmd", "/c", "start", nppExePathString, filePathString,
						"-n" + firstOccurrenceRow, "-c" + firstOccurrenceCol)
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
