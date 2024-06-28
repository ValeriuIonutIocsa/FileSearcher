package com.utils.gui.objects.tables.table_view;

import java.util.List;

import com.utils.data_types.table.TableRowDataMergedCells;
import com.utils.gui.GuiUtils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public abstract class AbstractCustomTableCellMergedCells<
		TableRowDataMergedCellsT extends TableRowDataMergedCells<TableRowDataMergedCellsT>>
		extends AbstractCustomTableCell<TableRowDataMergedCellsT, Object> {

	private static final Border BORDER_OTHERS = new Border(new BorderStroke(
			Color.BLACK,
			BorderStrokeStyle.NONE,
			CornerRadii.EMPTY, new BorderWidths(1.5), new Insets(0, -1, 0, -1)));

	private static final Border BORDER_LAST = new Border(new BorderStroke(
			Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
			BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
			CornerRadii.EMPTY, new BorderWidths(1.5), new Insets(0, -1, 0, -1)));

	private final boolean mergeColumn;

	protected AbstractCustomTableCellMergedCells(
			final boolean mergeColumn) {

		this.mergeColumn = mergeColumn;
	}

	@Override
	public void setText(
			final StackPane stackPane,
			final Object item) {

		final TableRowDataMergedCellsT rowData = getRowData();
		if (rowData == null) {
			super.setText(stackPane, item);

		} else {
			final List<TableRowDataMergedCellsT> itemList = getTableView().getItems();
			final int rowIndex = itemList.indexOf(rowData);

			final boolean firstCell;
			if (rowIndex == 0) {
				firstCell = true;
			} else {
				final TableRowDataMergedCellsT beforeRowData = itemList.get(rowIndex - 1);
				firstCell = rowData.checkFirstCell(beforeRowData);
			}
			final String text = createText(item, firstCell);
			final Label label = new Label(text);
			label.setGraphic(createLabelGraphic(item));
			final Pos textAlignment = getTextAlignmentValue();
			GuiUtils.addToStackPane(stackPane, label, textAlignment, 1, 1, 1, 1);

			final boolean lastCell;
			if (rowIndex == itemList.size() - 1) {
				lastCell = true;
			} else {
				final TableRowDataMergedCellsT afterRowData = itemList.get(rowIndex + 1);
				lastCell = rowData.checkLastCell(afterRowData);
			}
			if (lastCell) {
				stackPane.setBorder(BORDER_LAST);
			} else {
				stackPane.setBorder(BORDER_OTHERS);
			}
		}
	}

	private String createText(
			final Object item,
			final boolean firstCell) {

		final String text;
		if (mergeColumn) {
			if (firstCell) {
				text = item.toString();
			} else {
				text = "";
			}
		} else {
			text = item.toString();
		}
		return text;
	}
}
