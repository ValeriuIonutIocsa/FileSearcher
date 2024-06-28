package com.utils.gui.objects.select;

import java.util.List;

import com.utils.data_types.table.TableColumnData;
import com.utils.gui.AbstractCustomControl;
import com.utils.gui.GuiUtils;
import com.utils.gui.alerts.CustomAlertWarning;
import com.utils.gui.factories.BasicControlsFactories;
import com.utils.gui.factories.LayoutControlsFactories;
import com.utils.gui.objects.HBoxWindowButtons;
import com.utils.gui.objects.select.data.TextFieldWithSelectionItem;
import com.utils.gui.objects.tables.table_view.CustomTableView;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

class VBoxSelectTextFieldValue<
		TextFieldWithSelectionItemT extends TextFieldWithSelectionItem>
		extends AbstractCustomControl<VBox> {

	private final String displayName;
	private final TableColumnData[] tableColumnDataArray;
	private final List<TextFieldWithSelectionItemT> itemList;
	private final String currentTextFieldValue;

	private CustomTableView<TextFieldWithSelectionItemT> customTableView;
	private String newTextFieldValue;

	VBoxSelectTextFieldValue(
			final String displayName,
			final TableColumnData[] tableColumnDataArray,
			final List<TextFieldWithSelectionItemT> itemList,
			final String currentTextFieldValue) {

		this.displayName = displayName;
		this.tableColumnDataArray = tableColumnDataArray;
		this.itemList = itemList;
		this.currentTextFieldValue = currentTextFieldValue;
	}

	@Override
	protected VBox createRoot() {

		final VBox rootVBox = LayoutControlsFactories.getInstance().createVBox();

		final Label label = BasicControlsFactories.getInstance()
				.createLabel("select " + displayName + ":");
		GuiUtils.addToVBox(rootVBox, label,
				Pos.CENTER_LEFT, Priority.NEVER, 7, 7, 0, 7);

		customTableView = createCustomTableView();
		GuiUtils.addToVBox(rootVBox, customTableView,
				Pos.CENTER_LEFT, Priority.ALWAYS, 7, 7, 0, 7);

		final HBoxWindowButtons hBoxButtons = new HBoxWindowButtons("OK", this::okButtonClicked);
		GuiUtils.addToVBox(rootVBox, hBoxButtons.getRoot(),
				Pos.CENTER, Priority.ALWAYS, 7, 7, 7, 7);

		rootVBox.setOnKeyPressed(keyEvent -> {

			if (keyEvent.getCode() == KeyCode.ENTER) {
				okButtonClicked();
			}
		});

		return rootVBox;
	}

	private CustomTableView<TextFieldWithSelectionItemT> createCustomTableView() {

		final CustomTableView<TextFieldWithSelectionItemT> customTableView =
				new CustomTableView<>(tableColumnDataArray, false, true, true, true, true, 0);

		customTableView.setItems(itemList);

		TextFieldWithSelectionItemT selectedItem = null;
		for (final TextFieldWithSelectionItemT item : itemList) {

			final boolean matchesTextFieldValue =
					item.checkMatchesTextFieldValue(currentTextFieldValue);
			if (matchesTextFieldValue) {

				selectedItem = item;
				break;
			}
		}
		if (selectedItem != null) {
			customTableView.getSelectionModel().select(selectedItem);
		}

		customTableView.setOnMouseClicked(mouseEvent -> {

			if (GuiUtils.isDoubleClick(mouseEvent)) {
				okButtonClicked();
			}
		});

		return customTableView;
	}

	private void okButtonClicked() {

		final TextFieldWithSelectionItemT selectedItem =
				customTableView.getSelectionModel().getSelectedItem();
		if (selectedItem == null) {
			new CustomAlertWarning("no item selected",
					"please select an item in the table first").showAndWait();

		} else {
			newTextFieldValue = selectedItem.createTextFieldValue();
			getRoot().getScene().getWindow().hide();
		}
	}

	String getNewTextFieldValue() {
		return newTextFieldValue;
	}
}
