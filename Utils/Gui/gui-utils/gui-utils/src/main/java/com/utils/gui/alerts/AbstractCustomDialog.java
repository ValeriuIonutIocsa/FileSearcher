package com.utils.gui.alerts;

import java.util.Optional;

import com.utils.gui.AbstractCustomControl;
import com.utils.gui.GuiUtils;

import javafx.scene.control.Dialog;
import javafx.stage.StageStyle;

public abstract class AbstractCustomDialog<
		ObjectT,
		DialogT extends Dialog<ObjectT>>
		extends AbstractCustomControl<DialogT> implements CustomDialog<ObjectT, DialogT> {

	private ObjectT result;

	AbstractCustomDialog() {
	}

	@Override
	protected DialogT createRoot() {

		final DialogT dialog = createDialog();

		dialog.initStyle(StageStyle.UTILITY);
		dialog.setResizable(true);
		final int prefWidth = getPrefWidth();
		final int prefHeight = getPrefHeight();
		dialog.getDialogPane().setPrefSize(prefWidth, prefHeight);

		final String title = getTitle();
		dialog.setTitle(title);
		final String headerText = getHeaderText();
		dialog.setHeaderText(headerText);
		final String contentText = getContentText();
		dialog.setContentText(contentText);

		configureDialog(dialog);

		return dialog;
	}

	protected abstract DialogT createDialog();

	@Override
	public int getPrefWidth() {
		return 640;
	}

	@Override
	public int getPrefHeight() {
		return 320;
	}

	protected abstract String getTitle();

	protected abstract String getHeaderText();

	protected abstract String getContentText();

	/**
	 * @param dialog
	 *            dialog
	 */
	protected void configureDialog(
			final DialogT dialog) {
	}

	@Override
	public void showAndWait() {

		GuiUtils.runAndWait(() -> {

			final Optional<ObjectT> resultOptional = getRoot().showAndWait();
			resultOptional.ifPresent(buttonType -> result = buttonType);
		});
	}

	@Override
	public ObjectT getResult() {
		return result;
	}
}
