package com.personal.scripts.file_search;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.personal.scripts.file_search.text_find.TextFinder;
import com.personal.scripts.file_search.workers.jump.GuiWorkerJumpToFirstOccurrence;
import com.personal.scripts.file_search.workers.search.GuiWorkerSearch;
import com.personal.scripts.file_search.workers.search.SearchResult;
import com.utils.data_types.table.TableColumnData;
import com.utils.gui.AbstractCustomControl;
import com.utils.gui.GuiUtils;
import com.utils.gui.data.Dimensions;
import com.utils.gui.factories.BasicControlsFactories;
import com.utils.gui.factories.LayoutControlsFactories;
import com.utils.gui.objects.select.HBoxTextFieldWithSelectionImpl;
import com.utils.gui.objects.tables.table_view.CustomTableView;
import com.utils.string.StrUtils;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class VBoxFileSearcher extends AbstractCustomControl<VBox> {

	private final String searchFolderPathString;
	private final String nppExePathString;

	private final List<SelectionItem> searchPathSelectionItemList;
	private final List<SelectionItem> filePathPatternSelectionItemList;
	private final List<SelectionItem> searchTextSelectionItemList;

	private HBoxTextFieldWithSelectionImpl<SelectionItem> searchPathHBoxTextFieldWithSelection;
	private HBoxTextFieldWithSelectionImpl<SelectionItem> filePathPatternHBoxTextFieldWithSelection;
	private HBoxTextFieldWithSelectionImpl<SelectionItem> searchTextHBoxTextFieldWithSelection;
	private CheckBox useRegexCheckBox;
	private CheckBox caseSensitiveCheckBox;
	private CheckBox saveHistoryCheckBox;

	private CustomTableView<SearchResult> customTableView;

	private Label fileCountLabel;
	private TextField fileCountTextField;
	private Label fileContainingTextCountLabel;
	private TextField fileContainingTextCountTextField;

	private TextArea detailsTextArea;

	private TextFinder textFinder;

	VBoxFileSearcher(
			final String searchFolderPathString,
			final String nppExePathString,
			final List<SelectionItem> searchPathSelectionItemList,
			final List<SelectionItem> filePathPatternSelectionItemList,
			final List<SelectionItem> searchTextSelectionItemList) {

		this.searchFolderPathString = searchFolderPathString;
		this.nppExePathString = nppExePathString;

		this.searchPathSelectionItemList = searchPathSelectionItemList;
		this.filePathPatternSelectionItemList = filePathPatternSelectionItemList;
		this.searchTextSelectionItemList = searchTextSelectionItemList;
	}

	@Override
	protected VBox createRoot() {

		final VBox rootVBox = LayoutControlsFactories.getInstance().createVBox();

		final HBox topHBox = createTopHBox();
		GuiUtils.addToVBox(rootVBox, topHBox,
				Pos.CENTER_LEFT, Priority.NEVER, 7, 0, 0, 0);

		final HBox middleHBox = createMiddleHBox();
		GuiUtils.addToVBox(rootVBox, middleHBox,
				Pos.CENTER_LEFT, Priority.NEVER, 7, 0, 0, 0);

		final HBox bottomHBox = createBottomHBox();
		GuiUtils.addToVBox(rootVBox, bottomHBox,
				Pos.CENTER_LEFT, Priority.NEVER, 7, 0, 0, 0);

		customTableView = createCustomTableView();
		GuiUtils.addToVBox(rootVBox, customTableView,
				Pos.CENTER_LEFT, Priority.ALWAYS, 7, 0, 0, 0);

		final HBox countsHBox = createCountsHBox();
		GuiUtils.addToVBox(rootVBox, countsHBox,
				Pos.CENTER_LEFT, Priority.NEVER, 7, 0, 0, 0);

		detailsTextArea = createDetailsTextArea();
		GuiUtils.addToVBox(rootVBox, detailsTextArea,
				Pos.CENTER_LEFT, Priority.NEVER, 7, 7, 7, 7);

		customTableView.getSelectionModel().selectedItemProperty().addListener((
				observable,
				oldValue,
				newValue) -> {

			if (newValue != null) {

				final String detailsString = newValue.createDetailsString();
				detailsTextArea.setText(detailsString);
			}
		});

		rootVBox.setOnKeyPressed(keyEvent -> {

			if (keyEvent.getCode() == KeyCode.ENTER) {
				search();
			}
		});

		return rootVBox;
	}

	private HBox createCountsHBox() {

		final HBox countsHBox = LayoutControlsFactories.getInstance().createHBox();

		fileCountLabel = BasicControlsFactories.getInstance()
				.createLabel("file count:", "bold");
		GuiUtils.addToHBox(countsHBox, fileCountLabel,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 7);

		fileCountTextField = BasicControlsFactories.getInstance()
				.createReadOnlyTextField("");
		GuiUtils.addToHBox(countsHBox, fileCountTextField,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 7);

		fileContainingTextCountLabel = BasicControlsFactories.getInstance()
				.createLabel("file containing text count:", "bold");
		GuiUtils.addToHBox(countsHBox, fileContainingTextCountLabel,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 7);

		fileContainingTextCountTextField = BasicControlsFactories.getInstance()
				.createReadOnlyTextField("");
		GuiUtils.addToHBox(countsHBox, fileContainingTextCountTextField,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 7);

		makeCountsControlsInvisible();

		return countsHBox;
	}

	private static TextArea createDetailsTextArea() {

		final TextArea detailsTextArea =
				BasicControlsFactories.getInstance().createReadOnlyTextArea("");
		detailsTextArea.setWrapText(true);
		detailsTextArea.setPrefHeight(100);
		return detailsTextArea;
	}

	private CustomTableView<SearchResult> createCustomTableView() {

		final CustomTableView<SearchResult> customTableView =
				new CustomTableView<>(SearchResult.TABLE_COLUMN_DATA_ARRAY,
						false, true, true, true, true, 0);

		customTableView.getColumnByName(SearchResult.FILE_NAME_COLUMN_NAME)
				.setCellFactory(param -> new CustomTableCellSearchResultFileName());

		customTableView.setOnMouseClicked(mouseEvent -> {

			if (GuiUtils.isDoubleClick(mouseEvent)) {
				jumpToFirstOccurrence(customTableView);
			}
		});
		return customTableView;
	}

	private void jumpToFirstOccurrence(
			final CustomTableView<SearchResult> customTableView) {

		final SearchResult searchResult =
				customTableView.getSelectionModel().getSelectedItem();
		if (searchResult != null) {

			final String filePathString = searchResult.createFilePathString();
			new GuiWorkerJumpToFirstOccurrence(getRoot().getScene(),
					nppExePathString, filePathString, textFinder).start();
		}
	}

	private HBox createTopHBox() {

		final HBox topHBox = LayoutControlsFactories.getInstance().createHBox();

		final Label searchPathLabel =
				BasicControlsFactories.getInstance().createLabel("search path:", "bold");
		GuiUtils.addToHBox(topHBox, searchPathLabel,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 7);

		String searchPathInitialValue = "";
		if (StringUtils.isNotBlank(searchFolderPathString)) {
			searchPathInitialValue = searchFolderPathString;

		} else {
			if (!searchPathSelectionItemList.isEmpty()) {

				final SelectionItem firstSelectionItem = searchPathSelectionItemList.getFirst();
				searchPathInitialValue = firstSelectionItem.createTextFieldValue();
			}
		}
		searchPathHBoxTextFieldWithSelection = new HBoxTextFieldWithSelectionImpl<>("search path",
				new Dimensions(600, 600, 1920, 1280, 1200, 700), new TableColumnData[] {
						new TableColumnData("Search Path", "SearchPath", 1.0)
				}, searchPathSelectionItemList, searchPathInitialValue);
		GuiUtils.addToHBox(topHBox, searchPathHBoxTextFieldWithSelection.getRoot(),
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 7, 0, 7);

		return topHBox;
	}

	private HBox createMiddleHBox() {

		final HBox middleHBox = LayoutControlsFactories.getInstance().createHBox();

		final Label filePathPatternLabel =
				BasicControlsFactories.getInstance().createLabel("file path pattern:", "bold");
		GuiUtils.addToHBox(middleHBox, filePathPatternLabel,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 7);

		String filePathPatternInitialValue = "";
		if (!searchTextSelectionItemList.isEmpty()) {

			final SelectionItem firstSelectionItem = filePathPatternSelectionItemList.getFirst();
			filePathPatternInitialValue = firstSelectionItem.createTextFieldValue();
		}
		filePathPatternHBoxTextFieldWithSelection = new HBoxTextFieldWithSelectionImpl<>("file path pattern",
				new Dimensions(600, 600, 1920, 1280, 1200, 700), new TableColumnData[] {
						new TableColumnData("File Path Pattern", "FilePathPattern", 1.0)
				}, filePathPatternSelectionItemList, filePathPatternInitialValue);
		GuiUtils.addToHBox(middleHBox, filePathPatternHBoxTextFieldWithSelection.getRoot(),
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 7, 0, 7);

		final Label searchTextLabel =
				BasicControlsFactories.getInstance().createLabel("search text:", "bold");
		GuiUtils.addToHBox(middleHBox, searchTextLabel,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 7);

		String searchTextInitialValue = "";
		if (!searchTextSelectionItemList.isEmpty()) {

			final SelectionItem firstSelectionItem = searchTextSelectionItemList.getFirst();
			searchTextInitialValue = firstSelectionItem.createTextFieldValue();
		}
		searchTextHBoxTextFieldWithSelection = new HBoxTextFieldWithSelectionImpl<>("search text",
				new Dimensions(600, 600, 1920, 1280, 1200, 700), new TableColumnData[] {
						new TableColumnData("Search Text", "SearchText", 1.0)
				}, searchTextSelectionItemList, searchTextInitialValue);
		GuiUtils.addToHBox(middleHBox, searchTextHBoxTextFieldWithSelection.getRoot(),
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 7, 0, 7);

		return middleHBox;
	}

	private HBox createBottomHBox() {

		final HBox bottomHBox = LayoutControlsFactories.getInstance().createHBox();

		final Label useRegexLabel = BasicControlsFactories.getInstance()
				.createLabel("use regex:", "bold");
		GuiUtils.addToHBox(bottomHBox, useRegexLabel,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 7);

		useRegexCheckBox =
				BasicControlsFactories.getInstance().createCheckBox("");
		GuiUtils.addToHBox(bottomHBox, useRegexCheckBox,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 7);

		final Label caseSensitiveLabel = BasicControlsFactories.getInstance()
				.createLabel("case sensitive:", "bold");
		GuiUtils.addToHBox(bottomHBox, caseSensitiveLabel,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 7);

		caseSensitiveCheckBox =
				BasicControlsFactories.getInstance().createCheckBox("");
		caseSensitiveCheckBox.setSelected(true);
		GuiUtils.addToHBox(bottomHBox, caseSensitiveCheckBox,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 7);

		final Label saveHistoryLabel = BasicControlsFactories.getInstance()
				.createLabel("save history:", "bold");
		GuiUtils.addToHBox(bottomHBox, saveHistoryLabel,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 7);

		saveHistoryCheckBox =
				BasicControlsFactories.getInstance().createCheckBox("");
		saveHistoryCheckBox.setSelected(true);
		GuiUtils.addToHBox(bottomHBox, saveHistoryCheckBox,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 7);

		GuiUtils.addToHBox(bottomHBox, new Region(),
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 0, 0, 0);

		final Button searchButton =
				BasicControlsFactories.getInstance().createButton("Search");
		searchButton.setOnAction(event -> search());
		GuiUtils.addToHBox(bottomHBox, searchButton,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 7, 0, 7);

		return bottomHBox;
	}

	private void search() {

		final String searchFolderPathString =
				searchPathHBoxTextFieldWithSelection.computeValue();
		final String filePathPatternString =
				filePathPatternHBoxTextFieldWithSelection.computeValue();
		final String searchText = searchTextHBoxTextFieldWithSelection.computeValue();
		final boolean useRegex = useRegexCheckBox.isSelected();
		final boolean caseSensitive = caseSensitiveCheckBox.isSelected();
		final boolean saveHistory = saveHistoryCheckBox.isSelected();

		makeCountsControlsInvisible();

		new GuiWorkerSearch(getRoot().getScene(),
				searchFolderPathString, filePathPatternString, searchText,
				useRegex, caseSensitive, saveHistory, this).start();
	}

	private void makeCountsControlsInvisible() {

		fileCountLabel.setVisible(false);
		fileCountTextField.setVisible(false);
		fileContainingTextCountLabel.setVisible(false);
		fileContainingTextCountTextField.setVisible(false);
	}

	public void updateSearchResults(
			final List<SearchResult> searchResultList,
			final int fileCount,
			final int fileContainingTextCount,
			final TextFinder textFinder) {

		customTableView.setItems(searchResultList);

		fileCountLabel.setVisible(true);
		fileCountTextField.setText(StrUtils.positiveIntToString(fileCount, true));
		fileCountTextField.setVisible(true);

		if (textFinder != null) {

			fileContainingTextCountLabel.setVisible(true);
			fileContainingTextCountTextField.setText(
					StrUtils.positiveIntToString(fileContainingTextCount, true));
			fileContainingTextCountTextField.setVisible(true);
		}

		this.textFinder = textFinder;
	}
}
