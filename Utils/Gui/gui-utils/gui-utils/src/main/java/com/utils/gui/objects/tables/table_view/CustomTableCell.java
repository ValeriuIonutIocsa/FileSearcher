package com.utils.gui.objects.tables.table_view;

import com.utils.annotations.ApiMethod;
import com.utils.data_types.data_items.DataItem;
import com.utils.gui.GuiUtils;
import com.utils.gui.factories.BasicControlsFactories;
import com.utils.gui.factories.LayoutControlsFactories;
import com.utils.gui.objects.tables.CustomCell;
import com.utils.log.Logger;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;

public class CustomTableCell<
		RowDataT,
		CellDataT>
		extends TableCell<RowDataT, CellDataT> implements CustomCell<CellDataT> {

	@Override
	protected void updateItem(
			final CellDataT item,
			final boolean empty) {

		super.updateItem(item, empty);

		setPadding(new Insets(0));
		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

		final StackPane stackPane =
				LayoutControlsFactories.getInstance().createStackPane(Pos.CENTER_LEFT);
		setGraphic(stackPane);

		if (empty || item == null) {
			updateEmptyCell(stackPane);
		} else {
			updateNonEmptyCell(item, stackPane);
		}
	}

	@Override
	public StackPane updateEmptyCell(
			final StackPane stackPane) {

		setContextMenu(null);
		return stackPane;
	}

	@Override
	public StackPane updateNonEmptyCell(
			final CellDataT item,
			final StackPane stackPane) {

		try {
			setText(stackPane, item);

			final ContextMenu contextMenu = createContextMenu(item);
			CustomCell.setContextMenu(this, stackPane, contextMenu);

		} catch (final Exception exc) {
			Logger.printError("failed to render table cell");
			Logger.printException(exc);
		}
		return stackPane;
	}

	@Override
	public void setText(
			final StackPane stackPane,
			final CellDataT item) {

		final String text = item.toString();
		final Label label = new Label(text);

		final Tooltip tooltip = BasicControlsFactories.getInstance().createTooltip(text);
		setTooltip(tooltip);

		final String[] labelStyleClassElements = getLabelStyleClassElements();
		if (labelStyleClassElements != null) {
			label.getStyleClass().addAll(labelStyleClassElements);
		}

		final ContentDisplay labelContentDisplay = getLabelContentDisplay();
		label.setContentDisplay(labelContentDisplay);
		final Node labelGraphic = createLabelGraphic(item);
		label.setGraphic(labelGraphic);

		final Pos textAlignment = getTextAlignmentValue();
		GuiUtils.addToStackPane(stackPane, label, textAlignment, 1, 1, 1, 1);
	}

	@Override
	public String[] getLabelStyleClassElements() {
		return null;
	}

	@Override
	public ContentDisplay getLabelContentDisplay() {
		return ContentDisplay.LEFT;
	}

	@Override
	public Node createLabelGraphic(
			final CellDataT item) {
		return null;
	}

	@Override
	public Pos getTextAlignmentValue() {
		return Pos.CENTER_LEFT;
	}

	@Override
	public ContextMenu createContextMenu(
			final CellDataT item) {
		return null;
	}

	@ApiMethod
	protected RowDataT getRowData() {

		RowDataT rowData = null;
		final TableRow<RowDataT> tableRow = getTableRow();
		if (tableRow != null) {
			rowData = tableRow.getItem();
		}
		return rowData;
	}

	@ApiMethod
	protected <
			CellValue> CellValue getCellData(
					final Class<CellValue> cellValueClass) {

		CellValue cellData = null;
		final CellDataT item = getItem();
		if (item instanceof final DataItem<?> dataItem) {

			final Object value = dataItem.getValue();
			if (cellValueClass.isInstance(value)) {
				cellData = cellValueClass.cast(value);
			}
		}
		return cellData;
	}

	@ApiMethod
	protected int computeColumnIndex() {

		final TableColumn<RowDataT, CellDataT> tableColumn = getTableColumn();
		return getTableView().getColumns().indexOf(tableColumn);
	}
}
