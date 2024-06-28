package com.utils.gui.factories;

import java.util.List;
import java.util.Locale;

import com.utils.gui.GuiUtils;

import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

class ComboBoxKeyEventHandler<
		ObjectT> implements EventHandler<KeyEvent> {

	private final ComboBox<ObjectT> comboBox;

	private String typedString;

	ComboBoxKeyEventHandler(
			final ComboBox<ObjectT> comboBox) {

		this.comboBox = comboBox;

		typedString = "";
	}

	@Override
	public void handle(
			final KeyEvent event) {

		final KeyCode keyCode = event.getCode();

		if (keyCode == KeyCode.DELETE) {
			clearText();

		} else if (keyCode == KeyCode.BACK_SPACE) {

			if (!typedString.isEmpty()) {
				typedString = typedString.substring(0, typedString.length() - 1);
			}

		} else if (keyCode != KeyCode.TAB) {
			typedString += event.getText();
		}

		if (typedString.isEmpty()) {
			comboBox.getSelectionModel().selectFirst();

		} else {
			final List<ObjectT> itemList = comboBox.getItems();
			for (final ObjectT item : itemList) {

				final String displayString = item.toString().toLowerCase(Locale.US);
				if (displayString.startsWith(typedString)) {

					comboBox.getSelectionModel().select(item);

					final ListView<?> listView = GuiUtils.getComboBoxListView(comboBox);
					if (listView != null) {

						final List<?> listViewItemList = listView.getItems();
						final int itemIndex = listViewItemList.indexOf(item);
						listView.getFocusModel().focus(itemIndex);
						listView.scrollTo(itemIndex);
					}

					break;
				}
			}
		}
	}

	void clearText() {
		typedString = "";
	}
}
