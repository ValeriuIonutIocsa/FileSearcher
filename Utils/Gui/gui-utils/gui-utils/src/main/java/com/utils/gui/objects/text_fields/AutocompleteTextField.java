package com.utils.gui.objects.text_fields;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class AutocompleteTextField extends TextField {

	private final SortedSet<String> entries;
	private final ContextMenu entriesPopup;

	private boolean listening;

	public AutocompleteTextField() {

		super();

		entries = new TreeSet<>();
		entriesPopup = new ContextMenu();

		setListener();
	}

	private void setListener() {

		textProperty().addListener((
				observable,
				oldValue,
				newValue) -> {

			if (listening) {

				final List<String> filteredEntryList = new ArrayList<>();
				final String enteredText = getText();
				if (StringUtils.isEmpty(enteredText)) {
					filteredEntryList.addAll(entries);

				} else {
					for (final String entry : entries) {

						if (StringUtils.containsIgnoreCase(entry, enteredText)) {
							filteredEntryList.add(entry);
						}
					}
				}

				if (filteredEntryList.isEmpty()) {
					entriesPopup.hide();

				} else {
					populatePopup(filteredEntryList, enteredText);
					final Scene scene = getScene();
					if (scene != null) {

						if (!entriesPopup.isShowing()) {

							entriesPopup.show(this, Side.BOTTOM, 0, 0);
							entriesPopup.hide();
							entriesPopup.show(this, Side.BOTTOM, 0, 0);
						}
					}
				}
			}
		});

		focusedProperty().addListener((
				observableValue,
				oldValue,
				newValue) -> {

			if (newValue) {
				selectAll();
			}
			entriesPopup.hide();
		});
	}

	private void populatePopup(
			final List<String> filteredEntryList,
			final String searchRequest) {

		final List<CustomMenuItem> menuItems = new LinkedList<>();
		for (final String filteredEntry : filteredEntryList) {

			final Label entryLabel = new Label();
			final TextFlow textFlow = buildTextFlow(filteredEntry, searchRequest);
			entryLabel.setGraphic(textFlow);
			entryLabel.setPrefHeight(10);
			final CustomMenuItem item = new CustomMenuItem(entryLabel, true);
			menuItems.add(item);

			item.setOnAction(actionEvent -> {

				setText(filteredEntry);
				positionCaret(filteredEntry.length());
				entriesPopup.hide();
			});
		}

		entriesPopup.getItems().clear();
		entriesPopup.getItems().addAll(menuItems);
	}

	private static TextFlow buildTextFlow(
			final String text,
			final String filter) {

		final int filterIndex = text.toLowerCase().indexOf(filter.toLowerCase());
		final Text textBefore = new Text(text.substring(0, filterIndex));
		final Text textAfter = new Text(text.substring(filterIndex + filter.length()));
		final Text textFilter = new Text(text.substring(filterIndex, filterIndex + filter.length()));
		textFilter.setFill(Color.ORANGE);
		textFilter.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
		return new TextFlow(textBefore, textFilter, textAfter);
	}

	public SortedSet<String> getEntries() {
		return entries;
	}

	public void setListening(
			final boolean listening) {
		this.listening = listening;
	}
}
