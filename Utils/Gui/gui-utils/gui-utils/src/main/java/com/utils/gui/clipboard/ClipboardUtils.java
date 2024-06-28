package com.utils.gui.clipboard;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;

import com.utils.annotations.ApiMethod;

public final class ClipboardUtils {

	private ClipboardUtils() {
	}

	@ApiMethod
	public static void putStringInClipBoard(
			final String string) {

		try {
			final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			final StringSelection stringSelection = new StringSelection(string);
			clipboard.setContents(stringSelection, stringSelection);

		} catch (final Exception ignored) {
		}
	}

	@ApiMethod
	public static String getStringFromClipboard() {

		String string = null;
		try {
			final Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			final Object clipboardData = systemClipboard.getData(DataFlavor.stringFlavor);
			if (clipboardData != null) {
				string = clipboardData.toString();
			}

		} catch (final Exception ignored) {
		}
		return string;
	}
}
