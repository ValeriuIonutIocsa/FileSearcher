package com.utils.gui.objects.tables.tree_table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;

import com.utils.annotations.ApiMethod;
import com.utils.data_types.collections.CollectionUtils;
import com.utils.data_types.data_items.DataItem;
import com.utils.data_types.table.TableColumnData;
import com.utils.data_types.table.TableRowData;
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
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTablePosition;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

public class CustomTreeTableView<
		TableRowDataT extends TableRowData>
		extends TreeTableView<TableRowDataT> implements SearchAndFilterTable {

	private final int defaultSearchAndFilterColumnIndex;
	private final List<TreeTableColumn<TableRowDataT, Object>> columnList;
	private final List<ColumnHeader> columnHeaderList;
	private final Map<String, TreeTableColumn<TableRowDataT, Object>> columnsByNameMap;
	private final UnfilteredTreeItem<TableRowDataT> unfilteredTreeItemRoot;
	private final List<TreeItem<TableRowDataT>> treeItemList;

	private ScrollBar verticalScrollBar;
	private boolean hideVerticalScrollBar;
	private Predicate<TableRowDataT> filterPredicate;

	private boolean expandedListenerActive;

	public CustomTreeTableView(
			final TableColumnData[] tableColumnDataArray,
			final boolean editable,
			final boolean multipleSelection,
			final boolean cellSelection,
			final boolean filter,
			final int defaultSearchAndFilterColumnIndex) {

		this.defaultSearchAndFilterColumnIndex = defaultSearchAndFilterColumnIndex;

		columnList = new ArrayList<>();
		columnHeaderList = new ArrayList<>();
		columnsByNameMap = new LinkedHashMap<>();
		unfilteredTreeItemRoot = new UnfilteredTreeItem<>(null, true);
		treeItemList = new ArrayList<>();

		filterPredicate = item -> true;

		expandedListenerActive = true;

		setMinHeight(80);

		setShowRoot(false);
		setEditable(editable);

		final SelectionMode selectionMode;
		if (multipleSelection) {
			selectionMode = SelectionMode.MULTIPLE;
		} else {
			selectionMode = SelectionMode.SINGLE;
		}
		getSelectionModel().setSelectionMode(selectionMode);
		getSelectionModel().setCellSelectionEnabled(cellSelection);

		createTreeTableColumns(tableColumnDataArray, filter);

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

			final Set<Node> scrollBarSet = lookupAll(".scroll-bar");
			for (final Node node : scrollBarSet) {

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
		final TreeTablePosition<TableRowDataT, ?> focusedCell = getFocusModel().getFocusedCell();
		if (focusedCell != null) {
			columnIndex = focusedCell.getColumn();
		}
		search(columnIndex);
	}

	private void filterKeyCombinationPressed(
			final int defaultSearchAndFilterColumnIndex) {

		int columnIndex = defaultSearchAndFilterColumnIndex;
		final TreeTablePosition<TableRowDataT, ?> focusedCell = getFocusModel().getFocusedCell();
		if (focusedCell != null) {
			columnIndex = focusedCell.getColumn();
		}
		filter(columnIndex);
	}

	protected void copyKeyCombinationPressed() {

		final StringBuilder sbClipboardText = new StringBuilder();

		final boolean cellSelectionEnabled = getSelectionModel().isCellSelectionEnabled();
		if (cellSelectionEnabled) {

			final List<TreeTablePosition<TableRowDataT, ?>> selectedCellList =
					getSelectionModel().getSelectedCells();
			final int selectedCellCount = selectedCellList.size();
			for (int i = 0; i < selectedCellCount; i++) {

				final TreeTablePosition<TableRowDataT, ?> treeTablePosition = selectedCellList.get(i);
				final int row = treeTablePosition.getRow();
				final int column = treeTablePosition.getColumn();

				final TreeTableColumn<TableRowDataT, Object> treeTableColumn = columnList.get(column);
				final TreeItem<TableRowDataT> treeItem = treeTablePosition.getTreeItem();
				final ObservableValue<Object> cellObservableValue =
						treeTableColumn.getCellValueFactory().call(
								new TreeTableColumn.CellDataFeatures<>(this, treeTableColumn, treeItem));
				final Object cellValue = cellObservableValue.getValue();
				TableUtils.appendCellText(cellValue, sbClipboardText);

				if (i < selectedCellCount - 1) {

					final TreeTablePosition<TableRowDataT, ?> nextTablePosition = selectedCellList.get(i + 1);
					final int nextCellRow = nextTablePosition.getRow();
					if (row < nextCellRow) {
						sbClipboardText.append(System.lineSeparator());
					} else {
						sbClipboardText.append('\t');
					}
				}
			}

		} else {
			final List<TreeItem<TableRowDataT>> selectedItemList = getSelectionModel().getSelectedItems();
			for (int i = 0; i < selectedItemList.size(); i++) {

				final TreeItem<TableRowDataT> treeItem = selectedItemList.get(i);
				for (int j = 0; j < columnList.size(); j++) {

					final TreeTableColumn<TableRowDataT, Object> treeTableColumn = columnList.get(j);
					final ObservableValue<Object> cellObservableValue =
							treeTableColumn.getCellValueFactory().call(
									new TreeTableColumn.CellDataFeatures<>(this, treeTableColumn, treeItem));
					final Object cellValue = cellObservableValue.getValue();
					TableUtils.appendCellText(cellValue, sbClipboardText);
					if (j < columnList.size() - 1) {
						sbClipboardText.append('\t');
					}
				}
				if (i < selectedItemList.size() - 1) {
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

	public void createTreeTableColumns(
			final TableColumnData[] tableColumnDataArray,
			final boolean filter) {

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

			final TreeTableColumn<TableRowDataT, Object> treeTableColumn = new TreeTableColumn<>();
			treeTableColumn.setSortable(false);

			TableUtils.setTableColumnData(
					treeTableColumn, widthProperty(), widthWeightSum, tableColumnData);

			final ColumnHeader columnHeader = new ColumnHeader(treeTableColumn);

			final ContextMenu contextMenuTableColumn =
					createContextMenuTreeTableColumn(columnIndex, filter);
			treeTableColumn.setContextMenu(contextMenuTableColumn);

			treeTableColumn.setCellValueFactory(cellDataFeatures -> {

				ObservableValue<Object> observableValue = null;
				final TreeItem<TableRowDataT> treeItem = cellDataFeatures.getValue();
				final TableRowDataT value = treeItem.getValue();
				if (value != null) {

					final DataItem<?>[] tableViewData = value.getTableViewDataItemArray();
					final Object object = tableViewData[columnIndex];
					observableValue = new SimpleObjectProperty<>(object);
				}
				return observableValue;
			});

			columnList.add(treeTableColumn);
			columnHeaderList.add(columnHeader);
			final String columnName = treeTableColumn.getText();
			columnsByNameMap.put(columnName, treeTableColumn);
			getColumns().add(treeTableColumn);
		}

		VersionDependentMethods.disableTreeTableViewColumnReordering(this);
	}

	private ContextMenu createContextMenuTreeTableColumn(
			final int columnIndex,
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

		return contextMenu;
	}

	private void search(
			final int columnIndex) {

		final VBoxSearchTable vBoxSearchTable = new VBoxSearchTable(this, columnIndex);
		new PopupWindow(getScene(), null, Modality.APPLICATION_MODAL, "Search Tree Table",
				null, vBoxSearchTable.getRoot()).show();
	}

	@Override
	public void searchTable(
			final int searchColumnIndex,
			final CustomPatterns searchCustomPatterns) {

		final Predicate<TableRowDataT> searchPredicate = dataElementTableViewRow -> {

			boolean result = false;
			if (dataElementTableViewRow != null) {

				final Object[] rowData = dataElementTableViewRow.getTableViewDataItemArray();
				final Object rowDataObject = rowData[searchColumnIndex];
				final String filterCellString = Objects.toString(rowDataObject, "");
				result = searchCustomPatterns.checkMatchesPatterns(filterCellString);
			}
			return result;
		};

		searchInTreeTableView(searchColumnIndex, searchPredicate, true, true);
	}

	@ApiMethod
	public boolean searchInTreeTableView(
			final int searchColumnIndex,
			final Predicate<TableRowDataT> searchPredicate,
			final boolean requestFocus,
			final boolean verbose) {

		boolean found = false;
		final List<TreeItem<TableRowDataT>> treeItemList = createTreeItemList();
		final TreeItem<TableRowDataT> selectedItem = getSelectionModel().getSelectedItem();
		final int currentRow = Math.max(treeItemList.indexOf(selectedItem), 0);

		int foundRow = -1;
		final int rowCount = treeItemList.size();
		for (int row = currentRow + 1; row < rowCount; row++) {

			final TreeItem<TableRowDataT> treeItem = treeItemList.get(row);
			final TableRowDataT item = treeItem.getValue();
			if (item != null && searchPredicate.test(item)) {
				foundRow = row;
				break;
			}
		}

		if (foundRow == -1) {
			for (int row = 0; row <= currentRow; row++) {

				final TreeItem<TableRowDataT> treeItem = treeItemList.get(row);
				final TableRowDataT item = treeItem.getValue();
				if (item != null && searchPredicate.test(item)) {

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
			final TreeItem<TableRowDataT> foundTreeItem = treeItemList.get(foundRow);
			focusItem(foundTreeItem, searchColumnIndex, requestFocus);
			found = true;
		}
		return found;
	}

	@ApiMethod
	public List<TreeItem<TableRowDataT>> createTreeItemList() {

		final List<TreeItem<TableRowDataT>> treeItemList = new ArrayList<>();
		createTreeItemListRec(getRoot(), treeItemList);
		return treeItemList;
	}

	private void createTreeItemListRec(
			final TreeItem<TableRowDataT> treeItem,
			final List<TreeItem<TableRowDataT>> treeItemList) {

		if (treeItem != null) {

			treeItemList.add(treeItem);

			for (final TreeItem<TableRowDataT> child : treeItem.getChildren()) {
				createTreeItemListRec(child, treeItemList);
			}
		}
	}

	private void focusItem(
			final TreeItem<TableRowDataT> foundItem,
			final int searchColumnIndex,
			final boolean requestFocus) {

		setExpandedRec(foundItem.getParent());
		final int foundItemRowIndex = getRow(foundItem);

		getSelectionModel().clearSelection();
		final int columnIndex;
		if (0 <= searchColumnIndex && searchColumnIndex <= columnList.size()) {
			columnIndex = searchColumnIndex;
		} else {
			columnIndex = defaultSearchAndFilterColumnIndex;
		}
		final TreeTableColumn<TableRowDataT, ?> searchColumn = columnList.get(columnIndex);
		getSelectionModel().select(foundItemRowIndex, searchColumn);

		scrollTo(Math.max(0, foundItemRowIndex));
		if (requestFocus) {
			requestFocus();
		}
	}

	private void setExpandedRec(
			final TreeItem<TableRowDataT> treeItem) {

		if (treeItem != null) {

			treeItem.setExpanded(true);
			final TreeItem<TableRowDataT> treeItemParent = treeItem.getParent();
			setExpandedRec(treeItemParent);
		}
	}

	private void filter(
			final int columnIndex) {

		final VBoxFilterTable vBoxFilterTable = new VBoxFilterTable(this, columnIndex);
		new PopupWindow(getScene(), null, Modality.APPLICATION_MODAL, "Filter Tree Table",
				null, vBoxFilterTable.getRoot()).show();
	}

	@Override
	public void filterTable(
			final FilterType filterType,
			final int filterColumnIndex,
			final CustomPatterns filterCustomPatterns) {

		final Predicate<TableRowDataT> filterPredicate = dataElementTableViewRow -> {

			boolean result = false;
			if (dataElementTableViewRow != null) {

				final Object[] rowData = dataElementTableViewRow.getTableViewDataItemArray();
				final Object rowDataObject = rowData[filterColumnIndex];
				final String filterCellString = Objects.toString(rowDataObject, "");
				result = filterCustomPatterns.checkMatchesPatterns(filterCellString);
			}
			return result;
		};

		addColumnFilter(filterColumnIndex, filterPredicate, filterType, true);
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

			setFilteredItems();

			if (verbose) {
				TableUtils.printFilterAppliedMessage(filterType, filterColumnIndex, columnHeaderList);
			}
		}
	}

	protected void clearFilters(
			final boolean verbose) {

		filterPredicate = item -> true;
		setFilteredItems();
		for (final ColumnHeader columnHeader : columnHeaderList) {
			columnHeader.removeFilterGraphic();
		}
		if (verbose) {
			Logger.printNewLine();
			Logger.printStatus("Filters cleared.");
		}
	}

	@ApiMethod
	public void setFilteredItems() {

		try {
			expandedListenerActive = false;

			final TableRowDataT value = unfilteredTreeItemRoot.getValue();
			final boolean expanded = unfilteredTreeItemRoot.isExpanded();
			final TreeItem<TableRowDataT> filteredRoot = createTreeItem(value, expanded);

			setFilteredItemsRec(unfilteredTreeItemRoot, filteredRoot);

			setRoot(filteredRoot);
			fillTreeItemList();

		} finally {
			expandedListenerActive = true;
		}
	}

	private void setFilteredItemsRec(
			final UnfilteredTreeItem<TableRowDataT> unfilteredTreeItemRoot,
			final TreeItem<TableRowDataT> filteredTreeItem) {

		final List<UnfilteredTreeItem<TableRowDataT>> childrenList =
				unfilteredTreeItemRoot.getChildrenList();
		for (final UnfilteredTreeItem<TableRowDataT> unfilteredTreeItemChild : childrenList) {

			final TableRowDataT value = unfilteredTreeItemChild.getValue();
			final boolean expanded = unfilteredTreeItemChild.isExpanded();
			final TreeItem<TableRowDataT> filteredTreeItemChild = createTreeItem(value, expanded);

			setFilteredItemsRec(unfilteredTreeItemChild, filteredTreeItemChild);

			if (!filteredTreeItemChild.getChildren().isEmpty() ||
					filterPredicate.test(filteredTreeItemChild.getValue())) {
				filteredTreeItem.getChildren().add(filteredTreeItemChild);
			}
		}
	}

	@ApiMethod
	public TreeItem<TableRowDataT> createTreeItem(
			final TableRowDataT value,
			final boolean expanded) {

		final TreeItem<TableRowDataT> treeItem = new TreeItem<>(value);
		treeItem.setExpanded(expanded);
		treeItem.expandedProperty().addListener((
				observable,
				oldValue,
				newValue) -> expandedPropertyChanged());
		return treeItem;
	}

	private void expandedPropertyChanged() {

		if (expandedListenerActive) {
			fillTreeItemList();
		}
	}

	@ApiMethod
	public void fillTreeItemList() {

		treeItemList.clear();
		final TreeItem<TableRowDataT> treeItemRoot = getRoot();
		if (treeItemRoot != null) {
			fillTreeItemListRec(treeItemRoot);
		}
	}

	private void fillTreeItemListRec(
			final TreeItem<TableRowDataT> treeItem) {

		final boolean expanded = treeItem.isExpanded();
		if (expanded) {

			for (final TreeItem<TableRowDataT> treeItemChild : treeItem.getChildren()) {

				treeItemList.add(treeItemChild);
				fillTreeItemListRec(treeItemChild);
			}
		}
	}

	@Override
	public TreeItem<TableRowDataT> getTreeItem(
			final int row) {

		final TreeItem<TableRowDataT> treeItem;
		if (0 <= row && row < CollectionUtils.size(treeItemList)) {
			treeItem = treeItemList.get(row);
		} else {
			treeItem = super.getTreeItem(row);
		}
		return treeItem;
	}

	@ApiMethod
	public void addChildren(
			final TreeItem<TableRowDataT> treeItemParent,
			final List<TableRowDataT> tableRowDataList,
			final boolean expanded) {

		try {
			expandedListenerActive = false;

			int indexInTreeItemList = -1;
			for (int i = 0; i < treeItemList.size(); i++) {

				final TreeItem<TableRowDataT> treeItem = treeItemList.get(i);
				if (treeItemParent.equals(treeItem)) {

					indexInTreeItemList = i;
					break;
				}
			}

			if (indexInTreeItemList >= 0) {

				for (final TableRowDataT tableRowData : tableRowDataList) {

					final TreeItem<TableRowDataT> treeItemChild = createTreeItem(tableRowData, true);
					treeItemParent.getChildren().add(treeItemChild);
				}
				treeItemParent.setExpanded(expanded);
				treeItemList.addAll(indexInTreeItemList + 1, treeItemParent.getChildren());
			}

			final TableRowDataT tableRowDataT = treeItemParent.getValue();
			if (tableRowDataT != null) {

				final UnfilteredTreeItem<TableRowDataT> unfilteredTreeItemParent =
						computeUnfilteredTreeItemParentRec(unfilteredTreeItemRoot, tableRowDataT);
				if (unfilteredTreeItemParent != null) {

					for (final TableRowDataT tableRowData : tableRowDataList) {

						final UnfilteredTreeItem<TableRowDataT> unfilteredTreeItemChild =
								new UnfilteredTreeItem<>(tableRowData, true);
						unfilteredTreeItemParent.getChildrenList().add(unfilteredTreeItemChild);
						unfilteredTreeItemParent.setExpanded(expanded);
					}
				}
			}

		} catch (final Exception exc) {
			Logger.printError("failed to add cells to tree view");
			Logger.printException(exc);

		} finally {
			expandedListenerActive = true;
		}
	}

	private UnfilteredTreeItem<TableRowDataT> computeUnfilteredTreeItemParentRec(
			final UnfilteredTreeItem<TableRowDataT> unfilteredTreeItem,
			final TableRowDataT tableRowDataToSearch) {

		UnfilteredTreeItem<TableRowDataT> unfilteredTreeItemResult = null;
		final TableRowDataT tableRowData = unfilteredTreeItem.getValue();
		if (tableRowDataToSearch.equals(tableRowData)) {
			unfilteredTreeItemResult = unfilteredTreeItem;

		} else {
			final List<UnfilteredTreeItem<TableRowDataT>> childrenList =
					unfilteredTreeItem.getChildrenList();
			for (final UnfilteredTreeItem<TableRowDataT> unfilteredTreeItemChild : childrenList) {

				final UnfilteredTreeItem<TableRowDataT> unfilteredTreeItemRec =
						computeUnfilteredTreeItemParentRec(unfilteredTreeItemChild, tableRowDataToSearch);
				if (unfilteredTreeItemRec != null) {

					unfilteredTreeItemResult = unfilteredTreeItemRec;
					break;
				}
			}
		}
		return unfilteredTreeItemResult;
	}

	@ApiMethod
	public List<TableRowDataT> createItemList() {

		final List<TableRowDataT> itemList = new ArrayList<>();
		createItemListRec(getRoot(), itemList);
		return itemList;
	}

	private void createItemListRec(
			final TreeItem<TableRowDataT> treeItem,
			final List<TableRowDataT> itemList) {

		if (treeItem != null) {

			final TableRowDataT value = treeItem.getValue();
			if (value != null) {
				itemList.add(value);
			}

			for (final TreeItem<TableRowDataT> child : treeItem.getChildren()) {
				createItemListRec(child, itemList);
			}
		}
	}

	@Override
	public Collection<String> getTableColumnNames() {
		return columnsByNameMap.keySet();
	}

	@ApiMethod
	public List<TreeTableColumn<TableRowDataT, Object>> getColumnList() {
		return columnList;
	}

	@ApiMethod
	public TreeTableColumn<TableRowDataT, Object> getColumnByName(
			final String columnName) {

		return columnsByNameMap.get(columnName);
	}

	@Override
	public int computeTotalItemCount() {

		return computeTotalItemCountRec(unfilteredTreeItemRoot);
	}

	private int computeTotalItemCountRec(
			final UnfilteredTreeItem<TableRowDataT> unfilteredTreeItem) {

		final List<UnfilteredTreeItem<TableRowDataT>> childrenList =
				unfilteredTreeItem.getChildrenList();
		int itemCount = childrenList.size();
		for (final UnfilteredTreeItem<TableRowDataT> childUnfilteredTreeItem : childrenList) {
			itemCount += computeTotalItemCountRec(childUnfilteredTreeItem);
		}
		return itemCount;
	}

	@ApiMethod
	public UnfilteredTreeItem<TableRowDataT> getUnfilteredTreeItemRoot() {
		return unfilteredTreeItemRoot;
	}
}
