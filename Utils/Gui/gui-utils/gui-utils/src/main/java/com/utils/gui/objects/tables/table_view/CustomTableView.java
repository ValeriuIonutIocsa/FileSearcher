package com.utils.gui.objects.tables.table_view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;

import com.utils.annotations.ApiMethod;
import com.utils.data_types.data_items.DataItem;
import com.utils.data_types.table.TableColumnData;
import com.utils.data_types.table.TableRowData;
import com.utils.gui.alerts.CustomAlertConfirm;
import com.utils.gui.clipboard.ClipboardUtils;
import com.utils.gui.objects.PopupWindow;
import com.utils.gui.objects.search_and_filter.FilterType;
import com.utils.gui.objects.search_and_filter.SearchAndFilterTable;
import com.utils.gui.objects.search_and_filter.VBoxFilterTable;
import com.utils.gui.objects.search_and_filter.VBoxSearchTable;
import com.utils.gui.objects.tables.ColumnHeader;
import com.utils.gui.objects.tables.TableUtils;
import com.utils.gui.version.VersionDependentMethods;
import com.utils.log.Logger;
import com.utils.string.regex.custom_patterns.CustomPatterns;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

public class CustomTableView<
		TableRowDataT extends TableRowData>
		extends TableView<TableRowDataT> implements SearchAndFilterTable {

	private TableColumnData[] tableColumnDataArray;
	private final List<TableColumn<TableRowDataT, Object>> columnList;
	private final List<ColumnHeader> columnHeaderList;
	private final Map<String, TableColumn<TableRowDataT, Object>> columnsByNameMap;
	private final ObservableList<TableRowDataT> unfilteredItemList;
	private final FilteredList<TableRowDataT> filteredItemList;

	private ScrollBar verticalScrollBar;
	private boolean hideVerticalScrollBar;
	private Predicate<TableRowDataT> filterPredicate;

	public CustomTableView(
			final TableColumnData[] tableColumnDataArray,
			final boolean editable,
			final boolean multipleSelection,
			final boolean cellSelection,
			final boolean sort,
			final boolean filter,
			final int defaultSearchAndFilterColumnIndex) {

		this.tableColumnDataArray = tableColumnDataArray;

		columnList = new ArrayList<>();
		columnHeaderList = new ArrayList<>();
		columnsByNameMap = new LinkedHashMap<>();
		unfilteredItemList = FXCollections.observableArrayList();

		filterPredicate = tableViewItem -> true;

		setMinHeight(80);

		setEditable(editable);

		final SelectionMode selectionMode;
		if (multipleSelection) {
			selectionMode = SelectionMode.MULTIPLE;
		} else {
			selectionMode = SelectionMode.SINGLE;
		}
		getSelectionModel().setSelectionMode(selectionMode);
		getSelectionModel().setCellSelectionEnabled(cellSelection);

		createTableColumns(tableColumnDataArray, sort, filter);

		filteredItemList = new FilteredList<>(unfilteredItemList);
		filteredItemList.setPredicate(filterPredicate);

		final SortedList<TableRowDataT> sortedItemList = new SortedList<>(filteredItemList);
		sortedItemList.comparatorProperty().bind(comparatorProperty());

		setItems(sortedItemList);

		setOnKeyPressed(event -> keyPressed(event, defaultSearchAndFilterColumnIndex));
	}

	@ApiMethod
	public void setHideVerticalScrollBar(
			final boolean hideVerticalScrollBar) {
		this.hideVerticalScrollBar = hideVerticalScrollBar;
	}

	private ScrollBar horizontalScrollBar;
	private boolean hideHorizontalScrollBar;

	@ApiMethod
	public void setHideHorizontalScrollBar(
			final boolean hideHorizontalScrollBar) {
		this.hideHorizontalScrollBar = hideHorizontalScrollBar;
	}

	@Override
	protected void layoutChildren() {

		super.layoutChildren();

		if (hideVerticalScrollBar || hideHorizontalScrollBar) {

			final Set<Node> scrollBars = lookupAll(".scroll-bar");
			for (final Node node : scrollBars) {

				if (node instanceof final ScrollBar scrollBar) {

					final Orientation orientation = scrollBar.getOrientation();
					if (orientation == Orientation.VERTICAL) {
						verticalScrollBar = scrollBar;
					} else if (orientation == Orientation.HORIZONTAL) {
						horizontalScrollBar = scrollBar;
					}
				}
			}

			if (verticalScrollBar != null) {
				if (hideVerticalScrollBar) {
					verticalScrollBar.visibleProperty().addListener((
							observable,
							oldValue,
							newValue) -> verticalScrollBar.setVisible(false));
				}
			}

			if (horizontalScrollBar != null) {
				if (hideHorizontalScrollBar) {
					horizontalScrollBar.visibleProperty().addListener((
							observable,
							oldValue,
							newValue) -> horizontalScrollBar.setVisible(false));
				}
			}
		}
	}

	protected void keyPressed(
			final KeyEvent keyEvent,
			final int defaultSearchAndFilterColumnIndex) {

		if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.MINUS ||
				keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.SUBTRACT) {
			deselectAllKeyCombinationPressed();

		} else if (keyEvent.isControlDown() && !keyEvent.isShiftDown() && keyEvent.getCode() == KeyCode.F) {
			searchKeyCombinationPressed(defaultSearchAndFilterColumnIndex);
		} else if (keyEvent.isControlDown() && keyEvent.isShiftDown() && keyEvent.getCode() == KeyCode.F) {
			filterKeyCombinationPressed(defaultSearchAndFilterColumnIndex);

		} else if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.C) {
			copyKeyCombinationPressed();
		} else if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.V) {
			pasteKeyCombinationPressed();
		}
	}

	private void deselectAllKeyCombinationPressed() {

		getSelectionModel().clearSelection();
	}

	private void searchKeyCombinationPressed(
			final int defaultSearchAndFilterColumnIndex) {

		int columnIndex = defaultSearchAndFilterColumnIndex;
		final TablePosition<?, ?> focusedCell = getFocusModel().getFocusedCell();
		if (focusedCell != null) {
			columnIndex = focusedCell.getColumn();
		}
		search(columnIndex);
	}

	private void filterKeyCombinationPressed(
			final int defaultSearchAndFilterColumnIndex) {

		int columnIndex = defaultSearchAndFilterColumnIndex;
		final TablePosition<?, ?> focusedCell = getFocusModel().getFocusedCell();
		if (focusedCell != null) {
			columnIndex = focusedCell.getColumn();
		}
		filter(columnIndex);
	}

	protected void copyKeyCombinationPressed() {

		final StringBuilder sbClipboardText = new StringBuilder();

		final boolean cellSelectionEnabled = getSelectionModel().isCellSelectionEnabled();
		if (cellSelectionEnabled) {

			final int selectedCellCount = getSelectionModel().getSelectedCells().size();
			for (int i = 0; i < selectedCellCount; i++) {

				final TablePosition<?, ?> tablePosition =
						getSelectionModel().getSelectedCells().get(i);
				final int row = tablePosition.getRow();

				final Object cellData = tablePosition.getTableColumn().getCellData(row);
				TableUtils.appendCellText(cellData, sbClipboardText);

				if (i < selectedCellCount - 1) {

					final TablePosition<?, ?> nextTablePosition =
							getSelectionModel().getSelectedCells().get(i + 1);
					final int nextCellRow = nextTablePosition.getRow();
					if (row < nextCellRow) {
						sbClipboardText.append(System.lineSeparator());
					} else {
						sbClipboardText.append('\t');
					}
				}
			}

		} else {
			final List<Integer> selectedRowList = getSelectionModel().getSelectedIndices();
			for (int i = 0; i < selectedRowList.size(); i++) {

				final int row = selectedRowList.get(i);
				for (int j = 0; j < columnList.size(); j++) {

					final TableColumn<TableRowDataT, Object> tableColumn = columnList.get(j);
					final Object cellData = tableColumn.getCellData(row);
					TableUtils.appendCellText(cellData, sbClipboardText);
					if (j < columnList.size() - 1) {
						sbClipboardText.append('\t');
					}
				}
				if (i < selectedRowList.size() - 1) {
					sbClipboardText.append(System.lineSeparator());
				}
			}
		}

		final String clipboardText = sbClipboardText.toString();
		if (StringUtils.isNotBlank(clipboardText)) {
			ClipboardUtils.putStringInClipBoard(clipboardText);
		}
	}

	@Override
	public void pasteKeyCombinationPressed() {
	}

	public void createTableColumns(
			final TableColumnData[] tableColumnDataArray,
			final boolean sort,
			final boolean filter) {

		this.tableColumnDataArray = tableColumnDataArray;

		getColumns().clear();
		columnList.clear();
		columnHeaderList.clear();
		columnsByNameMap.clear();

		double widthWeightSum = 0;
		for (final TableColumnData tableColumn : tableColumnDataArray) {

			final double widthWeight = tableColumn.getWidthWeight();
			widthWeightSum += widthWeight;
		}

		for (int i = 0; i < tableColumnDataArray.length; i++) {

			final int columnIndex = i;
			final TableColumnData tableColumnData = tableColumnDataArray[columnIndex];

			final TableColumn<TableRowDataT, Object> tableColumn = new TableColumn<>();
			tableColumn.setSortable(false);

			TableUtils.setTableColumnData(tableColumn,
					widthProperty(), widthWeightSum, tableColumnData);

			final ColumnHeader columnHeader = new ColumnHeader(tableColumn);

			final ContextMenu contextMenuTableColumn =
					createContextMenuTableColumn(columnIndex, sort, filter);
			tableColumn.setContextMenu(contextMenuTableColumn);

			tableColumn.setCellValueFactory(cellDataFeatures -> {

				ObservableValue<Object> observableValue = null;
				final TableRowDataT value = cellDataFeatures.getValue();
				if (value != null) {

					final DataItem<?>[] tableViewData = value.getTableViewDataItemArray();
					final Object object = tableViewData[columnIndex];
					observableValue = new SimpleObjectProperty<>(object);
				}
				return observableValue;
			});

			columnList.add(tableColumn);
			columnHeaderList.add(columnHeader);
			final String columnName = tableColumn.getText();
			columnsByNameMap.put(columnName, tableColumn);
			getColumns().add(tableColumn);
		}

		VersionDependentMethods.disableTableViewColumnReordering(this);
	}

	private ContextMenu createContextMenuTableColumn(
			final int columnIndex,
			final boolean sort,
			final boolean filter) {

		final ContextMenu contextMenu = new ContextMenu();

		final MenuItem menuItemSearch = new MenuItem("search");
		menuItemSearch.setOnAction(event -> search(columnIndex));
		contextMenu.getItems().add(menuItemSearch);

		if (filter) {
			final MenuItem menuItemFilter = new MenuItem("filter");
			menuItemFilter.setOnAction(event -> filter(columnIndex));
			contextMenu.getItems().add(menuItemFilter);

			final MenuItem menuItemClearFilters = new MenuItem("clear filters");
			menuItemClearFilters.setOnAction(event -> clearFilters(true));
			contextMenu.getItems().add(menuItemClearFilters);
		}

		if (sort) {
			final MenuItem menuItemSortAscending = new MenuItem("sort A - Z");
			menuItemSortAscending.setOnAction(event -> sortAscending(columnIndex));
			contextMenu.getItems().add(menuItemSortAscending);

			final MenuItem menuItemSortDescending = new MenuItem("sort Z - A");
			menuItemSortDescending.setOnAction(event -> sortDescending(columnIndex));
			contextMenu.getItems().add(menuItemSortDescending);

			final MenuItem menuItemClearSorting = new MenuItem("clear sorting");
			menuItemClearSorting.setOnAction(event -> clearSorting());
			contextMenu.getItems().add(menuItemClearSorting);
		}

		return contextMenu;
	}

	private void search(
			final int columnIndex) {

		final VBoxSearchTable vBoxSearchTable = new VBoxSearchTable(this, columnIndex);
		new PopupWindow(getScene(), null, Modality.APPLICATION_MODAL, "Search Table",
				null, vBoxSearchTable.getRoot()).show();
	}

	@Override
	public void searchTable(
			final int searchColumnIndex,
			final CustomPatterns searchCustomPatterns) {

		final Predicate<TableRowDataT> searchPredicate = dataElementTableViewRow -> {

			boolean matchesPredicate = false;
			if (dataElementTableViewRow != null) {

				final Object[] rowData = dataElementTableViewRow.getTableViewDataItemArray();
				final Object rowDataObject = rowData[searchColumnIndex];
				final String filterCellString = Objects.toString(rowDataObject, "");
				matchesPredicate = searchCustomPatterns.checkMatchesPatterns(filterCellString);
			}
			return matchesPredicate;
		};

		searchInTableView(searchColumnIndex, searchPredicate, true, true);
	}

	@ApiMethod
	public boolean searchInTableView(
			final int searchColumnIndex,
			final Predicate<TableRowDataT> searchPredicate,
			final boolean requestFocus,
			final boolean verbose) {

		boolean found = false;
		final int currentRow = getSelectionModel().getSelectedIndex();

		int foundRow = -1;
		final int rowCount = getItems().size();
		for (int row = currentRow + 1; row < rowCount; row++) {

			final TableRowDataT item = getItems().get(row);
			if (searchPredicate.test(item)) {
				foundRow = row;
				break;
			}
		}

		if (foundRow == -1) {
			for (int row = 0; row <= currentRow; row++) {

				final TableRowDataT item = getItems().get(row);
				if (searchPredicate.test(item)) {
					if (verbose) {
						Logger.printStatus(System.lineSeparator() +
								"The search wrapped around the end of the file.");
					}
					foundRow = row;
					break;
				}
			}
		}

		if (foundRow == -1) {
			if (verbose) {
				Logger.printNewLine();
				Logger.printWarning("the search has found no results");
			}

		} else {
			final TableColumn<TableRowDataT, Object> searchColumn = columnList.get(searchColumnIndex);
			getSelectionModel().clearAndSelect(foundRow, searchColumn);
			scrollTo(foundRow);
			if (requestFocus) {
				requestFocus();
			}
			found = true;
		}
		return found;
	}

	private void filter(
			final int columnIndex) {

		final VBoxFilterTable vBoxFilterTable = new VBoxFilterTable(this, columnIndex);
		new PopupWindow(getScene(), null, Modality.APPLICATION_MODAL, "Filter Table",
				null, vBoxFilterTable.getRoot()).show();
	}

	@Override
	public void filterTable(
			final FilterType filterType,
			final int filterColumnIndex,
			final CustomPatterns filterCustomPatterns) {
		addColumnFilter(filterColumnIndex, filterCustomPatterns, filterType, true);
	}

	@ApiMethod
	public void addColumnFilter(
			final int filterColumnIndex,
			final CustomPatterns filterCustomPatterns,
			final FilterType filterType,
			final boolean verbose) {

		final ColumnHeader columnHeader = columnHeaderList.get(filterColumnIndex);
		columnHeader.setFilterGraphic();

		final Predicate<TableRowDataT> filterPredicate = dataElementTableViewRow -> {

			boolean matchesPredicated = false;
			if (dataElementTableViewRow != null) {

				final Object[] rowData = dataElementTableViewRow.getTableViewDataItemArray();
				final Object rowDataObject = rowData[filterColumnIndex];
				final String filterCellString = Objects.toString(rowDataObject, "");
				matchesPredicated = filterCustomPatterns.checkMatchesPatterns(filterCellString);
			}
			return matchesPredicated;
		};

		if (filterType == FilterType.AND) {
			this.filterPredicate = this.filterPredicate.and(filterPredicate);

		} else if (filterType == FilterType.OR) {
			this.filterPredicate = this.filterPredicate.or(filterPredicate);
		}

		setSortedAndFilteredItems();

		if (verbose) {

			final String message;
			final String filterColumnText = columnHeader.getColumnName();
			if (StringUtils.isNotBlank(filterColumnText)) {
				message = filterType + " filter applied to column " + "\"" + filterColumnText + "\"";
			} else {
				message = filterType + " filter applied to column " + filterColumnIndex;
			}
			Logger.printNewLine();
			Logger.printStatus(message);
		}
	}

	@ApiMethod
	public void addColumnFilter(
			final int filterColumnIndex,
			final Predicate<TableRowDataT> filterPredicate,
			final FilterType filterType,
			final boolean verbose) {

		final boolean applyFilters;
		if (filterType == FilterType.AND) {
			this.filterPredicate = this.filterPredicate.and(filterPredicate);
			applyFilters = true;

		} else if (filterType == FilterType.OR) {
			this.filterPredicate = this.filterPredicate.or(filterPredicate);
			applyFilters = true;

		} else {
			Logger.printError("filter type has to not be null to add column filter");
			applyFilters = false;
		}

		if (applyFilters) {

			if (filterColumnIndex >= 0) {
				final ColumnHeader columnHeader = columnHeaderList.get(filterColumnIndex);
				columnHeader.setFilterGraphic();
			}

			setSortedAndFilteredItems();

			if (verbose) {
				TableUtils.printFilterAppliedMessage(filterType, filterColumnIndex, columnHeaderList);
			}
		}
	}

	@ApiMethod
	public void clearFilters(
			final boolean verbose) {

		filterPredicate = tableViewItem -> true;
		setSortedAndFilteredItems();
		for (final ColumnHeader columnHeader : columnHeaderList) {
			columnHeader.removeFilterGraphic();
		}
		if (verbose) {
			Logger.printStatus(System.lineSeparator() + "Filters cleared.");
		}
	}

	private void sortAscending(
			final int columnIndex) {

		final TableColumn<TableRowDataT, Object> sortColumn = columnList.get(columnIndex);
		if (sortColumn != null) {

			sortColumn.setSortable(true);
			sortColumn.setSortType(TableColumn.SortType.ASCENDING);
			getSortOrder().addFirst(sortColumn);
			refresh();
			sortColumn.setSortable(false);
		}
	}

	private void sortDescending(
			final int columnIndex) {

		final TableColumn<TableRowDataT, Object> sortColumn = columnList.get(columnIndex);
		if (sortColumn != null) {

			sortColumn.setSortable(true);
			sortColumn.setSortType(TableColumn.SortType.DESCENDING);
			getSortOrder().addFirst(sortColumn);
			refresh();
			sortColumn.setSortable(false);
		}
	}

	private void clearSorting() {

		getSortOrder().clear();
		refresh();
	}

	@ApiMethod
	public void moveSelectedItem(
			final int offset,
			final TableItemIndexUpdater<TableRowDataT> tableItemIndexUpdater,
			final Comparator<TableRowDataT> comparator) {

		final boolean filteredTable = getItems().size() != unfilteredItemList.size();
		if (filteredTable && tableItemIndexUpdater == null) {
			Logger.printNewLine();
			Logger.printWarning("to move item, the table cannot be filtered");

		} else {
			final List<Integer> selectedIndices = getSelectionModel().getSelectedIndices();
			if (selectedIndices.isEmpty()) {
				Logger.printNewLine();
				Logger.printWarning("to move item, a single item should be selected" +
						System.lineSeparator() + "there are no items selected");

			} else {
				if (selectedIndices.size() > 1) {
					Logger.printNewLine();
					Logger.printWarning("to move item, a single item should be selected" +
							System.lineSeparator() + "there are multiple items selected");

				} else {
					final int selectedIndex = selectedIndices.getFirst();
					final int newIndex = selectedIndex + offset;
					if (newIndex < 0) {
						Logger.printNewLine();
						Logger.printWarning("the selected item is already the first item");

					} else {
						final List<TableRowDataT> itemList = getItems();
						if (newIndex == itemList.size()) {
							Logger.printNewLine();
							Logger.printWarning("the selected item is already the last item");

						} else {
							final TableRowDataT selectedItem = itemList.get(selectedIndex);
							getSelectionModel().clearSelection();
							if (filteredTable) {

								final List<TableRowDataT> copyItemList = new ArrayList<>(itemList);
								copyItemList.remove(selectedIndex);
								copyItemList.add(newIndex, selectedItem);
								for (int i = 0; i < copyItemList.size(); i++) {

									final TableRowDataT item = copyItemList.get(i);
									tableItemIndexUpdater.updateIndex(item, i);
								}
								unfilteredItemList.sort(comparator);

							} else {
								removeItem(selectedItem);
								unfilteredItemList.add(newIndex, selectedItem);
								if (tableItemIndexUpdater != null) {
									for (int i = 0; i < unfilteredItemList.size(); i++) {

										final TableRowDataT item = unfilteredItemList.get(i);
										tableItemIndexUpdater.updateIndex(item, i);
									}
								}
							}
							setSortedAndFilteredItems();
							getSelectionModel().select(newIndex);
						}
					}
				}
			}
		}
	}

	@ApiMethod
	public boolean removeSelectedItems(
			final String itemDisplayName,
			final String itemDisplayNamePlural,
			final boolean askForConfirmation,
			final Function<List<TableRowDataT>, Boolean> itemRemover) {

		boolean itemsRemoved = false;
		final List<TableRowDataT> selectedItemList =
				new ArrayList<>(getSelectionModel().getSelectedItems());
		if (selectedItemList.isEmpty()) {
			if (askForConfirmation) {
				Logger.printWarning("no " + itemDisplayName + " is selected");
			}

		} else {
			boolean keepGoing = true;
			final int itemCount = selectedItemList.size();
			if (askForConfirmation) {

				final ButtonType result = checkConfirmRemoveSelectedItems(
						itemDisplayName, itemDisplayNamePlural, itemCount);
				if (result == ButtonType.NO) {
					keepGoing = false;
				}
			}

			if (keepGoing) {

				if (itemRemover != null) {

					final boolean success = itemRemover.apply(selectedItemList);
					if (!success) {
						keepGoing = false;
					}
				}

				if (keepGoing) {

					removeItems(selectedItemList);
					if (askForConfirmation) {

						Logger.printNewLine();
						final String plural;
						if (itemCount == 1) {
							plural = "";
						} else {
							plural = "s";
						}
						Logger.printStatus("successfully removed the " + itemDisplayName + " item" + plural);
					}
					itemsRemoved = true;
				}
			}
		}
		return itemsRemoved;
	}

	private static ButtonType checkConfirmRemoveSelectedItems(
			final String itemDisplayName,
			final String itemDisplayNamePlural,
			final int itemCount) {

		final String itemCountString;
		if (itemCount == 1) {
			itemCountString = itemDisplayName;
		} else {
			itemCountString = itemCount + " " + itemDisplayNamePlural;
		}
		final CustomAlertConfirm customAlertConfirm = new CustomAlertConfirm("Removing " + itemDisplayName,
				"Are you sure you wish to remove the selected " + itemCountString + " from the list?",
				ButtonType.NO, ButtonType.YES);
		customAlertConfirm.showAndWait();

		return customAlertConfirm.getResult();
	}

	@ApiMethod
	public boolean removeSelectedItem(
			final String itemDisplayName,
			final boolean askForConfirmation,
			final Function<TableRowDataT, Boolean> itemRemover) {

		boolean itemRemoved = false;
		final TableRowDataT selectedItem = getSelectionModel().getSelectedItem();
		if (selectedItem == null) {
			if (askForConfirmation) {
				Logger.printWarning("no " + itemDisplayName + " is selected");
			}

		} else {
			boolean keepGoing = true;
			if (askForConfirmation) {

				final ButtonType result = checkConfirmRemoveSelectedItem(itemDisplayName);
				if (result == ButtonType.NO) {
					keepGoing = false;
				}
			}

			if (keepGoing) {

				if (itemRemover != null) {
					final boolean success = itemRemover.apply(selectedItem);
					if (!success) {
						keepGoing = false;
					}
				}

				if (keepGoing) {

					removeItem(selectedItem);
					if (askForConfirmation) {

						Logger.printNewLine();
						Logger.printStatus("successfully removed the " + itemDisplayName + " item");
						itemRemoved = true;
					}
				}
			}
		}
		return itemRemoved;
	}

	private static ButtonType checkConfirmRemoveSelectedItem(
			final String itemDisplayName) {

		final CustomAlertConfirm customAlertConfirm = new CustomAlertConfirm("Removing " + itemDisplayName,
				"Are you sure you wish to remove the selected " + itemDisplayName + " from the list?",
				ButtonType.NO, ButtonType.YES);
		customAlertConfirm.showAndWait();

		return customAlertConfirm.getResult();
	}

	@ApiMethod
	public void setItems(
			final Collection<TableRowDataT> itemsList) {

		unfilteredItemList.clear();
		if (itemsList != null) {
			unfilteredItemList.addAll(itemsList);
		}
		setSortedAndFilteredItems();
	}

	@ApiMethod
	public void addItem(
			final TableRowDataT item) {

		addItem(item, unfilteredItemList.size());
	}

	@ApiMethod
	public void addItem(
			final TableRowDataT item,
			final int index) {

		unfilteredItemList.add(index, item);
		setSortedAndFilteredItems();
	}

	@ApiMethod
	public void addItems(
			final Collection<TableRowDataT> items) {

		unfilteredItemList.addAll(items);
		setSortedAndFilteredItems();
	}

	@ApiMethod
	public void removeItems(
			final Collection<TableRowDataT> itemsToRemove) {

		final Iterator<TableRowDataT> iterator = unfilteredItemList.iterator();
		while (iterator.hasNext()) {

			final TableRowDataT item = iterator.next();
			if (item != null) {

				for (final TableRowDataT itemToRemove : itemsToRemove) {

					final boolean equals = item.equals(itemToRemove);
					if (equals) {

						iterator.remove();
						break;
					}
				}
			}
		}
		setSortedAndFilteredItems();
	}

	private void removeItem(
			final TableRowDataT itemToRemove) {

		if (itemToRemove != null) {

			final Iterator<TableRowDataT> iterator = unfilteredItemList.iterator();
			while (iterator.hasNext()) {

				final TableRowDataT item = iterator.next();
				if (itemToRemove.equals(item)) {

					iterator.remove();
					break;
				}
			}
			setSortedAndFilteredItems();
		}
	}

	private void setSortedAndFilteredItems() {

		filteredItemList.setPredicate(filterPredicate);
		refresh();
	}

	@ApiMethod
	public boolean containsItem(
			final TableRowDataT item) {
		return unfilteredItemList.contains(item);
	}

	@Override
	public Collection<String> getTableColumnNames() {
		return columnsByNameMap.keySet();
	}

	@ApiMethod
	public TableColumn<TableRowDataT, Object> getColumnByName(
			final String columnName) {

		return columnsByNameMap.get(columnName);
	}

	@Override
	public int computeTotalItemCount() {

		return unfilteredItemList.size();
	}

	@ApiMethod
	public TableColumnData[] getTableColumnDataArray() {
		return tableColumnDataArray;
	}

	@ApiMethod
	public List<TableColumn<TableRowDataT, Object>> getColumnList() {
		return columnList;
	}

	@ApiMethod
	public ObservableList<TableRowDataT> getUnfilteredItemList() {
		return unfilteredItemList;
	}
}
