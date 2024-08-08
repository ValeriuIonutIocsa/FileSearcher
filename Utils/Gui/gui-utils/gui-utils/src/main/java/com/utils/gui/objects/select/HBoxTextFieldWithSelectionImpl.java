package com.utils.gui.objects.select;

import java.util.List;

import com.utils.data_types.table.TableColumnData;
import com.utils.gui.AbstractCustomControl;
import com.utils.gui.GuiUtils;
import com.utils.gui.data.Dimensions;
import com.utils.gui.factories.BasicControlsFactories;
import com.utils.gui.factories.LayoutControlsFactories;
import com.utils.gui.objects.PopupWindow;
import com.utils.gui.objects.select.data.TextFieldWithSelectionItem;
import com.utils.log.Logger;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;

public class HBoxTextFieldWithSelectionImpl<
		TextFieldWithSelectionItemT extends TextFieldWithSelectionItem>
		extends AbstractCustomControl<HBox> implements HBoxTextFieldWithSelection {

	private final String displayName;
	private final Dimensions popupWindowDimensions;
	private final TableColumnData[] tableColumnDataArray;
	private final List<TextFieldWithSelectionItemT> itemList;
	private final String initialValue;

	private TextField textField;

	public HBoxTextFieldWithSelectionImpl(
			final String displayName,
			final Dimensions popupWindowDimensions,
			final TableColumnData[] tableColumnDataArray,
			final List<TextFieldWithSelectionItemT> itemList,
			final String initialValue) {

		this.displayName = displayName;
		this.popupWindowDimensions = popupWindowDimensions;
		this.tableColumnDataArray = tableColumnDataArray;
		this.itemList = itemList;
		this.initialValue = initialValue;
	}

	@Override
	protected HBox createRoot() {

		final HBox rootHBox = LayoutControlsFactories.getInstance().createHBox();

		textField = BasicControlsFactories.getInstance().createTextField(initialValue);
		GuiUtils.addToHBox(rootHBox, textField,
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 0, 0, 0);

		final Button browseButton = BasicControlsFactories.getInstance().createButton("...");
		browseButton.setOnAction(event -> browse());
		GuiUtils.addToHBox(rootHBox, browseButton,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 7);

		return rootHBox;
	}

	private void browse() {

		final String currentTextFieldValue = textField.getText();

		final VBoxSelectTextFieldValue<TextFieldWithSelectionItemT> vBoxSelectTextFieldValue =
				new VBoxSelectTextFieldValue<>(
						displayName, tableColumnDataArray, itemList, currentTextFieldValue);
		new PopupWindow(getRoot().getScene(), null, Modality.APPLICATION_MODAL, "select " + displayName,
				popupWindowDimensions, vBoxSelectTextFieldValue.getRoot()).showAndWait();

		final String newTextFieldValue = vBoxSelectTextFieldValue.getNewTextFieldValue();
		if (newTextFieldValue != null) {
			textField.setText(newTextFieldValue);
		}
	}

	@Override
	public void configureValue(
			final String value) {

		if (textField == null) {
			Logger.printError("HBoxTextFieldWithSelection component not rendered yet");

		} else {
			textField.setText(value);
		}
	}

	@Override
	public String computeValue() {

		final String value;
		if (textField == null) {
			Logger.printError("HBoxTextFieldWithSelection component not rendered yet");
			value = null;

		} else {
			value = textField.getText();
		}
		return value;
	}

	public List<TextFieldWithSelectionItemT> getItemList() {
		return itemList;
	}
}
