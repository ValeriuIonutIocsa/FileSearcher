package com.utils.gui.objects.select;

import java.util.List;

import com.utils.data_types.table.TableColumnData;
import com.utils.gui.AbstractCustomControl;
import com.utils.gui.GuiUtils;
import com.utils.gui.alerts.CustomAlertWarning;
import com.utils.gui.factories.BasicControlsFactories;
import com.utils.gui.factories.LayoutControlsFactories;
import com.utils.gui.objects.HBoxWindowButtons;
import com.utils.gui.objects.select.data.TextInputControlWithSelectionItem;
import com.utils.gui.objects.tables.table_view.CustomTableView;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

class VBoxSelectTextInputControlValue<
		TextInputControlWithSelectionItemT extends TextInputControlWithSelectionItem>
		extends AbstractCustomControl<VBox> {

	private final String displayName;
	private final TableColumnData[] tableColumnDataArray;
	private final List<TextInputControlWithSelectionItemT> itemList;
	private final String currentTextInputControlValue;

	private CustomTableView<TextInputControlWithSelectionItemT> customTableView;
	private String newTextInputControlValue;

	VBoxSelectTextInputControlValue(
			final String displayName,
			final TableColumnData[] tableColumnDataArray,
			final List<TextInputControlWithSelectionItemT> itemList,
			final String currentTextInputControlValue) {

		this.displayName = displayName;
		this.tableColumnDataArray = tableColumnDataArray;
		this.itemList = itemList;
		this.currentTextInputControlValue = currentTextInputControlValue;
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

	private CustomTableView<TextInputControlWithSelectionItemT> createCustomTableView() {

		final CustomTableView<TextInputControlWithSelectionItemT> customTableView =
				new CustomTableView<>(tableColumnDataArray, false, true, true, true, true, 0);

		customTableView.setItems(itemList);

		TextInputControlWithSelectionItemT selectedItem = null;
		for (final TextInputControlWithSelectionItemT item : itemList) {

			final boolean matchesTextInputControlValue =
					item.checkMatchesTextInputControlValue(currentTextInputControlValue);
			if (matchesTextInputControlValue) {

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

		final TextInputControlWithSelectionItemT selectedItem =
				customTableView.getSelectionModel().getSelectedItem();
		if (selectedItem == null) {
			new CustomAlertWarning("no item selected",
					"please select an item in the table first").showAndWait();

		} else {
			newTextInputControlValue = selectedItem.createTextInputControlValue();
			getRoot().getScene().getWindow().hide();
		}
	}

	String getNewTextInputControlValue() {
		return newTextInputControlValue;
	}
}
