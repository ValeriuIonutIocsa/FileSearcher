package com.utils.gui.objects.tables;

import com.utils.annotations.ApiMethod;
import com.utils.gui.GuiUtils;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.IndexedCell;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public interface CustomCell<
		CellDataT> {

	@ApiMethod
	StackPane updateEmptyCell(
			StackPane stackPane);

	@ApiMethod
	StackPane updateNonEmptyCell(
			CellDataT item,
			StackPane stackPane);

	@ApiMethod
	void setText(
			StackPane stackPane,
			CellDataT item);

	@ApiMethod
	String[] getLabelStyleClassElements();

	@ApiMethod
	ContentDisplay getLabelContentDisplay();

	@ApiMethod
	Node createLabelGraphic(
			CellDataT item);

	@ApiMethod
	Pos getTextAlignmentValue();

	static void setContextMenu(
			final IndexedCell<?> cell,
			final StackPane stackPane,
			final ContextMenu contextMenu) {

		if (contextMenu != null) {
			final Polygon polygon = new Polygon(5, 0, 0, 5, 5, 5);
			polygon.setFill(Color.DARKORANGE);
			GuiUtils.addToStackPane(stackPane, polygon, Pos.BOTTOM_RIGHT, 0, 0, 0, 0);
		}

		cell.setContextMenu(contextMenu);
	}

	@ApiMethod
	ContextMenu createContextMenu(
			CellDataT item);
}
