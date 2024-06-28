package com.utils.gui.alerts;

import com.utils.gui.GuiUtils;
import com.utils.gui.factories.BasicControlsFactories;
import com.utils.gui.factories.LayoutControlsFactories;
import com.utils.log.Logger;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class CustomAlertException extends AbstractCustomAlert {

	private final String headerText;
	private final String contentText;
	private final Exception exc;

	public CustomAlertException(
			final String headerText,
			final String contentText,
			final Exception exc) {

		this.headerText = headerText;
		this.contentText = contentText;
		this.exc = exc;
	}

	@Override
	Alert.AlertType getAlertType() {
		return Alert.AlertType.ERROR;
	}

	@Override
	protected String getTitle() {
		return "Exception";
	}

	@Override
	protected String getHeaderText() {
		return headerText;
	}

	@Override
	protected String getContentText() {
		return contentText;
	}

	@Override
	protected void configureDialog(
			final Alert dialog) {

		super.configureDialog(dialog);

		final VBox vBoxExpandableContent = LayoutControlsFactories.getInstance().createVBox();

		final Label labelDetails = BasicControlsFactories.getInstance().createLabel("Details:");
		GuiUtils.addToVBox(vBoxExpandableContent, labelDetails,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 5, 3);

		final String exceptionString = Logger.exceptionToString(exc);
		final TextArea textAreaExceptionString = BasicControlsFactories.getInstance().createTextArea(exceptionString);
		textAreaExceptionString.setEditable(false);
		textAreaExceptionString.setWrapText(true);
		GuiUtils.addToVBox(vBoxExpandableContent, textAreaExceptionString,
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 0, 0, 0);

		dialog.getDialogPane().setExpandableContent(vBoxExpandableContent);
		dialog.getDialogPane().setExpanded(true);
	}
}
