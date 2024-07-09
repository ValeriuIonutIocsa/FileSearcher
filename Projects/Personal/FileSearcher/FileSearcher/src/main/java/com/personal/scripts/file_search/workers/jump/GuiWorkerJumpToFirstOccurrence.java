package com.personal.scripts.file_search.workers.jump;

import java.io.BufferedReader;
import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;

import com.personal.scripts.file_search.text_find.TextFinder;
import com.utils.gui.alerts.CustomAlertError;
import com.utils.gui.alerts.CustomAlertException;
import com.utils.gui.clipboard.ClipboardUtils;
import com.utils.gui.workers.AbstractGuiWorker;
import com.utils.gui.workers.ControlDisablerAll;
import com.utils.io.ReaderUtils;
import com.utils.string.StrUtils;

import javafx.scene.Scene;

public class GuiWorkerJumpToFirstOccurrence extends AbstractGuiWorker {

	private final String nppExePathString;

	private final String filePathString;
	private final Charset charset;
	private final TextFinder textFinder;

	public GuiWorkerJumpToFirstOccurrence(
			final Scene scene,
			final String nppExePathString,
			final String filePathString,
			final Charset charset,
			final TextFinder textFinder) {

		super(scene, new ControlDisablerAll(scene));

		this.nppExePathString = nppExePathString;

		this.filePathString = filePathString;
		this.charset = charset;
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

		int row = 0;
		int col = 0;
		if (textFinder != null) {

			final FilePosition filePosition = findFirstOccurrenceFilePosition();
			row = filePosition.row();
			col = filePosition.col();
		}

		final Process process = new ProcessBuilder()
				.command("cmd", "/c", "start", nppExePathString, filePathString, "-n" + row, "-c" + col)
				.inheritIO()
				.start();
		process.waitFor();
	}

	private FilePosition findFirstOccurrenceFilePosition() {

		int row = 0;
		int col = 0;
		try (BufferedReader bufferedReader = ReaderUtils.openBufferedReader(filePathString, charset)) {

			String line;
			int fileRow = 1;
			while ((line = bufferedReader.readLine()) != null) {

				final int index = textFinder.findIndexInString(line);
				if (index >= 0) {

					row = fileRow;
					col = index + 1;
					break;
				}
				fileRow++;
			}

		} catch (final Exception exc) {
			new CustomAlertException("failed to find first occurrence in file",
					"error occurred while searching for first text occurrence " +
							"in file:" + System.lineSeparator() + filePathString, exc).showAndWait();
		}
		return new FilePosition(row, col);
	}

	private record FilePosition(
			int row,
			int col) {

		@Override
		public String toString() {
			return StrUtils.reflectionToString(this);
		}
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
