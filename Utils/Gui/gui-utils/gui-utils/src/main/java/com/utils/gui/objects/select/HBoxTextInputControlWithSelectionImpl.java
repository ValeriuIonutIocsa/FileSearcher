package com.utils.gui.objects.select;

import java.util.List;

import com.utils.data_types.table.TableColumnData;
import com.utils.gui.AbstractCustomControl;
import com.utils.gui.GuiUtils;
import com.utils.gui.data.Dimensions;
import com.utils.gui.factories.BasicControlsFactories;
import com.utils.gui.factories.LayoutControlsFactories;
import com.utils.gui.objects.PopupWindow;
import com.utils.gui.objects.select.data.TextInputControlWithSelectionItem;
import com.utils.log.Logger;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;

public class HBoxTextInputControlWithSelectionImpl<
		TextInputControlWithSelectionItemT extends TextInputControlWithSelectionItem>
		extends AbstractCustomControl<HBox> implements HBoxTextInputControlWithSelection {

	private final String displayName;
	private final Dimensions popupWindowDimensions;
	private final TableColumnData[] tableColumnDataArray;
	private final List<TextInputControlWithSelectionItemT> itemList;
	private final String initialValue;
	private final boolean multiline;
	private final Runnable onEnterRunnable;

	private TextInputControl textInputControl;

	public HBoxTextInputControlWithSelectionImpl(
			final String displayName,
			final Dimensions popupWindowDimensions,
			final TableColumnData[] tableColumnDataArray,
			final List<TextInputControlWithSelectionItemT> itemList,
			final String initialValue,
			final boolean multiline,
			final Runnable onEnterRunnable) {

		this.displayName = displayName;
		this.popupWindowDimensions = popupWindowDimensions;
		this.tableColumnDataArray = tableColumnDataArray;
		this.itemList = itemList;
		this.initialValue = initialValue;
		this.multiline = multiline;
		this.onEnterRunnable = onEnterRunnable;
	}

	@Override
	protected HBox createRoot() {

		final HBox rootHBox = LayoutControlsFactories.getInstance().createHBox();

		if (multiline) {

			textInputControl = BasicControlsFactories.getInstance().createTextArea(initialValue);
			textInputControl.setPrefHeight(45);
			textInputControl.addEventFilter(KeyEvent.KEY_PRESSED, event -> {

				if (event.getCode() == KeyCode.ENTER) {

					if (event.isShiftDown()) {

						final int caretPos = textInputControl.getCaretPosition();
						textInputControl.insertText(caretPos, "\n");

					} else {
						if (onEnterRunnable != null) {
							onEnterRunnable.run();
						}
					}
					event.consume();
				}
			});

		} else {
			textInputControl = BasicControlsFactories.getInstance().createTextField(initialValue);
			textInputControl.addEventFilter(KeyEvent.KEY_PRESSED, event -> {

				if (event.getCode() == KeyCode.ENTER) {

					if (onEnterRunnable != null) {
						onEnterRunnable.run();
					}
					event.consume();
				}
			});
		}
		GuiUtils.addToHBox(rootHBox, textInputControl,
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 0, 0, 0);

		final Button browseButton = BasicControlsFactories.getInstance().createButton("...");
		browseButton.setOnAction(event -> browse());
		GuiUtils.addToHBox(rootHBox, browseButton,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 7);

		return rootHBox;
	}

	private void browse() {

		final String currentTextInputControlValue = textInputControl.getText();

		final VBoxSelectTextInputControlValue<TextInputControlWithSelectionItemT> vBoxSelectTextInputControlValue =
				new VBoxSelectTextInputControlValue<>(
						displayName, tableColumnDataArray, itemList, currentTextInputControlValue);
		new PopupWindow(getRoot().getScene(), null, Modality.APPLICATION_MODAL, "select " + displayName,
				popupWindowDimensions, vBoxSelectTextInputControlValue.getRoot()).showAndWait();

		final String newTextInputControlValue = vBoxSelectTextInputControlValue.getNewTextInputControlValue();
		if (newTextInputControlValue != null) {
			textInputControl.setText(newTextInputControlValue);
		}
	}

	@Override
	public void configureValue(
			final String value) {

		if (textInputControl == null) {
			Logger.printError("HBoxTextInputControlWithSelection component not rendered yet");

		} else {
			textInputControl.setText(value);
		}
	}

	@Override
	public String computeValue() {

		final String value;
		if (textInputControl == null) {
			Logger.printError("HBoxTextInputControlWithSelection component not rendered yet");
			value = null;

		} else {
			value = textInputControl.getText();
		}
		return value;
	}

	public List<TextInputControlWithSelectionItemT> getItemList() {
		return itemList;
	}
}
